import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;


import com.begley.table.*;


public class Test implements FinFact {
	
	public static void main(String[] args) throws ParseException{
		System.out.println("hi f");
		
		
		String dt = "2008-01-01";  // Start date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(dt));
		c.add(Calendar.DATE, 1);  // number of days to add
		dt = sdf.format(c.getTime());  // dt is now the new date
		
		
		
		Test t = new Test();
		t.setVal(4L);
		t.setVal2(45L);
		t.setDate(new Date());
		
		
		
		Test t2 = new Test();
        t2.setVal2(46L);
    	t2.setDate(sdf.parse(dt));
		
		List<FinFact> list = new ArrayList<FinFact>();
		list.add(t);
		list.add(t2);
		
		Row r = Row.apply(list, "getVal");
		Row r2 = Row.apply(list, "getVal2");
		
		System.out.println("average" + r.avg());
		System.out.println(r2);
		System.out.println("years back" + r2.getColumIndices());
		
	
		
		System.out.println("wstf?" + Arrays.toString(r.getValues()));
		
	
		
		Row sumRow = Row.sum(r,r2,r.avg());
		
		Row sumAvg = sumRow.avg();
		System.out.println(sumRow);
		System.out.println(sumAvg);
		
	
	}
	
	private Long val;
	private Long val2;
	private Date date;

	public Long getVal() {
		return val;
	}

	public void setVal(Long val) {
		this.val = val;
	}
	
	
	
	

	public Long getVal2() {
		return val2;
	}

	public void setVal2(Long val2) {
		this.val2 = val2;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public LocalDate getDate() {
		return new LocalDate(date);
	}
	
	

}
