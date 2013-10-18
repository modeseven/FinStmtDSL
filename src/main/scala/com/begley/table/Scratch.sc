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


  val revRow = Row.exstractRow(isl, "revenue")    //> revRow  : com.begley.table.Row = Row(Map(2010-01-12 -> [-], 2011-01-12 -> [1
                                                  //| 00.0], 2012-01-12 -> [120.0], 2013-01-12 -> [170.0]))
  val cogs = Row.exstractRow(isl, "costOfGoodsSold")
                                                  //> cogs  : com.begley.table.Row = Row(Map(2010-01-12 -> [-], 2011-01-12 -> [50.
                                                  //| 0], 2012-01-12 -> [10.0], 2013-01-12 -> [75.0]))
  val grossProfit = revRow - cogs                 //> grossProfit  : com.begley.table.Row = Row(Map(2010-01-12 -> [-], 2011-01-12 
                                                  //| -> [50.0], 2012-01-12 -> [110.0], 2013-01-12 -> [95.0]))
  
  val profGR = grossProfit gr                     //> profGR  : com.begley.table.Row = Row(Map(2010-01-12 -> [-], 2011-01-12 -> [-
                                                  //| ], 2012-01-12 -> [1.2], 2013-01-12 -> [-0.13636363636363635]))
                                                  
   val rowList = List(revRow,cogs,grossProfit)    //> rowList  : List[com.begley.table.Row] = List(Row(Map(2010-01-12 -> [-], 2011
                                                  //| -01-12 -> [100.0], 2012-01-12 -> [120.0], 2013-01-12 -> [170.0])), Row(Map(2
                                                  //| 010-01-12 -> [-], 2011-01-12 -> [50.0], 2012-01-12 -> [10.0], 2013-01-12 -> 
                                                  //| [75.0])), Row(Map(2010-01-12 -> [-], 2011-01-12 -> [50.0], 2012-01-12 -> [11
                                                  //| 0.0], 2013-01-12 -> [95.0])))
                                                  
  rowList.map(_.gr).map(_.avg)                    //> res4: List[com.begley.table.Row] = List(Row(Map(2010-01-12 -> [0.15416666666
                                                  //| 666667], 2011-01-12 -> [0.20555555555555557], 2012-01-12 -> [0.3083333333333
                                                  //| 3335], 2013-01-12 -> [0.4166666666666667])), Row(Map(2010-01-12 -> [1.425], 
                                                  //| 2011-01-12 -> [1.9000000000000001], 2012-01-12 -> [2.85], 2013-01-12 -> [6.5
                                                  //| ])), Row(Map(2010-01-12 -> [0.2659090909090909], 2011-01-12 -> [0.3545454545
                                                  //| 454545], 2012-01-12 -> [0.5318181818181817], 2013-01-12 -> [-0.1363636363636
                                                  //| 3635])))

   
   
                                                  
                                                  
  


   

}