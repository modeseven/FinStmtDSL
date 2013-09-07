package com.begley.table

import com.github.nscala_time.time.Imports._
import collection.mutable.Map
import collection.immutable.TreeMap

case class RowIndex(label: String)

case class ColIndex(label: String, date: LocalDate) {
  override def toString: String = label
}

object ColIndex {
  val format = new java.text.SimpleDateFormat("mm/dd/yyyy")

  def date(dateStr: String) = {
    LocalDate.fromDateFields(format.parse(dateStr))
  }

  implicit def colIndexOrdering: Ordering[ColIndex] = Ordering.fromLessThan(_.date isBefore _.date) //todo: read more about implicits?
  def apply(dateAsString: String) {
    new ColIndex(dateAsString, date(dateAsString))
  }

}

case class Cell(row: RowIndex, column: ColIndex, value: Option[Double]) {
  override def toString: String = column + ":" + value.getOrElse("-")
}

object Cell {
  def apply(cell: Cell, value: Option[Double]) = new Cell(cell.row, cell.column, value)
}

case class Row(row: TreeMap[ColIndex, Cell]) {

  private def addRowTuple(pair: (Option[Double], Option[Double])): Option[Double] = {
    pair match {
      case (Some(l), Some(r)) => Some(l + r)
      case (None, Some(r)) => Some(r)
      case (Some(l), None) => Some(l)
      case _ => None
    }
  }

  private def subRowTuple(pair: (Option[Double], Option[Double])): Option[Double] = {
    pair match {
      case (Some(l), Some(r)) => Some(l - r)
      case (None, Some(r)) => Some(0D - r)
      case (Some(l), None) => Some(l)
      case _ => None
    }
  }

  def +(that: Row) = applyZipFn(that, addRowTuple)
  def -(that: Row) = applyZipFn(that, subRowTuple)

  def applyZipFn(that: Row, fn: ((Option[Double], Option[Double])) => Option[Double]) = {

    val l = (this.row.values, that.row.values).zipped.map((top: Cell, bottom: Cell) => {
      val tup = (top.value, bottom.value)
      val sum = fn(tup)
      Cell(top, sum)
    })
    val m = l map { t => (t.column, t) } toMap //TODO: inefficient transformations?..
    val tm = TreeMap(m.toArray: _*)
    Row(tm)

  }

  def ttm(): Row = {
    var lm: Map[ColIndex, Cell] = Map() // TODO: HOW TO MAKE THIS IMMUATEBLE?

    row.values.foldLeft(Array(0D, 0D, 0D, 0D))(
      (state: Array[Double], cell: Cell) => {
        val newState = Array(cell.value.getOrElse(0D), state(0), state(1), state(2))
        lm(cell.column) = Cell(cell, Some(newState.reduceLeft(_ + _)))
        newState
      } // end hof
      )
    Row(TreeMap(lm.toSeq: _*))
  } // end ttm

  def calc(fn: ((Option[Double], Option[Double])) => Option[Double]): Row = {
    var lm: Map[ColIndex, Cell] = Map() // TODO: HOW TO MAKE THIS IMMUATEBLE?

    row.values.foldLeft(Option(0D))((i: Option[Double], cell: Cell) => {
      val incDec: Option[Double] = fn(i, cell.value)
      lm(cell.column) = Cell(cell, incDec)
      cell.value
    })
    Row(TreeMap(lm.toSeq: _*))
  }

  def average(): Row = {
    var lm: Map[ColIndex, Cell] = Map() // TODO: HOW TO MAKE THIS IMMUATEBLE?

    row.values.foldRight((1, 0D))(
      (cell: Cell, state: (Int, Double)) => {
        val curVal: Double = cell.value.getOrElse(0D)
        lm(cell.column) = Cell(cell, Some((curVal + state._2) / state._1))
        (state._1 + 1, state._2 + curVal)
      })
    Row(TreeMap(lm.toSeq: _*))
  }

}