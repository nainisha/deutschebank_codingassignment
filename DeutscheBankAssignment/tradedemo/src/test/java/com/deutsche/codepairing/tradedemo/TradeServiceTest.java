package com.deutsche.codepairing.tradedemo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.deutsche.codepairing.tradedemo.model.TradeDetail;
import com.deutsche.codepairing.tradedemo.service.TradeService;

@RunWith(MockitoJUnitRunner.class)
public class TradeServiceTest {
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	@InjectMocks
	TradeService service = new TradeService();
	
	@Test
	public void testWithLowerVersion() {
		
		//when
		service.addTrade(new TradeDetail("T2", 2, "CP-2", "B1", "20/05/2021", "04/11/2021", 'N'));
		service.addTrade(new TradeDetail("T2", 1, "CP-1", "B1", "20/05/2021", "04/11/2021", 'N'));
		//then
		assertEquals(1, service.getTradeDetailMap().size());
	}

	@Test
	public void testWithIncorrectMaturityDate() {
		// Given
		System.setOut(new PrintStream(outputStreamCaptor));
		// when
		service.addTrade(new TradeDetail("T1", 1, "CP-1", "B1", "10/04/2021", "04/11/2021", 'N'));

		// then
		assertEquals(0, service.getTradeDetailMap().size());
		Assert.assertEquals("Maturity Date is not correct", outputStreamCaptor.toString().trim());
	}
	
	@Test
	public void testWithSameVersion() {
		TradeDetail t1 = new TradeDetail("T1", 1, "CP-2", "B2", "11/07/2021", "04/11/2021", 'N');
		// when
		service.addTrade(new TradeDetail("T1", 1, "CP-1", "B1", "11/06/2021", "04/11/2021", 'N'));
		service.addTrade(new TradeDetail("T1", 1, "CP-2", "B2", "11/07/2021", "04/11/2021", 'N'));

		// then
		assertEquals(1, service.getTradeDetailMap().size());
		Map<String, TradeDetail> tradeRecord = service.getTradeDetailMap();
		assertThat(t1).isEqualToComparingFieldByField(tradeRecord.get("T1"));

	}


	@Test
	public void testWithCorrectTrade() {
		
		TradeDetail t1 = new TradeDetail("T2", 2, "CP-2", "B1", "20/05/2021", "04/11/2021", 'N');
		service.addTrade(new TradeDetail("T2", 2, "CP-2", "B1", "20/05/2021", "04/11/2021", 'N'));
		service.addTrade(new TradeDetail("T3", 1, "CP-1", "B1", "20/06/2021", "04/11/2021", 'N'));
		assertEquals(2, service.getTradeDetailMap().size());
		Map<String, TradeDetail> tradeRecord = service.getTradeDetailMap();
		assertThat(t1).isEqualToComparingFieldByField(tradeRecord.get("T2"));
	}
}
