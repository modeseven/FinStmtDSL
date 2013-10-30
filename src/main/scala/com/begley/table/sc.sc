package com.begley.table

import collection.immutable.TreeMap
import scala.beans.BeanProperty

object Scratch {

  class Proj(
    @BeanProperty var revenue: Double,
    @BeanProperty var revGr: Double,
    @BeanProperty var ebtida: Double,
    @BeanProperty var ebtidaGr: Double) {

    def calc(lastRevenue: Option[Double]) = {

      if (lastRevenue.isDefined) {
        revenue = lastRevenue.get * (1 + revGr)
      }

      ebtida = revenue * ebtidaGr
    }

  }

  val p1: Proj = new Proj(120000000, 0D, 0, .081) //> p1  : com.begley.table.Scratch.Proj = com.begley.table.Scratch$Proj@5d1f1d6
  val p2: Proj = new Proj(0, .05, 0, .081)        //> p2  : com.begley.table.Scratch.Proj = com.begley.table.Scratch$Proj@4f124609
                                                  //| 
  val p3: Proj = new Proj(0, .05, 0, .09)         //> p3  : com.begley.table.Scratch.Proj = com.begley.table.Scratch$Proj@38b4216d
                                                  //| 
  val rawList = List(p1, p2, p3)                  //> rawList  : List[com.begley.table.Scratch.Proj] = List(com.begley.table.Scrat
                                                  //| ch$Proj@5d1f1d6, com.begley.table.Scratch$Proj@4f124609, com.begley.table.Sc
                                                  //| ratch$Proj@38b4216d)
  
  val x:Option[Proj] = None                       //> x  : Option[com.begley.table.Scratch.Proj] = None
  
  rawList.foldLeft(x)((last:Option[Proj],li) =>{
  val lr:Option[Double] = if(last.isDefined) Option(last.get.revenue) else None
  li.calc(lr)
  Some(li)
  }
  )                                               //> res0: Option[com.begley.table.Scratch.Proj] = Some(com.begley.table.Scratch$
                                                  //| Proj@38b4216d)
  
  rawList.mapConserve(_.ebtida.formatted("%.3f")) //> res1: List[Object] = List(9720000.000, 10206000.000, 11907000.000)

}


   
                                                  
                                                  
  


   