package tech.locusxt.pgaf.example

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import tech.locusxt.pgaf.{DistrubtedPopulation, Individual, SubPopulation}

/**
  * Created by zhuting on 2016/12/6.
  */
class TSPDistrubtedPop(val sc : SparkContext, val slices : Int = 4,
                       val timeLimit: Int = Int.MaxValue, val evolveLimit: Int = Int.MaxValue) extends DistrubtedPopulation {
  val populationArr = new Array[TSPSubPopulation](slices)
  for (i <- 0 until slices){
    populationArr(i) = new TSPSubPopulation
  }
  override var populationRDD: RDD[SubPopulation] = sc.parallelize(populationArr, slices)
  var startTime = System.currentTimeMillis / 1000
  var evolveCnt = 0
  var minDistance = Int.MaxValue
  override def getBestIndividual: Any = {

  }

  def printInfo(): Unit ={
    print(evolveCnt + ",")
    print((System.currentTimeMillis - startTime) / 1000)
    print(",")
    println(minDistance)
  }


  override def checkLimit(): Boolean ={
    if (System.currentTimeMillis - startTime > timeLimit) true
        else if (evolveCnt >= evolveLimit) true
    else false
  }

  override def evolve(): Unit ={
    startTime = System.currentTimeMillis
    var limit = false
    populationRDD = populationRDD.map(p => {
      p.genOriginalPop()
      p
    }).cache()

    minDistance = populationRDD.map(p => {
      p.calFitness()
      p
    }).reduce((p1, p2)=>{
      if (p1.asInstanceOf[TSPSubPopulation].minDistance < p2.asInstanceOf[TSPSubPopulation].minDistance) p1
      else p2
    }).asInstanceOf[TSPSubPopulation].minDistance
    printInfo()
    var lastBest = Int.MaxValue
    while(!limit){
      populationRDD = populationRDD.map(p => {
        p.evolveFor(100)
        p
      }).cache()
      val bestIndividuals = populationRDD.map(p => p.individuals(0)).collect()
      minDistance = bestIndividuals.reduce((i1, i2) => if (i1.asInstanceOf[TSPIndividual].distanceSum < i2.asInstanceOf[TSPIndividual].distanceSum) i1 else i2).asInstanceOf[TSPIndividual].distanceSum
      evolveCnt += 100
      printInfo()
      lastBest = minDistance

      populationRDD = populationRDD.map(p => {
        p.updateWith(bestIndividuals.asInstanceOf[Array[Individual]])
        p
      }).cache()
      limit = checkLimit()
    }
    printInfo()
  }

}
