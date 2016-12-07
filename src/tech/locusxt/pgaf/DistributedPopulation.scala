package tech.locusxt.pgaf

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
* Created by zhuting on 2016/12/4.
*/
trait DistributedPopulation extends Population{
  val sc:SparkContext
  val slices:Int
  var populationRDD:RDD[SubPopulation]

  def evolve(): Unit ={
    var limit = false
    populationRDD = populationRDD.map(p => {
      p.genOriginalPop()
      p
    }).cache()

    while(!limit){
      populationRDD = populationRDD.map(p => {
        p.evolveFor(10000)//share best individuals every 10000 turns
        p
      }).cache()
      val bestIndividuals = populationRDD.map(p => p.individuals(0)).collect()

      //share best individuals with *all* the subpopulations
      populationRDD = populationRDD.map(p => {
        p.updateWith(bestIndividuals)
        p
      }).cache()
      limit = checkLimit()
    }
  }

}
