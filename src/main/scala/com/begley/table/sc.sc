package com.begley.table

import collection.immutable.TreeMap

object Scratch {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  val is1 = IncomeStmt(table.date("12/12/2010"), None, None)
                                                  //> is1  : com.begley.table.IncomeStmt = IncomeStmt(2010-01-12,None,None)
  val is2 = IncomeStmt(table.date("12/12/2011"), Some(100), Some(50))
                                                  //> is2  : com.begley.table.IncomeStmt = IncomeStmt(2011-01-12,Some(100),Some(50
                                                  //| ))
  val is3 = IncomeStmt(table.date("12/12/2012"), Some(120), Some(10))
                                                  //> is3  : com.begley.table.IncomeStmt = IncomeStmt(2012-01-12,Some(120),Some(10
                                                  //| ))
  val is4 = IncomeStmt(table.date("12/12/2013"), Some(170), Some(75))
                                                  //> is4  : com.begley.table.IncomeStmt = IncomeStmt(2013-01-12,Some(170),Some(75
                                                  //| ))

  var isl: java.util.List[FinFact] = new java.util.ArrayList()
                                                  //> isl  : java.util.List[com.begley.table.FinFact] = []
  isl.add(is1)                                    //> res0: Boolean = true
  isl.add(is2)                                    //> res1: Boolean = true
  isl.add(is3)                                    //> res2: Boolean = true
  isl.add(is4)                                    //> res3: Boolean = true

// use currying here.. creates function that has just "property" as param....!!!
 

  val revRow = Row(isl, "revenue")                //> revRow  : com.begley.table.Row = revenue:Map(2010-01-12 -> [-], 2011-01-12 -
                                                  //| > [100.0], 2012-01-12 -> [120.0], 2013-01-12 -> [170.0])
  
  revRow.getValues                                //> res4: Array[Double] = Array(NaN, 100.0, 120.0, 170.0)
  val cogs = Row(isl, "costOfGoodsSold")          //> cogs  : com.begley.table.Row = costOfGoodsSold:Map(2010-01-12 -> [-], 2011-0
                                                  //| 1-12 -> [50.0], 2012-01-12 -> [10.0], 2013-01-12 -> [75.0])
  val grossProfit = revRow - cogs                 //> grossProfit  : com.begley.table.Row = revenue-costOfGoodsSold:Map(2010-01-12
                                                  //|  -> [-], 2011-01-12 -> [50.0], 2012-01-12 -> [110.0], 2013-01-12 -> [95.0])
  
  val sumall = Row.sum(revRow,cogs)               //> sumall  : com.begley.table.Row = revenue+costOfGoodsSold:Map(2010-01-12 -> [
                                                  //| -], 2011-01-12 -> [150.0], 2012-01-12 -> [130.0], 2013-01-12 -> [245.0])
                                                  
   val rowList = List(revRow,cogs,grossProfit)    //> rowList  : List[com.begley.table.Row] = List(revenue:Map(2010-01-12 -> [-], 
                                                  //| 2011-01-12 -> [100.0], 2012-01-12 -> [120.0], 2013-01-12 -> [170.0]), costOf
                                                  //| GoodsSold:Map(2010-01-12 -> [-], 2011-01-12 -> [50.0], 2012-01-12 -> [10.0],
                                                  //|  2013-01-12 -> [75.0]), revenue-costOfGoodsSold:Map(2010-01-12 -> [-], 2011-
                                                  //| 01-12 -> [50.0], 2012-01-12 -> [110.0], 2013-01-12 -> [95.0]))
                                                  
  rowList.map(_.gr).map(_.avg)                    //> res5: List[com.begley.table.Row] = List(avg(gr(revenue)):Map(2010-01-12 -> [
                                                  //| 0.15416666666666667], 2011-01-12 -> [0.20555555555555557], 2012-01-12 -> [0.
                                                  //| 30833333333333335], 2013-01-12 -> [0.4166666666666667]), avg(gr(costOfGoodsS
                                                  //| old)):Map(2010-01-12 -> [1.425], 2011-01-12 -> [1.9000000000000001], 2012-01
                                                  //| -12 -> [2.85], 2013-01-12 -> [6.5]), avg(gr(revenue-costOfGoodsSold)):Map(20
                                                  //| 10-01-12 -> [0.2659090909090909], 2011-01-12 -> [0.3545454545454545], 2012-0
                                                  //| 1-12 -> [0.5318181818181817], 2013-01-12 -> [-0.13636363636363635]))
                                                  
                                                  
  def sum(x:Int *):Int={
  x.toList.reduceLeft(_ + _)
  }                                               //> sum: (x: Int*)Int
  
  
  sum(4,3,4)                                      //> res6: Int = 11
  
  
  
  

   
   
                                                  
                                                  
  


   

}