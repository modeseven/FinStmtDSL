package com.begley.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;


public class FinStmtServiceLocal implements FinStmtService {
	
	private List<IncomeStatement> isFacts = new ArrayList();
	

	public FinStmtServiceLocal() {
		isFacts.add(is1());
		isFacts.add(is2());
		isFacts.add(is3());
		isFacts.add(is4());
		isFacts.add(is5());
	}
	@Override
	public List<IncomeStatement> findAllAnnual(Long assetId) {
		return isFacts;
	}

	@Override
	public List<IncomeStatement> findAnnualUtilDate(Long assetId,LocalDate date) {
		 List<IncomeStatement> result = new ArrayList();
		 
		 for(IncomeStatement is: isFacts){
			 if (is.getLocalDate().isBefore(date) || is.getLocalDate().isEqual(date)){
				 result.add(is);
			 }
		 }
		 
		return result;
	}
	
	private IncomeStatement is1(){		
		IncomeStatement is = new IncomeStatement();
		is.setDate(makeDate("12/31/2007"));
		is.setRevenue(2751274L);
		is.setCostOfGoodsSold(1800374L);
		is.setOperatingEbitda(7659901L);
		is.setDepAmort(122985L);
		is.setInterestExpense(210000L);
		is.setOtherIncomeExpense(null);
		is.setIncomeTaxBenifit(null)	;	
		return is;
	}
	
	private IncomeStatement is2(){		
		IncomeStatement is = new IncomeStatement();
		is.setDate(makeDate("12/31/2008"));
		is.setRevenue(20497701L);
		is.setCostOfGoodsSold(12254620L);
		is.setOperatingEbitda(13165359L);
		is.setDepAmort(243420L);
		is.setInterestExpense(450000L);
		is.setOtherIncomeExpense(null);
		is.setIncomeTaxBenifit(3510L)	;	
		return is;
	}
	
	private IncomeStatement is3(){		
		IncomeStatement is = new IncomeStatement();
		is.setDate(makeDate("12/31/2009"));
		is.setRevenue(37919170L);
		is.setCostOfGoodsSold(18139088L);
		is.setOperatingEbitda(19179472L);
		is.setDepAmort(482222L);
		is.setInterestExpense(364667L);
		is.setOtherIncomeExpense(113L);
		is.setIncomeTaxBenifit(15800L)	;	
		return is;
	}
	
	private IncomeStatement is4(){		
		IncomeStatement is = new IncomeStatement();
		is.setDate(makeDate("12/31/2010"));
		is.setRevenue(63057498L);
		is.setCostOfGoodsSold(32072733L);
		is.setOperatingEbitda(26023577L);
		is.setDepAmort(511358L);
		is.setInterestExpense(329621L);
		is.setOtherIncomeExpense(10178L);
		is.setIncomeTaxBenifit(192609L)	;	
		return is;
	}
	
	private IncomeStatement is5(){		
		IncomeStatement is = new IncomeStatement();
		is.setDate(makeDate("12/31/2011"));
		is.setRevenue(81870001L);
		is.setCostOfGoodsSold(50768296L);
		is.setOperatingEbitda(24487069L);
		is.setDepAmort(396731L);
		is.setInterestExpense(251000L);
		is.setOtherIncomeExpense(null);
		is.setIncomeTaxBenifit(1580929L)	;	
		return is;
	}
	
	private Date makeDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
