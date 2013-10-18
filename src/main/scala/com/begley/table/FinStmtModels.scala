package com.begley.table
import com.github.nscala_time.time.Imports._
import scala.beans.ScalaBeanInfo


// facts for a simple income statement

trait FinFact {
  def getDate():LocalDate
}

case class IncomeStmt (
  date:LocalDate,  
  revenue: Option[Long] = None,
  costOfGoodsSold: Option[Long] = None) extends FinFact{  
  def getDate():LocalDate = date
}
  
