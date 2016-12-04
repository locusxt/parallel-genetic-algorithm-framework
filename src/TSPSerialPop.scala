/**
  * Created by zhuting on 2016/12/4.
  */

package tech.locusxt.pgaf.example {

  import tech.locusxt.pgaf.{Individual, SerialPopulation}

  class TSPSerialPop(var size:Int = 100, var crossoverRate:Double = 0.8, val timeLimit:Int = Int.MaxValue, val evolveLimit:Int = Int.MaxValue, var mutateRate: Double = 0.4) extends SerialPopulation with java.io.Serializable{
//    override var size: Int = _Int
//    override var crossoverRate: Double = _
//    override var mutateRate: Double = _
    override var individuals = new Array[TSPIndividual](size).asInstanceOf[Array[Individual]]
    override var probability = new Array[Double](size)
    var cities = new Cities()
    var startTime = System.currentTimeMillis / 1000
    var evolveCnt = 0
    var minDistance = Int.MaxValue

    //when to stop
    override def checkLimit(): Boolean ={
      //    println("checking...")
      if (System.currentTimeMillis - startTime > timeLimit) true
      else if (evolveCnt >= evolveLimit) true
      else false
    }


    //init individuals array
    override def init(): Unit = {
      cities.loadData("/Users/zhuting/Downloads/att48_d_10628.txt")
      for (i <- 0 until size){
        individuals(i) = new TSPIndividual(cities)
      }
    }

    def printInfo(): Unit ={
      print(evolveCnt + ",")
      print((System.currentTimeMillis - startTime) / 1000)
      print(",")
      println(minDistance)
    }

    override def evolve(): Unit ={
      startTime = System.currentTimeMillis
      genOriginalPop()
      var limit = false
      var lastBest = Int.MaxValue
      while (!limit){
        calFitness()
        minDistance = getBestIndividual().asInstanceOf[TSPIndividual].distanceSum
        if(evolveCnt % 100 == 0 || minDistance != lastBest) {printInfo(); lastBest = minDistance;}
        genNextGeneration()
        limit = checkLimit()
        evolveCnt += 1
      }
      printInfo()
    }
  }

}
