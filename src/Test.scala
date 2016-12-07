/**
  * Created by zhuting on 2016/12/3.
  */

  import org.apache.spark.{SparkConf, SparkContext}
  import tech.locusxt.pgaf.example.{TSPDistributedPop, TSPSerialPop}

  object Test{
    def main(args: Array[String]): Unit = {
//      val pop = new TSPSerialPop()
//      pop.evolve()
      val conf = new SparkConf().setAppName("TSP")
      val spark = new SparkContext(conf)
      val dpop = new TSPDistributedPop(spark)
      dpop.evolve()
      spark.stop()
    }
  }



