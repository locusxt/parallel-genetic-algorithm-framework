/**
  * Created by zhuting on 2016/12/3.
  */

  import tech.locusxt.pgaf.example.TSPSerialPop

  object Test{
    def main(args: Array[String]): Unit = {
      val pop = new TSPSerialPop()
      pop.evolve()
    }
  }



