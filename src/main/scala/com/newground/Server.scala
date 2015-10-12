package com.newground

import org.eclipse.jetty.server.{ Server => JettyServer }
import org.eclipse.jetty.servlet.{ ServletHolder, ServletContextHandler }
import org.eclipse.jetty.server.handler.HandlerList
import com.sun.jersey.spi.container.servlet.{ ServletContainer => JettyServletContainer }
import javax.ws.rs.core.{ Application => JerseyApplication }

/**
 * @author mike
 */
class Server(port: Int) {
  val server: JettyServer = new JettyServer(port)

  val context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS)
  context.setContextPath("/")

  val jerseyServletHolder = new ServletHolder(classOf[JettyServletContainer])
  jerseyServletHolder.setInitParameter("javax.ws.rs.Application", "com.newground.Application")
  jerseyServletHolder.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true")
  context.addServlet(jerseyServletHolder, "/*")

  val handlers = new HandlerList()
  handlers.setHandlers(Array(context))
  server.setHandler(handlers)

  def start() {
    server.start()
  }

  def stop() {
    server.stop()
  }

  def isRunning = server.isRunning
}

import scala.collection.JavaConversions.setAsJavaSet

class Application extends JerseyApplication {
  override def getSingletons = setAsJavaSet(Context.instances)
}

object Runner extends App {
  Context.server.start()
}
