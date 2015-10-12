package com.newground

import com.newground.api.FactorResource

import org.codehaus.jackson.map.ObjectMapper

import javax.ws.rs.Produces
import javax.ws.rs.ext.Provider

/**
 * @author mike
 */
object Context {

  val server = new Server(8080)

  val mapper = {
    val mapper = new ObjectMapper()
    mapper
  }

  val instances: Set[AnyRef] = Set[AnyRef](
    new FactorResource,
    new ContextResolver(mapper)
  )

}