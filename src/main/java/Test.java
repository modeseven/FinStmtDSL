import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;

import com.begley.table.*;


public class Test implements FinFact {
	
	public static void main(String[] args){
		System.out.println("hi f");
		
		Test t = new Test();
		t.setVal(4L);
		
		List<FinFact> list = new ArrayList<FinFact>();
		list.add(t);
		
		Row r = Row.exstractRow(list, "getVal");
		
		r.average();
		System.out.println(r.getLastCell().get());
		
	
		
		Row.sum(r);
		
	
	}
	
	private Long val;
	private Date date;

	public Long getVal() {
		return val;
	}

	public void setVal(Long val) {
		this.val = val;
	}
	
	

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public LocalDate getDate() {
		return new LocalDate(date);
	}
	
	

}
