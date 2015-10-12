# NewGround Factor server

## Run
`sbt run`

## Test 
Edit `f1.csv` and `f2.csv`

- Get 
```
curl -XGET http://localhost:8080/factor?v1=5
```
- Post
```
curl -XPOST -H 'Content-type: text/xml' -d '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><factor-calculation-request><v2>10</v2><v3>15</v3><v4>15</v4></factor-calculation-request>'  http://localhost:8080/factor
```

## Contribute
`sbt eclipse` 

## Improvements 
In case we are able to replace csv to binary format we can use a memory mapped file to have random acces to the data for much better performance.
``` Scala
package com.newground

import java.io.RandomAccessFile
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import scala.collection.mutable.ListBuffer
import sun.nio.ch.DirectBuffer
import java.io.File

/**
 * @author mike
 */
class Matrix(h: Int, w: Int, f: String) {
  
  val MappingSize = 1 << 30
  
  val mappings = ListBuffer.empty[MappedByteBuffer]
  val raf = new RandomAccessFile(File.createTempFile("NewGround", "csv"), "rw")  
  
  val size = 8L * w * h
  var offset = 0L
 
  while (offset < size) {
    var asize = Math.min(size - offset, MappingSize)
    mappings.append(raf.getChannel().map(FileChannel.MapMode.READ_WRITE, offset, asize))
    offset += MappingSize
  }
  
  private def pos(r: Int, c: Int): (Int, Int) = {
    val pos = 8L * (r * h + c) 
    ((pos / MappingSize).toInt, (pos % MappingSize).toInt)
  }
  
  def get(r: Int, c: Int): Double = {
    val(map_num, map_off) = pos(r, c)    
    mappings(map_num).getDouble(map_off)
  }

  def set(r: Int, c: Int, d: Double): Unit = {
    val(map_num, map_off) = pos(r, c)    
    mappings(map_num).putDouble(map_off, d)
  }
  
  def close(): Unit = {
    for (mapping <- mappings) {
      mapping.asInstanceOf[DirectBuffer].cleaner().clean()
    }
    mappings.clear()
    raf.close()
  }
  
} 
```
