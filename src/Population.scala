/**
  * Created by zhuting on 2016/12/3.
  */

package tech.locusxt.pgaf {

  trait Population{
    def getBestIndividual():Any
    def evolve()
    def checkLimit():Boolean
  }

}
