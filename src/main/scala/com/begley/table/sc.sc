package com.begley.table

import collection.immutable.TreeMap

object Scratch {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  val y = List(12D, 323D, 123424D, 232D, 1212D)   //> y  : List[Double] = List(12.0, 323.0, 123424.0, 232.0, 1212.0)

  def weightAvg(x: List[Double]): Double = {
    x.foldLeft((1, 0D))((tup, li) => (tup._1 + 1, tup._2 + li * tup._1))._2 / (1 to x.size).sum
  }                                               //> weightAvg: (x: List[Double])Double

  def wAvg(x: List[Double]): List[Double] = {

    def loop(inner: List[Double], accum: List[Double]): List[Double] = {
      val result = accum :+ weightAvg(inner)
      val tail = inner.tail
      if (tail.isEmpty) result else loop(tail, result)
    }
    loop(x, List())
  }                                               //> wAvg: (x: List[Double])List[Double]
  
  wAvg(y)                                         //> res0: List[Double] = List(25194.533333333333, 25271.5, 21254.0, 885.33333333
                                                  //| 33334, 1212.0)

  // good example about mapping..F
  //val x =  Map("k1" -> 1, "k2" -> 2)

  //x.map { case (k,v) => (k,v+1) }

}


   
                                                  
                                                  
  


   