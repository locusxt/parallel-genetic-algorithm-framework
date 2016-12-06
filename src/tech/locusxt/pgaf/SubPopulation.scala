package tech.locusxt.pgaf

/**
  * Created by zhuting on 2016/12/6.
  */
trait SubPopulation extends SerialPopulation with java.io.Serializable{

  //update with individuals from other population
  def updateWith(arr : Array[Individual]): Unit = {
    individuals(0) = arr(0)
    for (i <- 1 until arr.length){
      individuals(size - i) = arr(i)
    }
  }

  //sync after evolve for *times*
  def evolveFor(times: Int = 1): Unit ={
    for (i <- 0 until times) {
      calFitness()
      genNextGeneration()
    }
  }
}
