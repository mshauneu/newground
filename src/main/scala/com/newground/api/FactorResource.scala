package com.newground.api

import scala.language.reflectiveCalls
import java.io.File
import java.util.concurrent.locks.{ ReentrantReadWriteLock, Lock }
import javax.ws.rs.{ POST, Produces, GET, Path, QueryParam }
import javax.ws.rs.core.MediaType
import javax.xml.bind.annotation.{ XmlRootElement, XmlAccessorType, XmlAccessType }
import com.github.tototoshi.csv.{ CSVReader, CSVWriter }


@XmlRootElement(name = "factor-result")
@XmlAccessorType(XmlAccessType.FIELD)
case class FactorResult(value: Double) {
  private def this() = this(0.0)
}

@XmlRootElement(name = "factor-calculation-request")
@XmlAccessorType(XmlAccessType.FIELD)
case class FactorCalculationRequest(v2: Int, v3: Int, v4: Int) {
  private def this() = this(-1, -1, -1)
}

@XmlRootElement(name = "factor-calculation-response")
@XmlAccessorType(XmlAccessType.FIELD)
case class FactorCalculationResponse(result: Int) {
  private def this() = this(0)
}

@Path("/factor")
@Produces(Array(MediaType.APPLICATION_XML))
class FactorResource {
  
  private val F1 = "f1.csv"
  private val F2 = "f2.csv"

  private def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B = {
    try {
      f(resource)
    } finally {
      resource.close()
    }
  }

  private def locking[B](lock: Lock)(f: Unit => B): B = {
    lock.lock()
    try {
      f(())
    } finally {
      lock.unlock()
    }
  }

  
  private val f1 = using(CSVReader.open(new File(F1))) { reader => {
    reader.all()
  }}
  private val f1h = f1.length
  private val f1w = f1(0).length
  
  private var f2 = using(CSVReader.open(new File(F2))) { reader => {
    reader.all()
  }}
  private val f2h = f2.length
  private val f2w = f2(0).length
 
  private val lock = new ReentrantReadWriteLock()
  
  
  @GET 
  def retrieve(@QueryParam("v1") v1: Int): FactorResult = {
    val v = locking(lock.readLock()) { Unit => {
      f2(v1 / f2w)(v1 % f2w)    
    }}.toDouble
    new FactorResult(if (v > 10) v - 10 else v) 
  }
    
  @POST
  def calculate(request: FactorCalculationRequest): FactorCalculationResponse = {

    var v = f1(request.v3 / f1w)(request.v3 % f1w).toDouble + request.v2
    val r = if (v < 10) { v += 10; 0 } else 1

    f2 = locking(lock.writeLock()) { Unit => {
      f2.updated(request.v4 / f2w, f2(request.v4 / f2w).updated(request.v4 % f2w, v.toString()))
    }}
    
    using(CSVWriter.open(new File(F2))) { writer => {
      writer.writeAll(f2)
    }}    
    
    new FactorCalculationResponse(r)
  }
  
}