package com.begley.model;

import java.util.Date;

import org.joda.time.LocalDate;
import org.joda.time.Days;

import com.begley.table.FinFact;

public class Projection implements FinFact {

	private Date date;

	private Long revenue;
	private Double revenueGr;
	private Long ebitda;
	private Double ebitdaGr;
	private Long depAmort;
	private Double depAmortGr;
	private Long capEx;
	private Double capExGr;
	private Double nolUtil;
	private Long incDecWc;
	private Double incDecWcGr;
	
	private Double peroidsBeyondValDate; 

	public void calc(Long lastRevenue,Date valDate) { 
		LocalDate valDateLd = new LocalDate(valDate);
		peroidsBeyondValDate = (double) Days.daysBetween(valDateLd, getLocalDate()).getDays()/365;

		if (lastRevenue != null && revenueGr != null) {
			revenue = new Double(lastRevenue * (1 + revenueGr)).longValue();
		}	

		if (revenue != null) {
			
			if(lastRevenue != null && revenueGr == null){
				revenueGr = ((double) revenue - (double) lastRevenue) / (double) lastRevenue;
			}

			if (ebitdaGr != null) {
				ebitda = new Double(revenue * ebitdaGr).longValue();
			}

			if (depAmort != null) {
				depAmortGr = (double) depAmort / revenue;
			}

			if (capEx != null) {
				capExGr = (double) capEx / revenue;
			}

			if (depAmortGr != null) {
				depAmort = new Double(revenue * depAmortGr).longValue();
			}

			if (capExGr != null) {
				capEx = new Double(revenue * capExGr).longValue();
			}

			if (incDecWcGr != null) {
				incDecWc = new Double(revenue * incDecWcGr).longValue();
			}

		}

	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getRevenue() {
		return revenue;
	}

	public void setRevenue(Long revenue) {
		this.revenue = revenue;
	}

	public Double getRevenueGr() {
		return revenueGr;
	}

	public void setRevenueGr(Double revenueGr) {
		this.revenueGr = revenueGr;
	}

	public Long getEbitda() {
		return ebitda;
	}

	public void setEbitda(Long ebitda) {
		this.ebitda = ebitda;
	}

	public Double getEbitdaGr() {
		return ebitdaGr;
	}

	public void setEbitdaGr(Double ebitdaGr) {
		this.ebitdaGr = ebitdaGr;
	}

	public Long getDepAmort() {
		return depAmort;
	}

	public void setDepAmort(Long depAmort) {
		this.depAmort = depAmort;
	}

	public Double getDepAmortGr() {
		return depAmortGr;
	}

	public void setDepAmortGr(Double depAmortGr) {
		this.depAmortGr = depAmortGr;
	}

	public Long getCapEx() {
		return capEx;
	}

	public void setCapEx(Long capEx) {
		this.capEx = capEx;
	}

	public Double getCapExGr() {
		return capExGr;
	}

	public void setCapExGr(Double capExGr) {
		this.capExGr = capExGr;
	}

	public Double getNolUtil() {
		return nolUtil;
	}

	public void setNolUtil(Double nolUtil) {
		this.nolUtil = nolUtil;
	}

	public Long getIncDecWc() {
		return incDecWc;
	}

	public void setIncDecWc(Long incDecWc) {
		this.incDecWc = incDecWc;
	}

	public Double getIncDecWcGr() {
		return incDecWcGr;
	}

	public void setIncDecWcGr(Double incDecWcGr) {
		this.incDecWcGr = incDecWcGr;
	}
	
	

	public Double getPeroidsBeyondValDate() {
		return peroidsBeyondValDate;
	}

	@Override
	public String toString() {
		return "Projection [date=" + date + ", revenue=" + revenue
				+ ", revenueGr=" + revenueGr + ", ebitda=" + ebitda
				+ ", ebitdaGr=" + ebitdaGr + ", depAmort=" + depAmort
				+ ", depAmortGr=" + depAmortGr + ", capEx=" + capEx
				+ ", capExGr=" + capExGr + ", nolUtil=" + nolUtil
				+ ", incDecWc=" + incDecWc + ", incDecWcGr=" + incDecWcGr + "]";
	}

	@Override
	public LocalDate getLocalDate() {
		return new LocalDate(date);
	}

}
