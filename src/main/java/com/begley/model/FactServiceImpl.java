package com.begley.model;

import java.util.List;

import org.joda.time.LocalDate;

import com.begley.table.FinFactRowMapBuilder;
import com.begley.table.Row;
import com.begley.table.RowMap;
import com.begley.table.RowMapBuilder;


public class FactServiceImpl implements FactService {
	
	private FinStmtService finStmtService = new FinStmtServiceLocal();

	// prolly return Optional?
	@Override
	public RowMap findIncomeStmt(Long assetId, LocalDate maxDate, String calc) {
		
		// might rought time peroidd? normalized /unadjusted?
		List<IncomeStatement> incs = finStmtService.findAnnualUtilDate(assetId, maxDate); // testers and validators.
		
		RowMap fact = create(incs, true);
		
		// case statments here to route proper calc?
		
		return fact;
	}
	
	
	private RowMap create(List<IncomeStatement> incs,boolean cs){
		FinFactRowMapBuilder incStmmtRb = new FinFactRowMapBuilder(incs);
		
		Row revenue = incStmmtRb.addExstractedRow("Revenue", "getRevenue");
		Row costOfGoodsSold = incStmmtRb.addExstractedRow("Cost of Goods Sold", "getCostOfGoodsSold"); 
		Row grossProfit = incStmmtRb.addRow("Gross Profit", revenue.minus(costOfGoodsSold));
		Row operatingExpenseEbitda = incStmmtRb.addExstractedRow("Operating Expense Before ITDA", "getOperatingEbitda");  
		Row operatingEbitda =incStmmtRb.addRow("Operating EITDA", grossProfit.minus(operatingExpenseEbitda));
		
		if(!cs){
			return incStmmtRb.getMap();
		} 
		
		RowMapBuilder incStmtCsRb = new RowMapBuilder();		
		Row r = incStmtCsRb.addRow("Revenue", revenue.div(revenue));
		Row x = incStmtCsRb.addRow("Cost of godds",costOfGoodsSold.div(revenue));
		incStmtCsRb.addRow("gross Proffice",r.minus(x));
		
		return incStmtCsRb.getMap();

		
	}



}
