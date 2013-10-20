package com.begley.table

import collection.mutable.Map
import collection.immutable.TreeMap
import scala.collection.immutable.ListMap
import org.joda.time.LocalDate
import collection.JavaConversions._

package object table {
  type Value = Option[Double]
  type ValuePair = (Value, Value)
  val dateFormat = new java.text.SimpleDateFormat("mm/dd/yyyy")
  def date(dateStr: String) = LocalDate.fromDateFields(table.dateFormat.parse(dateStr))
}

trait FinFact {
  def getLocalDate(): LocalDate
}

// facts for a simple income statement

case class IncomeStmt(
  date: LocalDate,
  revenue: Option[Long] = None,
  costOfGoodsSold: Option[Long] = None) extends FinFact {
  def getLocalDate(): LocalDate = date
}
// shouold use immutable treemap?
case class RowMap(var rmap: collection.immutable.Map[String, Row] = collection.immutable.Map.empty) {
  def add(key: String, row: Row): Row = {
    rmap += (key -> row) // if rmap is avar i can use += else I cant'.. i can only use + , what does this mean?
    row
  }
  def get(key: String) = rmap.get(key)
  def avg() = RowMap(rmap.mapValues(_.avg))
  def gr() = RowMap(rmap.mapValues(_.gr))
  def grAvg() = gr avg
}


class RowBuilder(factList: java.util.List[FinFact]) {
  private val map: RowMap = new RowMap

  def exstractRow(label: String, methodName: String): Row = map.add(label, Row(factList, methodName))

  def calcRow(label: String, row: Row): Row = map.add(label, row)

  def getRow(label: String) = map.get(label)

  def getMap = map

}

// do i want this kind of equality? if you add the same day.. it overrides one alreayd aded.. is this ok??? maybe not a case class.....
case class ColIndex(label: String, date: LocalDate) {
  override def toString: String = label
}

object ColIndex {

  implicit def colIndexOrdering: Ordering[ColIndex] = Ordering.fromLessThan(_.date isBefore _.date)

  def apply(dateAsString: String) = new ColIndex(dateAsString, table.date(dateAsString))

  def apply(label: String, dateStr: String) = new ColIndex(label, table.date(dateStr))

  def apply(date: LocalDate) = new ColIndex(date.toString(), date)

}
/**
 * not sure we need cells at all?
 */
case class Cell(column: ColIndex, value: table.Value) { //not sure if cell needs row index..
  override def toString: String = "[" + value.getOrElse("-").formatted("%.3f") + "]"
}

case class Row(row: TreeMap[ColIndex, Cell], desc: String) {

  def plus(that: Row) = applyZipFn(that, addRowTuple, this.desc + "+" + that.desc)
  def minus(that: Row) = applyZipFn(that, subRowTuple, this.desc + "-" + that.desc)
  def mult(that: Row) = applyZipFn(that, multRowTuple, this.desc + "*" + that.desc)
  def div(that: Row) = applyZipFn(that, divRowTuple, this.desc + "/" + that.desc)
  def incDec() = calc(increaseDecrease, "incDec(" + this.desc + ")")
  def gr() = calc(growthRate, "gr(" + this.desc + ")")
  def avg() = average;
  def ttm() = trailingFourPeriods;

  def +(that: Row) = plus(that)
  def -(that: Row) = minus(that)
  def *(that: Row) = mult(that)
  def /(that: Row) = div(that)
  // todo: weigthed avg.

  override def toString: String = desc + ":" + row

  def getLastCell: Option[Cell] = row.get(row.lastKey)

  def getColumns: java.util.Set[ColIndex] = row.keySet

  def getValues = row.values.map(_.value.getOrElse(Double.NaN)).toArray

  def getColumIndices = row.keys.map(row.keys.toSeq.indexOf(_)).toList.reverse

  private def addRowTuple(pair: table.ValuePair): table.Value = {
    pair match {
      case (Some(l), Some(r)) => Some(l + r)
      case (None, Some(r)) => Some(r)
      case (Some(l), None) => Some(l)
      case _ => None
    }
  }

  private def subRowTuple(pair: table.ValuePair): table.Value = {
    pair match {
      case (Some(l), Some(r)) => Some(l - r)
      case (None, Some(r)) => Some(0D - r)
      case (Some(l), None) => Some(l)
      case _ => None
    }
  }

  private def multRowTuple(pair: table.ValuePair): table.Value = {
    pair match {
      case (Some(l), Some(r)) => Some(l * r)
      case (None, Some(r)) => Some(0) // null * X should be 0?
      case (Some(l), None) => Some(0) // X * null should be 0?
      case _ => None
    }
  }

  private def divRowTuple(pair: table.ValuePair): table.Value = {
    pair match {
      case (Some(l), Some(r)) => if (r != 0) Some(l / r) else None
      case (None, Some(r)) => if (r != 0) Some(0D) else None
      case _ => None
    }
  }

  def applyZipFn(that: Row, fn: ((Option[Double], Option[Double])) => Option[Double], fnDesc: String) = {

    val l = (this.row.values, that.row.values).zipped.map((top: Cell, bottom: Cell) => {
      val tup = (top.value, bottom.value)
      val sum = fn(tup)
      Cell(top.column, sum)
    })
    val m = l map { t => (t.column, t) } toMap
    val tm = TreeMap(m.toArray: _*)
    Row(tm, fnDesc)

  }

  def trailingFourPeriods(): Row = {
    var lm: Map[ColIndex, Cell] = Map()

    row.values.foldLeft(Array(0D, 0D, 0D, 0D))(
      (state: Array[Double], cell: Cell) => {
        val newState = Array(cell.value.getOrElse(0D), state(0), state(1), state(2))
        lm(cell.column) = Cell(cell.column, Some(newState.reduceLeft(_ + _)))
        newState
      })
    Row(TreeMap(lm.toSeq: _*), "ttm(" + this.desc + ")")
  }

  def calc(fn: ((Option[Double], Option[Double])) => Option[Double], fnDesc: String): Row = {
    var lm: Map[ColIndex, Cell] = Map()

    row.values.foldLeft(Option(0D))((i: Option[Double], cell: Cell) => {
      val incDec: Option[Double] = fn(i, cell.value)
      lm(cell.column) = Cell(cell.column, incDec)
      cell.value
    })
    Row(TreeMap(lm.toSeq: _*), fnDesc)
  }

  private def average(): Row = {
    //  var lm: Map[ColIndex, Cell] = Map()

    var tm = TreeMap[ColIndex, Cell]()

    row.values.foldRight((1, 0D))(
      (cell: Cell, state: (Int, Double)) => {
        val curVal: Double = cell.value.getOrElse(0D)
        val yearsBack: String = row.keys.toSeq.indexOf(cell.column).toString
        val colIndex = ColIndex(yearsBack, cell.column.date)
        //   lm(colIndex) = Cell(colIndex, Some((curVal + state._2) / state._1)) 

        tm += colIndex -> Cell(colIndex, Some((curVal + state._2) / state._1))

        (state._1 + 1, state._2 + curVal)
      })
    Row(tm, "avg(" + this.desc + ")")
  }

  def growthRate(pair: table.ValuePair): table.Value = {
    pair match {
      case (Some(l), Some(r)) => Some((r - l) / l) // growth rate only calculated if both sides populated
      case _ => None
    }
  }

  def increaseDecrease(pair: table.ValuePair): table.Value = {
    pair match {
      case (Some(l), Some(r)) => Some(r - l)
      case (None, Some(r)) => Some(r)
      case _ => None
    }
  }

}

object Row {

  implicit def reflector(ref: AnyRef) = new {
    def getV(name: String): Any = ref.getClass.getMethods.find(_.getName == name).get.invoke(ref)
    def setV(name: String, value: Any): Unit = ref.getClass.getMethods.find(_.getName == name + "_$eq").get.invoke(ref, value.asInstanceOf[AnyRef])
  }

  // CHANGE TO "EXSTRACT ROW" and make it an Optional[Row]
  def apply(list: java.util.List[FinFact], property: String): Row = {

    var tm = TreeMap[ColIndex, Cell]()

    list.toList.map((is: FinFact) => {
      val colIndex = ColIndex(is.getLocalDate)

      val dVal: Option[Double] = is.getV(property) match {
        case i: java.lang.Long => Option(i.toDouble)
        case d: java.lang.Double => Option(d)
        case x: java.lang.Integer => Option(x.toDouble)
        case Some(l: Long) => Some(l.toDouble)
        case Some(dd: Double) => Some(dd)
        case Some(int: Int) => Some(int.toDouble)
        case _ => None
      }

      tm += colIndex -> Cell(colIndex, dVal)

    })

    Row(tm, property)

  }

  @annotation.varargs
  def sum(x: Row*): Row = {
    x.toList.reduceLeft(_ + _)
  }

}


