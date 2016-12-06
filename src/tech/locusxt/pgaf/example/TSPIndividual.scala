/**
  * Created by zhuting on 2016/12/4.
  */
package tech.locusxt.pgaf.example

import tech.locusxt.pgaf.Individual

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

  class TSPIndividual(val cities: Cities) extends Individual with java.io.Serializable{
    var chromosome = new Array[Int](cities.cityNum)
    var distanceSum = Int.MaxValue
    fitness = Double.MaxValue

    override def crossover(individual: Individual): Individual = {
      val p1 = this
      val p2 = individual.asInstanceOf[TSPIndividual]//this is important
      val parentChromosome = Array(p1.chromosome, p2.chromosome)
      val unvisited = new scala.collection.mutable.HashMap[Int, Boolean]
      for (i <- 0 until p1.cities.cityNum) unvisited += (i -> true)

      val newChromosome = new ArrayBuffer[Int]()
      var startCity = parentChromosome((new Random).nextInt(2))(0)
      newChromosome += startCity
      unvisited -= startCity
      for (i <- 1 until p1.cities.cityNum){
        val route1 = p1.cities.distMatrix(startCity)(parentChromosome(0)(i))
        val route2 = p1.cities.distMatrix(startCity)(parentChromosome(1)(i))
        if (unvisited.getOrElse(parentChromosome(0)(i), false) && route1 < route2
          || !unvisited.getOrElse(parentChromosome(1)(i), false)){
          startCity = parentChromosome(0)(i)
          newChromosome += startCity
          unvisited -= startCity
        }
        else if (unvisited.getOrElse(parentChromosome(1)(i), false)){
          startCity = parentChromosome(1)(i)
          newChromosome += startCity
          unvisited -= startCity
        }
        else{
          val tmp = (new Random).nextInt(unvisited.keySet.size)
          startCity = unvisited.keySet.toArray.apply(tmp)
          newChromosome += startCity
          unvisited -= startCity
        }
      }
//      new Individual(newChromosome.toArray, p1.cities)
      val p = new TSPIndividual(this.cities)
      p.chromosome = newChromosome.toArray
      p
    }

    override def mutate(): Individual = {
      val i = (new Random).nextInt(chromosome.length)
      var tmp = (new Random).nextInt(chromosome.length - 1)
      val j = if (tmp >= i) tmp + 1 else tmp
      val newChromosome = this.chromosome.toBuffer

      //just a swap(i, j)
      tmp = newChromosome(j)
      newChromosome(j) = newChromosome(i)
      newChromosome(i) = tmp

      val p = new TSPIndividual(this.cities)
      p.chromosome = newChromosome.toArray
      p
    }

    override def randomGen(): Unit = {
      chromosome = TSPIndividual.randomArray(cities.cityNum)//random gen
    }

    override def calFitness() = {
      val distances = for (i <- 1 until chromosome.length) yield cities.distMatrix(chromosome(i - 1))(chromosome(i))
      distanceSum = distances.toList.+:(cities.distMatrix(chromosome(chromosome.length - 1 ))(chromosome(0))).sum
      fitness = 1 / distanceSum.toDouble
      fitness
    }
  }

  object TSPIndividual{
    def randomArray(n : Int) = {
      var arr = 0 until n toArray
      var outList:List[Int] = Nil
      var border = n
      for (i <- 0 until n) {
        val index=(new Random).nextInt(border)
        outList = outList:::List(arr(index))
        arr(index) = arr.last
        arr = arr.dropRight(1)
        border -= 1
      }
      outList.toArray
    }
  }