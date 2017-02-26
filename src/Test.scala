/**
  * Created by zhuting on 2016/12/3.
  */

  import org.apache.spark.{SparkConf, SparkContext}
  import tech.locusxt.pgaf.example._

  object Test{
    def main(args: Array[String]): Unit = {
//      val pop = new TSPSerialPop()
//      pop.evolve()

    //TSP
//      val conf = new SparkConf().setAppName("TSP")
//      val spark = new SparkContext(conf)
//      val dpop = new TSPDistributedPop(spark)
//      dpop.evolve()
//      spark.stop()

    //Test decycle
//      var arr1 = Array(0, 1, 2, 3, 4, 5)
//      var arr2 = Array(1, 2, 4, 0, 5, 3)
//      var result = MIndividual.per2cycle(arr1, arr2)
//      println(result.mkString(" "))
//      var arr3 = MIndividual.multiplyCycle(arr1, result)
//      println("3")
//      println(arr3.mkString(" "))
//      result = MIndividual.halfCycle(result)
//      println(result.mkString(" "))
//      result = Array(2, 4, 5 , -1)
//      println("1")
//      println(arr1.mkString(" "))
//      arr3 = MIndividual.multiplyCycle(arr1, result)
//      println("4")
//      println(arr3.mkString(" "))
//
//      result = Array(1, 2, 4, 0, 5, 3, -1, 7, 8, 9, -1, 11, 12, 13, 14, -1)
//      var m = MIndividual.cycle2map(result)
//      println(m.mkString(" "))
//      result = MIndividual.halfCycle(result)
//      println(result.mkString(" "))

      //Test graphs load
//      val graphs = new Graphs()
//      graphs.loadData("/Users/zhuting/Downloads/testg1.txt", "/Users/zhuting/Downloads/testg1.txt")
//      graphs.mtx1.map(i => println(i.mkString(" ")))
//      graphs.mtx2.map(i => println(i.mkString(" ")))
//      println(graphs.nodeNum)


      val pop = new MSerialPop()
      pop.evolve()
    }
  }



