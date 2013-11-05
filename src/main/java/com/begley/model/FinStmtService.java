package com.begley.model;

import java.util.List;

import org.joda.time.LocalDate;

public interface FinStmtService {
	
	public List<IncomeStatement> findAllAnnual(Long assetId);
	
	public List<IncomeStatement> findAnnualUtilDate(Long assetId,LocalDate maxDate);

}
