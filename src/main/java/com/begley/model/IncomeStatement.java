package com.begley.model;

import java.util.Date;

import org.joda.time.LocalDate;

import com.begley.table.FinFact;

public class IncomeStatement implements FinFact {

	private Date date;

	private Long revenue;
	private Long costOfGoodsSold;

	private Long operatingEbitda;
	private Long depAmort;
	private Long interestExpense;

	private Long otherIncomeExpense;

	private Long incomeTaxBenifit;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getOperatingEbitda() {
		return operatingEbitda;
	}

	public void setOperatingEbitda(Long operatingEbitda) {
		this.operatingEbitda = operatingEbitda;
	}

	public Long getDepAmort() {
		return depAmort;
	}

	public void setDepAmort(Long depAmort) {
		this.depAmort = depAmort;
	}

	public Long getInterestExpense() {
		return interestExpense;
	}

	public void setInterestExpense(Long interestExpense) {
		this.interestExpense = interestExpense;
	}

	public Long getOtherIncomeExpense() {
		return otherIncomeExpense;
	}

	public void setOtherIncomeExpense(Long otherIncomeExpense) {
		this.otherIncomeExpense = otherIncomeExpense;
	}

	public Long getIncomeTaxBenifit() {
		return incomeTaxBenifit;
	}

	public void setIncomeTaxBenifit(Long incomeTaxBenifit) {
		this.incomeTaxBenifit = incomeTaxBenifit;
	}

	public Long getRevenue() {
		return revenue;
	}

	public void setRevenue(Long revenue) {
		this.revenue = revenue;
	}

	public Long getCostOfGoodsSold() {
		return costOfGoodsSold;
	}

	public void setCostOfGoodsSold(Long costOfGoodsSold) {
		this.costOfGoodsSold = costOfGoodsSold;
	}

	@Override
	public LocalDate getLocalDate() {
		return new LocalDate(date);
	}

}
