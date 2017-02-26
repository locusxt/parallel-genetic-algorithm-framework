package tech.locusxt.pgaf.example

import tech.locusxt.pgaf.{Individual, SerialPopulation}

/**
  * Created by zhuting on 2017/2/26.
  */
class MSerialPop(var size:Int = 100, var crossoverRate:Double = 0.8, val timeLimit:Int = Int.MaxValue, val evolveLimit:Int = Int.MaxValue, var mutateRate: Double = 0.1) extends SerialPopulation with java.io.Serializable {
  override var individuals: Array[Individual] = new Array[MIndividual](size).asInstanceOf[Array[Individual]]
  override var probability: Array[Double] = new Array[Double](size)
  val graphs = new Graphs()
  var startTime = System.currentTimeMillis / 1000
  var evolveCnt = 0

  override def checkLimit(): Boolean ={
    //    println("checking...")
    if (System.currentTimeMillis - startTime > timeLimit) true
    else if (evolveCnt >= evolveLimit) true
    else false
  }

  //init individuals array
  override def init(): Unit = {
    graphs.loadData("/Users/zhuting/Downloads/testg1.txt", "/Users/zhuting/Downloads/testg1.txt")
    for (i <- 0 until size){
      individuals(i) = new MIndividual(graphs)
    }
  }

  def printInfo(): Unit ={
    print(evolveCnt + ",")
    print((System.currentTimeMillis - startTime) / 1000)
    print(",")
    println(bestFitness)
  }

  override def evolve(): Unit ={
    startTime = System.currentTimeMillis
    genOriginalPop()
    var limit = false
    var lastBest = 0.0
    while (!limit){
      calFitness()
      if(evolveCnt % 100 == 0 || bestFitness != lastBest) {printInfo(); lastBest = bestFitness;}
      genNextGeneration()
      limit = checkLimit()
      evolveCnt += 1
    }
    printInfo()
  }

}
