package com.begley.table

import collection.mutable.Map
import collection.immutable.TreeMap
import scala.collection.immutable.ListMap
import org.joda.time.LocalDate
import collection.JavaConversions._
import java.util.LinkedHashMap
import org.joda.convert.ToString

package object table {
  type Value = Option[Double]
  type ValuePair = (Value, Value)
  val dateFormat = new java.text.SimpleDateFormat("mm/dd/yyyy")
  def date(dateStr: String) = LocalDate.fromDateFields(table.dateFormat.parse(dateStr))
}

trait FinFact {
  def getLocalDate(): LocalDate
}

/*
 * 
 * 
 */
case class RowMap(var rmap: collection.immutable.Map[String, Row] = collection.immutable.ListMap.empty) {
  def add(key: String, row: Row): Row = {
    rmap += (key -> row) // if rmap is avar i can use += else I cant'.. i can only use + , what does this mean?
    row
  }
  def getRow(key: String) = rmap.get(key)
  def avg() = RowMap(rmap.mapValues(_.avg))
  def wavg() = RowMap(rmap.mapValues(_.wa))
  def gr() = RowMap(rmap.mapValues(_.gr))
  def grAvg() = gr avg
  def grWavg() = gr wavg

  def print(label: String = "test") = {
    val (key, value) = rmap.head
    val colRow = label +: value.getColSeq
    val rowList: Seq[Seq[String]] = rmap.map {
      case (k, v) => k.toString() +: v.getSeq
    }(collection.breakOut): Seq[Seq[String]]
    val table: Seq[Seq[String]] = colRow +: rowList
    println(Tabulator.format(table))
  }
}
// use trait?
class RowMapBuilder() {
  protected val map: RowMap = new RowMap
  def calcRow(label: String, row: Row): Row = map.add(label, row)
  def getRow(label: String) = map.getRow(label)
  def getMap = map
}

class FinFactRowMapBuilder(factList: java.util.List[FinFact]) extends RowMapBuilder {
  def exstractRow(label: String, methodName: String): Row = this.map.add(label, Row(factList, methodName))
}

// do i want this kind of equality? if you add the same day.. it overrides one alreayd aded.. is this ok??? maybe not a case class.....
// also, when we make a "indxed" cell w/ lable .. it's not longer equal to non indexed cell.. this could be wrong..
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
case class Cell(column: ColIndex, value: table.Value) {
  override def toString: String = value.getOrElse("-").toString //.formatted("%1.0f")
}

case class Row(row: TreeMap[ColIndex, Cell], desc: String) {

  def plus(that: Row) = applyZipFn(that, addRowTuple, this.desc + "+" + that.desc)
  def plus(in: Double) = transform(in, addRowTuple)

  def minus(that: Row) = applyZipFn(that, subRowTuple, this.desc + "-" + that.desc)
  def minus(in: Double) = transform(in, subRowTuple)

  def mult(that: Row) = applyZipFn(that, multRowTuple, this.desc + "*" + that.desc)
  def mult(in: Double) = transform(in, multRowTuple)

  def div(that: Row) = applyZipFn(that, divRowTuple, this.desc + "/" + that.desc)
  def div(in: Double) = transform(in, divRowTuple)

  def pow(that: Row) = applyZipFn(that, powRowTuple, this.desc + "/" + that.desc)
  def pow(in: Double) = transform(in, powRowTuple)

  def incDec() = calc(increaseDecrease, "incDec(" + this.desc + ")")
  def gr() = calc(growthRate, "gr(" + this.desc + ")")
  def avg() = average;
  def ttm() = trailingFourPeriods;

  // scala api
  def +(that: Row) = plus(that)
  def +(in: Double) = transform(in, addRowTuple)
  def -(that: Row) = minus(that)
  def -(in: Double) = transform(in, subRowTuple)
  def *(that: Row) = mult(that)
  def *(in: Double) = transform(in, multRowTuple)
  def /(that: Row) = div(that)
  def /(in: Double) = transform(in, divRowTuple)
  def ^(that: Row) = pow(that)
  def ^(in: Double) = transform(in, powRowTuple)

  override def toString: String = desc + ":" + row

  // todo: shoudl be ableo to get X peroids Back..

  def getLastValue = getValueAt(0)

  def getValueAt(index: Int): Option[Double] = {
    val seq = row.values.map(_.value)(collection.breakOut): Seq[Option[Double]]
    val calcedIndex = (seq.size - index) - 1
    if (calcedIndex < 0) None else seq(calcedIndex)
  }

  def getColumns: java.util.Set[ColIndex] = row.keySet

  def getValues = row.values.map(_.value.getOrElse(Double.NaN)).toArray

  def getSeq: Seq[String] = row.values.map(_.toString)(collection.breakOut): Seq[String]

  def getColSeq: Seq[String] = row.keySet.map(_.label)(collection.breakOut): Seq[String]

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

  private def powRowTuple(pair: table.ValuePair): table.Value = {
    pair match {
      case (Some(l), Some(r)) => Some(math.pow(l, r))
      case _ => None
    }
  }

  def transform(number: Double, fn: (table.ValuePair => table.Value)) = {
    val tm: TreeMap[ColIndex, Cell] = this.row.map {
      case (k, v) =>
        val tup = (v.value, Option(number))
        val result = fn(tup)
        (k, Cell(k, result))
    }

    Row(tm, "trans")
  }

  def applyZipFn(that: Row, fn: (table.ValuePair) => table.Value, fnDesc: String) = {

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

  def calc(fn: (table.ValuePair) => table.Value, fnDesc: String): Row = {
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
        val colIndex =indexedCell(cell)
        tm += colIndex -> Cell(colIndex, Some((curVal + state._2) / state._1))

        (state._1 + 1, state._2 + curVal)
      })
    Row(tm, "avg(" + this.desc + ")")
  }

  private def indexedCell(cell: Cell) = {
    val index = row.keys.toSeq.indexOf(cell.column)
    val yearsBack: String = (index - (row.keys.size - 1)).abs.toString
    ColIndex(yearsBack, cell.column.date)
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

  /**
   * TODO: refactor this!
   */
  def wa: Row = {
    var tm = TreeMap[ColIndex, Cell]()

    val doubleList = this.row.values.map(_.value.getOrElse(0D))(collection.breakOut): List[Double]
    val wAvgList = wAvg(doubleList)
    var x = 0
    for ((k, v) <- this.row) {
       val colIndex =indexedCell(v)
      tm += colIndex -> Cell(colIndex, Some(wAvgList(x)))
      x = x + 1
    }

    Row(tm, "wavg(" + this.desc + ")")
  }

  def wAvg(x: List[Double]): List[Double] = {
    def loop(inner: List[Double], accum: List[Double]): List[Double] = {
      val result = accum :+ weightAvg(inner)
      val tail = inner.tail
      if (tail.isEmpty) result else loop(tail, result)
    }
    loop(x, List())
  }

  def weightAvg(x: List[Double]): Double = {
    x.foldLeft((1, 0D))((tup, li) => (tup._1 + 1, tup._2 + li * tup._1))._2 / (1 to x.size).sum
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

object Tabulator {
  def format(table: Seq[Seq[Any]]) = table match {
    case Seq() => ""
    case _ =>
      val sizes = for (row <- table) yield (for (cell <- row) yield if (cell == null) 0 else cell.toString.length)
      val colSizes = for (col <- sizes.transpose) yield col.max
      val rows = for (row <- table) yield formatRow(row, colSizes)
      formatRows(rowSeparator(colSizes), rows)
  }

  def formatRows(rowSeparator: String, rows: Seq[String]): String = (
    rowSeparator ::
    rows.head ::
    rowSeparator ::
    rows.tail.toList :::
    rowSeparator ::
    List()).mkString("\n")

  def formatRow(row: Seq[Any], colSizes: Seq[Int]) = {
    val cells = (for ((item, size) <- row.zip(colSizes)) yield if (size == 0) "" else ("%" + size + "s").format(item))
    cells.mkString("|", "|", "|")
  }

  def rowSeparator(colSizes: Seq[Int]) = colSizes map { "-" * _ } mkString ("+", "+", "+")
}


