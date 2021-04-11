package com.deutsche.codepairing.tradedemo.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import com.deutsche.codepairing.tradedemo.customexception.TradeLowerVersion;
import com.deutsche.codepairing.tradedemo.model.TradeDetail;

public class TradeService {

	private Map<String, TradeDetail> tradeDetailMap = new HashMap<String, TradeDetail>();

	public Map<String, TradeDetail> getTradeDetailMap() {
		return tradeDetailMap;
	}

	public void addTrade(TradeDetail tradeDetail) {

		if (validateTrade(tradeDetail)) {
			tradeDetailMap.put(tradeDetail.getTradeId(), tradeDetail);
		}

	}

	private boolean validateTrade(TradeDetail tradeDetail) {
		if (tradeDetailMap.containsKey(tradeDetail.getTradeId())) {
			return isValidVersion(tradeDetail);
		} else {
			return isValidMaturityDate(tradeDetail);
		}

	}

	private boolean isValidVersion(TradeDetail tradeDetail) {
		if (tradeDetailMap.containsKey(tradeDetail.getTradeId())) {
			TradeDetail tradeDetailInDB = tradeDetailMap.get(tradeDetail.getTradeId());
			if (tradeDetailInDB.getVersion() <= tradeDetail.getVersion()) {
				return true;
			} else {
				try {
					throw new TradeLowerVersion(tradeDetail.getTradeId(), tradeDetail.getVersion(),
							tradeDetailInDB.getVersion());
				} catch (TradeLowerVersion e) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isValidMaturityDate(TradeDetail tradeDetail) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate maturityDate = LocalDate.parse(tradeDetail.getMaturityDate(), formatter);
			if (maturityDate.isBefore(LocalDate.now())) {
				System.out.println("Maturity Date is not correct");
				return false;
			}
			return true;
		} catch (DateTimeParseException e) {
			System.out.println("Unable to parse Maturity Date " + e.getMessage());
			return false;
		}
	}

}
