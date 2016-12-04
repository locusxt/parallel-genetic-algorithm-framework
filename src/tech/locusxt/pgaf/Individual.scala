package tech.locusxt.pgaf

/**
* Created by zhuting on 2016/12/4.
*/
trait Individual {
  var fitness:Double = 0.0
  def crossover(individual: Individual):Individual
  def mutate():Individual
  def randomGen()
  def calFitness()
}
