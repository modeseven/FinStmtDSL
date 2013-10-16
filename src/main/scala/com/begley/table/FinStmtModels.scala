package com.begley.table
import com.github.nscala_time.time.Imports._
import scala.beans.ScalaBeanInfo


// facts for a simple income statement

case class IncomeStmt(
  date:LocalDate,  
  revenue: Option[Long] = None,
  costOfGoodsSold: Option[Long] = None)
  
