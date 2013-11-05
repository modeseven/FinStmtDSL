package com.begley.model;

import org.joda.time.LocalDate;

import com.begley.table.RowMap;

public interface FactService {
	
	public RowMap findIncomeStmt(Long assetId,LocalDate MaxDate,String calc); // other params?

}
