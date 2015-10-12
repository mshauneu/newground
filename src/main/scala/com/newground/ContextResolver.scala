package com.newground

import javax.ws.rs.ext.{ ContextResolver => JerseyContextResolver, Provider }
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

import org.codehaus.jackson.map.ObjectMapper

/**
 * @author mike
 */
@Provider
@Produces(Array(MediaType.APPLICATION_XML))
class ContextResolver(mapper: ObjectMapper) extends JerseyContextResolver[ObjectMapper] {

  def getContext(clazz: Class[_]) = mapper

}