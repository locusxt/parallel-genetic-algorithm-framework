package tech.locusxt.pgaf

import scala.util.Random

/**
* Created by zhuting on 2016/12/4.
*/
trait SerialPopulation extends Population{
  var size: Int
  var crossoverRate:Double
  var mutateRate: Double
  var individuals:Array[Individual]
  var probability:Array[Double]//init for size



  var bestFitness = -0.0
  var bestIndividualIndex = -1

  //init individuals array
  def init()

  def genOriginalPop(): Unit ={
    init()
    individuals.foreach(i => i.randomGen())//individual init
  }

  def calFitness(): Unit ={
    individuals.foreach(i => i.calFitness())
    for (i <- 0 until size){
      if (bestFitness < individuals(i).fitness){
        bestFitness = individuals(i).fitness
        bestIndividualIndex = i
      }
    }
  }

  //calculate property according to fitness
  def calProbability(): Unit ={
//      //adjust fitness
//      var distSum = 0
//      individuals.foreach(i => distSum += i.distanceSum)
//      for (i <- 0 until size){
//        individuals(i).fitness = 1.0 - individuals(i).distanceSum.toDouble / distSum.toDouble
//      }

    var fitnessSum = 0.0
    individuals.foreach(i => fitnessSum += i.fitness)
    for (i <- 0 until size){
      probability(i) = individuals(i).fitness / fitnessSum
    }
  }


  //selset based on round robin
  def select():Individual = {
    val targetProbabilitySum = (new Random()).nextDouble()
    var selectIndex = size - 1
    var curProbabilitySum = 0.0
    var found = false
    for (i <- 0 until size if !found) {
      curProbabilitySum += probability(i)
      if (curProbabilitySum > targetProbabilitySum) {
        selectIndex = i
        found = true
      }
    }
    individuals(selectIndex)
  }

  //generate n individuals
  def crossoverStep(n:Int) ={
    var newIndividuals = new Array[Individual](n)
    calProbability()
    for(i <- 0 until n){
      newIndividuals(i) =
        if ((new Random).nextDouble() <= crossoverRate && i != bestIndividualIndex)
          select().crossover(select())
//          Individual.crossover(individuals(roundRobinSelect()), individuals(roundRobinSelect()))
        else
          select()
    }
    //    newIndividuals.map(i => Individual.crossover(individuals(roundRobinSelect()), individuals(roundRobinSelect())))
    newIndividuals
  }

  //return mutate num to help your test
  def mutateStep(): Int ={
    var mutateNum = 0
    for (i <- 1 until size){
      if ((new Random()).nextDouble() <= mutateRate){
        mutateNum += 1
        val tmp = individuals(i).mutate()
        individuals(i) = tmp
      }
    }
    //guarantee the first individual is the best
    if ((new Random()).nextDouble() <= mutateRate){
      var tmp = individuals(0).mutate()
      tmp.calFitness()
      if(individuals(0).fitness < tmp.fitness)
        individuals(0) = tmp
    }
    mutateNum
  }

  //TODO error exists here  //FIXED
  def genNextGeneration(): Unit ={
    val tmp = individuals(bestIndividualIndex)
    //crossover
    val newChildren = crossoverStep(size - 1)
    //    print(newChildren.length)
    for (i <- 1 until size){
      individuals(i) = newChildren(i - 1)
    }

    //keep the best individual
    individuals(0) = tmp
    bestIndividualIndex = 0
    //mutate
    mutateStep()
  }

  //when to stop
  def checkLimit(): Boolean

  //this is a simple case you should always override it
  def evolve(): Unit ={
    genOriginalPop()
    var limit = false
    while (!limit){
      calFitness()
      genNextGeneration()
      limit = checkLimit()
    }
  }

  override def getBestIndividual: Any = {
    individuals(0)
  }
}
