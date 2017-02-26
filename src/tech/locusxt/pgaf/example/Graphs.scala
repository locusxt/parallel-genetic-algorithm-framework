package tech.locusxt.pgaf.example

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.math.sqrt

/**
  * Created by zhuting on 2017/2/26.
  */
class Graphs extends java.io.Serializable{
  var nodeNum = 0
  var mtx1 = Array.ofDim[Int](1, 1)
  var mtx2 = Array.ofDim[Int](1, 1)

  def loadSingleData(fileUri: String):Array[Array[Int]] ={
    val file = Source.fromFile(fileUri)
    var arr = ArrayBuffer[Int]()
    file.getLines.foreach(s => {
      val ss = s.split(" ", 0)
      arr ++= (for (i <- ss if i.compareTo("") != 0) yield i.toInt)
    })
    nodeNum = sqrt(arr.length.toDouble).toInt
    var tmpMtx = Array.ofDim[Int](nodeNum, nodeNum)
    for (i <- 0 until arr.length) {
      tmpMtx(i / nodeNum)(i % nodeNum) = arr(i)
    }
    tmpMtx
  }

  def loadData(fileUri1: String, fileUri2: String): Unit = {
    mtx1 = loadSingleData(fileUri1)
    mtx2 = loadSingleData(fileUri2)
  }
}
