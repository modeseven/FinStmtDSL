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

  val isl = List(is1, is2, is3, is4)              //> isl  : List[com.begley.table.IncomeStmt] = List(IncomeStmt(2010-01-12,None,N
                                                  //| one), IncomeStmt(2011-01-12,Some(100),Some(50)), IncomeStmt(2012-01-12,Some(
                                                  //| 120),Some(10)), IncomeStmt(2013-01-12,Some(170),Some(75)))

  implicit def reflector(ref: AnyRef) = new {
    def getV(name: String): Any = ref.getClass.getMethods.find(_.getName == name).get.invoke(ref)
    def setV(name: String, value: Any): Unit = ref.getClass.getMethods.find(_.getName == name + "_$eq").get.invoke(ref, value.asInstanceOf[AnyRef])
  }                                               //> reflector: (ref: AnyRef)AnyRef{def getV(name: String): Any; def setV(name: S
                                                  //| tring,value: Any): Unit}

  def rowify(list: List[IncomeStmt], property: String): Row = {
    val rowIndex1 = RowIndex("Revenue")
    var tm = TreeMap[ColIndex, Cell]()

    list map ((is: IncomeStmt) => {
      val colIndex = ColIndex(is.date)
      val anyVal:Option[Double] = is.getV(property) match {
        case i: Option[Long] =>  if (i.isDefined) Some(i.get.toDouble) else None
        case _ => None
      }
      val value: Option[Double] = anyVal

      tm += colIndex -> Cell(rowIndex1, colIndex, value)

    })

    Row(tm)

  }                                               //> rowify: (list: List[com.begley.table.IncomeStmt], property: String)com.begl
                                                  //| ey.table.Row

  val revRow = rowify(isl, "costOfGoodsSold")     //> revRow  : com.begley.table.Row = Row(Map(2010-01-12 -> [-], 2011-01-12 -> [
                                                  //| 50.0], 2012-01-12 -> [10.0], 2013-01-12 -> [75.0]))

  val revGr = revRow gr                           //> revGr  : com.begley.table.Row = Row(Map(2010-01-12 -> [-], 2011-01-12 -> [-
                                                  //| ], 2012-01-12 -> [-0.8], 2013-01-12 -> [6.5]))

}