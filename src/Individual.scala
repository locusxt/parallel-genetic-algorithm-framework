/**
  * Created by zhuting on 2016/12/3.
  */
package tech.locusxt.pgaf {

  trait Individual {
    var fitness:Double = 0.0
    def crossover(individual: Individual):Individual
    def mutate():Individual
    def randomGen()
    def calFitness()
  }
}
