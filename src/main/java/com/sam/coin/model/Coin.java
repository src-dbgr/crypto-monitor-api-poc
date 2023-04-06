package com.sam.coin.model;

import java.util.UUID;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Coin {

	@JsonProperty("id")
	private final UUID id;
	@NotNull
	@JsonProperty("timestamp")
	private Timestamp timestamp;
	@NotBlank
	@JsonProperty("symbol")
	private String symbol;
	@JsonProperty("coinId")
	private String coinId;
	@JsonProperty("coinName")
	private String coinName;
	@JsonProperty("priceEur")
	private BigDecimal priceEur;
	@JsonProperty("priceUsd")
	private BigDecimal priceUsd;
	@JsonProperty("priceBtc")
	private BigDecimal priceBtc;
	@JsonProperty("priceEth")
	private BigDecimal priceEth;
	@JsonProperty("marketCapEur")
	private BigDecimal marketCapEur;
	@JsonProperty("marketCapUsd")
	private BigDecimal marketCapUsd;
	@JsonProperty("marketCapBtc")
	private BigDecimal marketCapBtc;
	@JsonProperty("marketCapEth")
	private BigDecimal marketCapEth;
	@JsonProperty("totalVolumeEur")
	private BigDecimal totalVolumeEur;
	@JsonProperty("totalVolumeUsd")
	private BigDecimal totalVolumeUsd;
	@JsonProperty("totalVolumeBtc")
	private BigDecimal totalVolumeBtc;
	@JsonProperty("totalVolumeEth")
	private BigDecimal totalVolumeEth;
	@JsonProperty("twitterFollowers")
	private long twitterFollowers;
	@JsonProperty("redditAvgPosts48Hours")
	private BigDecimal redditAvgPosts48Hours;
	@JsonProperty("redditAvgComments48Hours")
	private BigDecimal redditAvgComments48Hours;
	@JsonProperty("redditAccountsActive48Hours")
	private BigDecimal redditAccountsActive48Hours;
	@JsonProperty("redditSubscribers")
	private long redditSubscribers;
	@JsonProperty("devForks")
	private long devForks;
	@JsonProperty("devStars")
	private long devStars;
	@JsonProperty("devTotalIssues")
	private long devTotalIssues;
	@JsonProperty("devClosedIssues")
	private long devClosedIssues;
	@JsonProperty("devPullRequestsMerged")
	private long devPullRequestsMerged;
	@JsonProperty("devPullRequestContributors")
	private long devPullRequestContributors;
	@JsonProperty("devCommitCount4Weeks")
	private long devCommitCount4Weeks;
	@JsonProperty("devCodeAdditions4Weeks")
	private long devCodeAdditions4Weeks;
	@JsonProperty("devCodeDeletions4Weeks")
	private long devCodeDeletions4Weeks;
	@JsonProperty("publicAlexaRank")
	private long publicAlexaRank;

	public Coin() {
		this.id = UUID.randomUUID();
	}
	
	public Coin(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	public BigDecimal getPriceEur() {
		return priceEur;
	}

	public void setPriceEur(BigDecimal priceEur) {
		this.priceEur = priceEur;
	}

	public BigDecimal getPriceUsd() {
		return priceUsd;
	}

	public void setPriceUsd(BigDecimal priceUsd) {
		this.priceUsd = priceUsd;
	}

	public BigDecimal getPriceBtc() {
		return priceBtc;
	}

	public void setPriceBtc(BigDecimal priceBtc) {
		this.priceBtc = priceBtc;
	}

	public BigDecimal getPriceEth() {
		return priceEth;
	}

	public void setPriceEth(BigDecimal priceEth) {
		this.priceEth = priceEth;
	}

	public BigDecimal getMarketCapEur() {
		return marketCapEur;
	}

	public void setMarketCapEur(BigDecimal marketCapEur) {
		this.marketCapEur = marketCapEur;
	}

	public BigDecimal getMarketCapUsd() {
		return marketCapUsd;
	}

	public void setMarketCapUsd(BigDecimal marketCapUsd) {
		this.marketCapUsd = marketCapUsd;
	}

	public BigDecimal getMarketCapBtc() {
		return marketCapBtc;
	}

	public void setMarketCapBtc(BigDecimal marketCapBtc) {
		this.marketCapBtc = marketCapBtc;
	}

	public BigDecimal getMarketCapEth() {
		return marketCapEth;
	}

	public void setMarketCapEth(BigDecimal marketCapEth) {
		this.marketCapEth = marketCapEth;
	}

	public BigDecimal getTotalVolumeEur() {
		return totalVolumeEur;
	}

	public void setTotalVolumeEur(BigDecimal totalVolumeEur) {
		this.totalVolumeEur = totalVolumeEur;
	}

	public BigDecimal getTotalVolumeUsd() {
		return totalVolumeUsd;
	}

	public void setTotalVolumeUsd(BigDecimal totalVolumeUsd) {
		this.totalVolumeUsd = totalVolumeUsd;
	}

	public BigDecimal getTotalVolumeBtc() {
		return totalVolumeBtc;
	}

	public void setTotalVolumeBtc(BigDecimal totalVolumeBtc) {
		this.totalVolumeBtc = totalVolumeBtc;
	}

	public BigDecimal getTotalVolumeEth() {
		return totalVolumeEth;
	}

	public void setTotalVolumeEth(BigDecimal totalVolumeEth) {
		this.totalVolumeEth = totalVolumeEth;
	}

	public long getTwitterFollowers() {
		return twitterFollowers;
	}

	public void setTwitterFollowers(long twitterFollowers) {
		this.twitterFollowers = twitterFollowers;
	}

	public BigDecimal getRedditAvgPosts48Hours() {
		return redditAvgPosts48Hours;
	}

	public void setRedditAvgPosts48Hours(BigDecimal redditAvgPosts48Hours) {
		this.redditAvgPosts48Hours = redditAvgPosts48Hours;
	}

	public BigDecimal getRedditAvgComments48Hours() {
		return redditAvgComments48Hours;
	}

	public void setRedditAvgComments48Hours(BigDecimal redditAvgComments48Hours) {
		this.redditAvgComments48Hours = redditAvgComments48Hours;
	}

	public BigDecimal getRedditAccountsActive48Hours() {
		return redditAccountsActive48Hours;
	}

	public void setRedditAccountsActive48Hours(BigDecimal redditAccountsActive48Hours) {
		this.redditAccountsActive48Hours = redditAccountsActive48Hours;
	}

	public long getRedditSubscribers() {
		return redditSubscribers;
	}

	public void setRedditSubscribers(long redditSubscribers) {
		this.redditSubscribers = redditSubscribers;
	}

	public long getDevForks() {
		return devForks;
	}

	public void setDevForks(long devForks) {
		this.devForks = devForks;
	}

	public long getDevStars() {
		return devStars;
	}

	public void setDevStars(long devStars) {
		this.devStars = devStars;
	}

	public long getDevTotalIssues() {
		return devTotalIssues;
	}

	public void setDevTotalIssues(long devTotalIssues) {
		this.devTotalIssues = devTotalIssues;
	}

	public long getDevClosedIssues() {
		return devClosedIssues;
	}

	public void setDevClosedIssues(long devClosedIssues) {
		this.devClosedIssues = devClosedIssues;
	}

	public long getDevPullRequestsMerged() {
		return devPullRequestsMerged;
	}

	public void setDevPullRequestsMerged(long devPullRequestsMerged) {
		this.devPullRequestsMerged = devPullRequestsMerged;
	}

	public long getDevPullRequestContributors() {
		return devPullRequestContributors;
	}

	public void setDevPullRequestContributors(long devPullRequestContributors) {
		this.devPullRequestContributors = devPullRequestContributors;
	}

	public long getDevCommitCount4Weeks() {
		return devCommitCount4Weeks;
	}

	public void setDevCommitCount4Weeks(long devCommitCount4Weeks) {
		this.devCommitCount4Weeks = devCommitCount4Weeks;
	}

	public long getDevCodeAdditions4Weeks() {
		return devCodeAdditions4Weeks;
	}

	public void setDevCodeAdditions4Weeks(long devCodeAdditions4Weeks) {
		this.devCodeAdditions4Weeks = devCodeAdditions4Weeks;
	}

	public long getDevCodeDeletions4Weeks() {
		return devCodeDeletions4Weeks;
	}

	public void setDevCodeDeletions4Weeks(long devCodeDeletions4Weeks) {
		this.devCodeDeletions4Weeks = devCodeDeletions4Weeks;
	}

	public long getPublicAlexaRank() {
		return publicAlexaRank;
	}

	public void setPublicAlexaRank(long publicAlexaRank) {
		this.publicAlexaRank = publicAlexaRank;
	}

	@Override
	public String toString() {
		return "Coin2 [id=" + id + ", timestamp=" + timestamp + ", symbol=" + symbol + ", coinId=" + coinId
				+ ", coinName=" + coinName + ", priceEur=" + priceEur + ", priceUsd=" + priceUsd + ", priceBtc="
				+ priceBtc + ", priceEth=" + priceEth + ", marketCapEur=" + marketCapEur + ", marketCapUsd="
				+ marketCapUsd + ", marketCapBtc=" + marketCapBtc + ", marketCapEth=" + marketCapEth
				+ ", totalVolumeEur=" + totalVolumeEur + ", totalVolumeUsd=" + totalVolumeUsd + ", totalVolumeBtc="
				+ totalVolumeBtc + ", totalVolumeEth=" + totalVolumeEth + ", twitterFollowers=" + twitterFollowers
				+ ", redditAvgPosts48Hours=" + redditAvgPosts48Hours + ", redditAvgComments48Hours="
				+ redditAvgComments48Hours + ", redditAccountsActive48Hours=" + redditAccountsActive48Hours
				+ ", redditSubscribers=" + redditSubscribers + ", devForks=" + devForks + ", devStars=" + devStars
				+ ", devTotalIssues=" + devTotalIssues + ", devClosedIssues=" + devClosedIssues
				+ ", devPullRequestsMerged=" + devPullRequestsMerged + ", devPullRequestContributors="
				+ devPullRequestContributors + ", devCommitCount4Weeks=" + devCommitCount4Weeks
				+ ", devCodeAdditions4Weeks=" + devCodeAdditions4Weeks + ", devCodeDeletions4Weeks="
				+ devCodeDeletions4Weeks + ", publicAlexaRank=" + publicAlexaRank + "]";
	}

}
