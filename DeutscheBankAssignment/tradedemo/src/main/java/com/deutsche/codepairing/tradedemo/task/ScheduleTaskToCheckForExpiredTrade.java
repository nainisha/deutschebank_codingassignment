package com.deutsche.codepairing.tradedemo.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import com.deutsche.codepairing.tradedemo.model.TradeDetail;

public class ScheduleTaskToCheckForExpiredTrade implements Runnable {
	Map<String, TradeDetail> recordsToCheck;

	public ScheduleTaskToCheckForExpiredTrade() {

	}

	public ScheduleTaskToCheckForExpiredTrade(Map<String, TradeDetail> recordsToCheck) {
		this.recordsToCheck = recordsToCheck;
	}

	@Override
	public void run() {
		System.out.println("Schedule task started to check and update expired trade ");
		
		for (Map.Entry<String, TradeDetail> entry : recordsToCheck.entrySet()) {
			try {
			TradeDetail tradeDetail = entry.getValue();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate maturityDate = LocalDate.parse(tradeDetail.getMaturityDate(), formatter);
			if (maturityDate.isBefore(LocalDate.now())) {
				tradeDetail.setExpired('Y');
			}
			}catch (DateTimeParseException e) {
				System.out.println("Unable to parse Maturity Date " + e.getMessage());
			}
			
		}
		
		System.out.println("Schedule task completed");

	}

}
