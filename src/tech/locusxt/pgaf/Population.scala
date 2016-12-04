package tech.locusxt.pgaf

/**
* Created by zhuting on 2016/12/4.
*/
trait Population{
  def getBestIndividual():Any
  def evolve()
  def checkLimit():Boolean
}
