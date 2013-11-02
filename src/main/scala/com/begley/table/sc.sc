package com.begley.table

import collection.immutable.TreeMap
import scala.beans.BeanProperty

object Scratch {

  

 
  val band: TreeMap[String, Int] = TreeMap("Dave" -> 10,
                "Tony" -> 20,
                "Greg" -> 30)                     //> band  : scala.collection.immutable.TreeMap[String,Int] = Map(Dave -> 10, Gre
                                                  //| g -> 30, Tony -> 20)
                                                  
   band.values.reduceRight(_ + _)                 //> res0: Int = 60



}


   
                                                  
                                                  
  


   