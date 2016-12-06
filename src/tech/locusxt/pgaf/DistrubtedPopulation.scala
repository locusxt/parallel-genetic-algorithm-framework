package tech.locusxt.pgaf

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
* Created by zhuting on 2016/12/4.
*/
trait DistrubtedPopulation extends Population{
  val sc:SparkContext
  val slices:Int
//  var populationArr:Array[SubPopulation]
  var populationRDD:RDD[SubPopulation]

  def evolve(): Unit ={
//    startTime = System.currentTimeMillis
    var limit = false
    populationRDD = populationRDD.map(p => {
      p.genOriginalPop()
      p
    }).cache()

//    var lastBest = Int.MaxValue
    while(!limit){
      populationRDD = populationRDD.map(p => {
        p.evolveFor(10000)
        p
      }).cache()
      val bestIndividuals = populationRDD.map(p => p.individuals(0)).collect()

      populationRDD = populationRDD.map(p => {
        p.updateWith(bestIndividuals)
        p
      }).cache()
      limit = checkLimit()
    }
  }

}
