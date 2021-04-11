package com.deutsche.codepairing.tradedemo.test;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.deutsche.codepairing.tradedemo.model.TradeDetail;
import com.deutsche.codepairing.tradedemo.service.TradeService;
import com.deutsche.codepairing.tradedemo.task.ScheduleTaskToCheckForExpiredTrade;

public class TestTrade {
	public static void main(String[] args) {

		TradeService service = new TradeService();
		service.addTrade(new TradeDetail("T1", 1, "CP-1", "B1", "10/04/2021", "14/03/2015", 'N'));
		service.addTrade(new TradeDetail("T2", 2, "CP-2", "B1", "20/05/2021", "14/03/2015", 'N'));
		service.addTrade(new TradeDetail("T2", 1, "CP-1", "B1", "20/05/2021", "14/03/2015", 'N'));
		service.addTrade(new TradeDetail("T3", 3, "CP-3", "B2", "20/05/2021", "14/03/2015", 'Y'));
		Map<String, TradeDetail> map = service.getTradeDetailMap();
		System.out.println("=============TradeDetails==========");
		for (Map.Entry<String, TradeDetail> entry : map.entrySet()) {
			TradeDetail tradeDetail = entry.getValue();
			System.out.println("Key = " + entry.getKey() + ", Version:" + tradeDetail.getVersion() + " MaturityDate "
					+ tradeDetail.getMaturityDate() + " Expired " + tradeDetail.getExpired());
		}

		new TestTrade().checkExpiredTrade(map);

	}

	private void checkExpiredTrade(Map<String, TradeDetail> map) {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
		ZonedDateTime nextRun = now.withHour(12).withMinute(0).withSecond(0);
		if (now.compareTo(nextRun) > 0)
			nextRun = nextRun.plusDays(1);

		Duration duration = Duration.between(now, nextRun);
		long initalDelay = duration.getSeconds();

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new ScheduleTaskToCheckForExpiredTrade(map), initalDelay, TimeUnit.DAYS.toSeconds(1),
				TimeUnit.SECONDS);
	}

}
