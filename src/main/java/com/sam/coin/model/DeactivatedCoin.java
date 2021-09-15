package com.sam.coin.model;

import java.util.UUID;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeactivatedCoin {

	@JsonProperty("id")
	private final UUID id;

	@NotBlank
	@JsonProperty("symbol")
	private String symbol;

	@NotNull
	@JsonProperty("timestamp")
	private long timestamp;

	@JsonProperty("datetime")
	private String datetime;

	@JsonProperty("high")
	private BigDecimal high;
	@JsonProperty("low")
	private BigDecimal low;
	@JsonProperty("bid")
	private BigDecimal bid;
	@JsonProperty("ask")
	private BigDecimal ask;
	@JsonProperty("vwap")
	private BigDecimal vwap;
	@JsonProperty("close")
	private BigDecimal close;
	@JsonProperty("last")
	private BigDecimal last;
	@JsonProperty("baseVolume")
	private BigDecimal baseVolume;
	@JsonProperty("quoteVolume")
	private BigDecimal quoteVolume;
	@JsonProperty("info")
	private Info info;

	public DeactivatedCoin() {
		this.id = UUID.randomUUID();
	}
	
	public DeactivatedCoin(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public String getSymbol() {
		return symbol;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getDatetime() {
		return datetime;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public BigDecimal getBid() {
		return bid;
	}

	public BigDecimal getAsk() {
		return ask;
	}

	public BigDecimal getVwap() {
		return vwap;
	}

	public BigDecimal getClose() {
		return close;
	}

	public BigDecimal getLast() {
		return last;
	}

	public BigDecimal getBaseVolume() {
		return baseVolume;
	}

	public BigDecimal getQuoteVolume() {
		return quoteVolume;
	}

	public Info getInfo() {
		return info;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}

	public void setAsk(BigDecimal ask) {
		this.ask = ask;
	}

	public void setVwap(BigDecimal vwap) {
		this.vwap = vwap;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	public void setLast(BigDecimal last) {
		this.last = last;
	}

	public void setBaseVolume(BigDecimal baseVolume) {
		this.baseVolume = baseVolume;
	}

	public void setQuoteVolume(BigDecimal quoteVolume) {
		this.quoteVolume = quoteVolume;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public class Info {
		@JsonProperty("symbol")
		private String symbol;
		@JsonProperty("priceChange")
		private BigDecimal priceChange;
		@JsonProperty("priceChangePercent")
		private BigDecimal priceChangePercent;
		@JsonProperty("weightedAvgPrice")
		private BigDecimal weightedAvgPrice;
		@JsonProperty("prevClosePrice")
		private BigDecimal prevClosePrice;
		@JsonProperty("lastPrice")
		private BigDecimal lastPrice;
		@JsonProperty("lastQty")
		private BigDecimal lastQty;
		@JsonProperty("bidPrice")
		private BigDecimal bidPrice;
		@JsonProperty("bidQty")
		private BigDecimal bidQty;
		@JsonProperty("askPrice")
		private BigDecimal askPrice;
		@JsonProperty("askQty")
		private BigDecimal askQty;
		@JsonProperty("openPrice")
		private BigDecimal openPrice;
		@JsonProperty("highPrice")
		private BigDecimal highPrice;
		@JsonProperty("lowPrice")
		private BigDecimal lowPrice;
		@JsonProperty("volume")
		private BigDecimal volume;
		@JsonProperty("quoteVolume")
		private BigDecimal quoteVolume;
		@JsonProperty("openTime")
		private long openTime;
		@JsonProperty("closeTime")
		private long closeTime;
		@JsonProperty("firstId")
		private long firstID;
		@JsonProperty("lastId")
		private long lastID;
		@JsonProperty("count")
		private long count;

		public String getSymbol() {
			return symbol;
		}

		public BigDecimal getPriceChange() {
			return priceChange;
		}

		public BigDecimal getPriceChangePercent() {
			return priceChangePercent;
		}

		public BigDecimal getWeightedAvgPrice() {
			return weightedAvgPrice;
		}

		public BigDecimal getPrevClosePrice() {
			return prevClosePrice;
		}

		public BigDecimal getLastPrice() {
			return lastPrice;
		}

		public BigDecimal getLastQty() {
			return lastQty;
		}

		public BigDecimal getBidPrice() {
			return bidPrice;
		}

		public BigDecimal getBidQty() {
			return bidQty;
		}

		public BigDecimal getAskPrice() {
			return askPrice;
		}

		public BigDecimal getAskQty() {
			return askQty;
		}

		public BigDecimal getOpenPrice() {
			return openPrice;
		}

		public BigDecimal getHighPrice() {
			return highPrice;
		}

		public BigDecimal getLowPrice() {
			return lowPrice;
		}

		public BigDecimal getVolume() {
			return volume;
		}

		public BigDecimal getQuoteVolume() {
			return quoteVolume;
		}

		public long getOpenTime() {
			return openTime;
		}

		public long getCloseTime() {
			return closeTime;
		}

		public long getFirstID() {
			return firstID;
		}

		public long getLastID() {
			return lastID;
		}

		public long getCount() {
			return count;
		}

		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}

		public void setPriceChange(BigDecimal priceChange) {
			this.priceChange = priceChange;
		}

		public void setPriceChangePercent(BigDecimal priceChangePercent) {
			this.priceChangePercent = priceChangePercent;
		}

		public void setWeightedAvgPrice(BigDecimal weightedAvgPrice) {
			this.weightedAvgPrice = weightedAvgPrice;
		}

		public void setPrevClosePrice(BigDecimal prevClosePrice) {
			this.prevClosePrice = prevClosePrice;
		}

		public void setLastPrice(BigDecimal lastPrice) {
			this.lastPrice = lastPrice;
		}

		public void setLastQty(BigDecimal lastQty) {
			this.lastQty = lastQty;
		}

		public void setBidPrice(BigDecimal bidPrice) {
			this.bidPrice = bidPrice;
		}

		public void setBidQty(BigDecimal bidQty) {
			this.bidQty = bidQty;
		}

		public void setAskPrice(BigDecimal askPrice) {
			this.askPrice = askPrice;
		}

		public void setAskQty(BigDecimal askQty) {
			this.askQty = askQty;
		}

		public void setOpenPrice(BigDecimal openPrice) {
			this.openPrice = openPrice;
		}

		public void setHighPrice(BigDecimal highPrice) {
			this.highPrice = highPrice;
		}

		public void setLowPrice(BigDecimal lowPrice) {
			this.lowPrice = lowPrice;
		}

		public void setVolume(BigDecimal volume) {
			this.volume = volume;
		}

		public void setQuoteVolume(BigDecimal quoteVolume) {
			this.quoteVolume = quoteVolume;
		}

		public void setOpenTime(long openTime) {
			this.openTime = openTime;
		}

		public void setCloseTime(long closeTime) {
			this.closeTime = closeTime;
		}

		public void setFirstID(long firstID) {
			this.firstID = firstID;
		}

		public void setLastID(long lastID) {
			this.lastID = lastID;
		}

		public void setCount(long count) {
			this.count = count;
		}

		@Override
		public String toString() {
			return "Info [symbol=" + symbol + ", priceChange=" + priceChange + ", priceChangePercent="
					+ priceChangePercent + ", weightedAvgPrice=" + weightedAvgPrice + ", prevClosePrice="
					+ prevClosePrice + ", lastPrice=" + lastPrice + ", lastQty=" + lastQty + ", bidPrice=" + bidPrice
					+ ", bidQty=" + bidQty + ", askPrice=" + askPrice + ", askQty=" + askQty + ", openPrice="
					+ openPrice + ", highPrice=" + highPrice + ", lowPrice=" + lowPrice + ", volume=" + volume
					+ ", quoteVolume=" + quoteVolume + ", openTime=" + openTime + ", closeTime=" + closeTime
					+ ", firstID=" + firstID + ", lastID=" + lastID + ", count=" + count + "]";
		}

	}

	@Override
	public String toString() {
		return "Coin [id=" + id + ", symbol=" + symbol + ", timestamp=" + timestamp + ", datetime=" + datetime
				+ ", high=" + high + ", low=" + low + ", bid=" + bid + ", ask=" + ask + ", vwap=" + vwap + ", close="
				+ close + ", last=" + last + ", baseVolume=" + baseVolume + ", quoteVolume=" + quoteVolume + ", info="
				+ getInfo().toString() + "]";
	}

}
