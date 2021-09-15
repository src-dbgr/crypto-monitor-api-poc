package com.sam.coin.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CoinTest {

	public static String testJson = "{\n" + "	\"symbol\": \"BTC/EUR\",\n" + "	\"timestamp\": 1597610876174,\n"
			+ "	\"datetime\":\n" + "	\"2020-08-16T20:47:56.174Z\",\n" + "	\"high\": 10150,\n"
			+ "	\"low\": 9935.65,\n" + "	\"bid\": 10043.58,\n" + "	\"ask\": 10044.78,\n"
			+ "	\"vwap\": 10050.49559764,\n" + "	\"close\": 10043.58,\n" + "	\"last\": 10043.58,\n"
			+ "	\"baseVolume\": 166.453966,\n" + "	\"quoteVolume\": 1672944.85249286,\n" + "	\"info\": {\n"
			+ "		\"symbol\": \"BTCEUR\",\n" + "		\"priceChange\": \"-31.94000000\",\n"
			+ "		\"priceChangePercent\":\n" + "		\"-0.317\",\n"
			+ "		\"weightedAvgPrice\": \"10050.49559764\",\n" + "		\"prevClosePrice\":\n"
			+ "		\"10070.00000000\",\n" + "		\"lastPrice\": \"10043.58000000\",\n"
			+ "		\"lastQty\": \"0.00100000\",\n" + "		\"bidPrice\": \"10043.58000000\",\n"
			+ "		\"bidQty\": \"0.04978300\",\n" + "		\"askPrice\":\n" + "		\"10044.78000000\",\n"
			+ "		\"askQty\": \"0.00118600\",\n" + "		\"openPrice\": \"10075.52000000\",\n"
			+ "		\"highPrice\": \"10150.00000000\",\n" + "		\"lowPrice\": \"9935.65000000\",\n"
			+ "		\"volume\":\n" + "		\"166.45396600\",\n" + "		\"quoteVolume\": \"1672944.85249286\",\n"
			+ "		\"openTime\": 1597524476174,\n" + "		\"closeTime\": 1597610876174,\n"
			+ "		\"firstId\": 997030,\n" + "		\"lastId\": 1003318,\n" + "		\"count\":\n" + "		6289\n"
			+ "	}\n" + "}";
	
	@Test
	public void test() throws JsonMappingException, JsonProcessingException {
//		ObjectMapper mapper = new ObjectMapper();
//		Coin coin = mapper.readerFor(Coin.class).readValue(testJson);
//		String writeValueAsString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(coin);
//		assertThat(coin.getId()).isNotNull();
//		assertThat(coin.getSymbol()).isEqualTo("BTC/EUR");
//		assertThat(coin.getInfo().getSymbol()).isEqualTo("BTCEUR");
//		assertThat(coin.getInfo().getWeightedAvgPrice()).isInstanceOf(BigDecimal.class);
//		System.out.println(writeValueAsString);
	}

}
