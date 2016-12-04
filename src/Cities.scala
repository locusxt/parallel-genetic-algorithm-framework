import scala.io.Source
import scala.math._

/**
  * Created by zhuting on 2016/12/4.
  */
package tech.locusxt.pgaf.example {

  import scala.collection.mutable.ArrayBuffer

  class Cities extends java.io.Serializable {
    var cityNum = 0
    var distMatrix = Array.ofDim[Int](1, 1)

    def loadData(fileUri: String): Unit = {
      val file = Source.fromFile(fileUri)
      var distArr = ArrayBuffer[Int]()
      file.getLines.foreach(s => {
        val ss = s.split(" ", 0)
        distArr ++= (for (i <- ss if i.compareTo("") != 0) yield i.toInt)
      })
      cityNum = sqrt(distArr.length.toDouble).toInt
      distMatrix = Array.ofDim[Int](cityNum, cityNum)
      for (i <- 0 until distArr.length) {
        distMatrix(i / cityNum)(i % cityNum) = distArr(i)
      }
    }
  }

}
