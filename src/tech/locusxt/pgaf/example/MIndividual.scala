package tech.locusxt.pgaf.example

import tech.locusxt.pgaf.Individual

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/**
  * Created by zhuting on 2017/2/26.
  */
class MIndividual(val graphs:Graphs) extends Individual with java.io.Serializable{
  var chromosome = new Array[Int](graphs.nodeNum)
  fitness = 0.0

  override def crossover(individual: Individual): Individual = {
    val i1 = this
    val i2 = individual.asInstanceOf[MIndividual]//this is important
    val p1 = i1.chromosome
    val p2 = i2.chromosome
    var tmpP = (for (i <- 0 until graphs.nodeNum) yield i).toArray
    val path = MIndividual.per2cycle(p1, p2)
    val halfPath = MIndividual.halfCycle(path)
    val resP = MIndividual.multiplyCycle(p1, halfPath)
    val resI = new MIndividual(this.graphs)
    resI.chromosome = resP
    resI
  }

  override def mutate(): Individual = {
    val resI = new MIndividual(this.graphs)
    val randNum1 = (new Random).nextInt(this.chromosome.length - 1)
    val randNum2 = (new Random).nextInt(this.chromosome.length - 1)

    val resP = this.chromosome
    val tmp = resP(randNum1)
    resP(randNum1) = resP(randNum2)
    resP(randNum2) = tmp

    resI.chromosome = resP
    resI
  }

  override def randomGen(): Unit = {
    chromosome = MIndividual.randomArray(graphs.nodeNum)//random gen
  }

  //generate new individual randomly
  override def calFitness(): Unit = {
    var edge1 = 0
    var fEdge = 0
    val c = this.chromosome
    for (i <- 0 until graphs.nodeNum){
      for(j <- i + 1 until graphs.nodeNum){
        if(graphs.mtx1(i)(j) != 0){
          edge1 += 1
          if(graphs.mtx2(c(i))(c(j)) != 0){
            fEdge += 1
          }
        }
      }
    }
    fitness = fEdge.toFloat / edge1.toFloat
  }
}

object MIndividual{
  def randomArray(n : Int): Array[Int] = {
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

  def per2cycle(c1:Array[Int], c2:Array[Int]): Array[Int] ={
    val len = c1.length
    val visited = (for (i <- 0 until len) yield false).toArray
    var finished = false
    var start = c1(0)
    var arrBuf = ArrayBuffer[Int]()
    arrBuf += start
    visited(start) = true
    var visitedNum = 1
    while (!finished){
//      println(arrBuf)
      for(i<- 0 until len){
        if (start == -1 && !visited(c1(i))){
          start = c1(i)
          arrBuf += start
          visited(start) = true
          visitedNum += 1
        }
        if(start == c1(i)){
          if(!visited(c2(i))){
            start = c2(i)
            arrBuf += start
            visited(start) = true
            visitedNum += 1
          }
          else{
            arrBuf += -1
            start = -1
          }
        }
      }
      if (visitedNum == len) finished = true
    }
    arrBuf += -1
    arrBuf.toArray
  }

  def halfCycle(c:Array[Int]):Array[Int] = {
    val len = c.length
    var start = 0
    var arrBuf = ArrayBuffer[Int]()
    var randNum = (new Random).nextInt(len)
    for (i <- 0 until len){
      if (c(i) == -1){
        val end = i
        val cLen = end - start
        if (cLen >= 2){
          val index = randNum % cLen
          val halfLen = cLen / 2
          var cur = start + index
          for (j <- 0 until halfLen){
            if (cur >= end) cur = start
            arrBuf += c(cur)
            cur += 1
          }
          arrBuf += -1
        }
        start = i+1
      }
    }
    arrBuf.toArray
  }

  def cycle2map(c:Array[Int]):Map[Int, Int]={
    val len = c.length
    var start = 0
    var m:Map[Int, Int] = Map()
    for (i <- 1 until len){
      if (c(i) != -1){
        if(c(i - 1) != -1){
          m += (c(i - 1) -> c(i))
        }
        if (i != start && (c(i + 1) == -1) || (i + 1) >= len){
          m += (c(i) -> c(start))
        }
      }
      else{
        start = i + 1
      }
    }
//    println(m.mkString(" "))
    m
  }

  def multiplyCycle(p:Array[Int], c:Array[Int]):Array[Int]={
    val m = cycle2map(c)
    val len = p.length
    val np = new Array[Int](len)
    for(i <- 0 until len){
      val res = m.get(p(i))
      np(i) = res.getOrElse(p(i))
    }
    np
  }
}
































