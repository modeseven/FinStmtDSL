package a

import org.junit._
import Assert._
import com.begley.table.ColIndex
import org.joda.time.LocalDate
import com.begley.table._
import scala.collection.immutable.TreeMap


class Tests {

  @Test def sample() {

    val format = new java.text.SimpleDateFormat("mm/dd/yyyy")

    def date(dateStr: String) = {
      LocalDate.fromDateFields(format.parse(dateStr))
    }

    val colIndex1 = ColIndex("1", date("12/31/2010"))

    val colIndex2 = ColIndex("2", date("12/31/2011"))

    val colIndex3 = ColIndex("3", date("12/31/2012"))

    val colIndex4 = ColIndex("4", date("12/31/2013"))

    val colIndex5 = ColIndex("5", date("12/31/2014"))
    
    val rowIndex1 = RowIndex("Revenue")  
    
     val revenueRow = Row(TreeMap(
   colIndex1 -> Cell(rowIndex1,colIndex1,Some(150D)),
   colIndex2 -> Cell(rowIndex1,colIndex2,Some(450D)),
   colIndex3 -> Cell(rowIndex1,colIndex3,Some(500D)),
   colIndex4 -> Cell(rowIndex1,colIndex4,Some(100D)),
   colIndex5 -> Cell(rowIndex1,colIndex5,Some(120D))
   )) 
   
   val revenueIncDec = revenueRow.incDec
   
    println("x is?:" + revenueIncDec)
    
    
    assertEquals(42, 6 * 7)
  }
}
