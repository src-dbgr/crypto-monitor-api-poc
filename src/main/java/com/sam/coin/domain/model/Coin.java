package com.sam.coin.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a cryptocurrency coin with its various attributes")
public class Coin {

	public Coin(UUID id) {
		this.id = id;
	}

	@Schema(description = "Unique identifier of the coin", example = "123e4567-e89b-12d3-a456-426614174000")
	private UUID id;

	@Schema(description = "Timestamp of when the coin data was recorded", example = "2023-04-15T10:30:00Z")
	private Timestamp timestamp;

	@Schema(description = "Symbol of the coin", example = "BTC")
	private String symbol;

	@Schema(description = "Unique identifier of the coin type", example = "bitcoin")
	private String coinId;

	@Schema(description = "Full name of the coin", example = "Bitcoin")
	private String coinName;

	@Schema(description = "Price of the coin in Euros", example = "50000.00")
	private BigDecimal priceEur;

	@Schema(description = "Price of the coin in US Dollars", example = "60000.00")
	private BigDecimal priceUsd;

	@Schema(description = "Price of the coin in Bitcoin", example = "1.0")
	private BigDecimal priceBtc;

	@Schema(description = "Price of the coin in Ethereum", example = "20.5")
	private BigDecimal priceEth;

	@Schema(description = "Market capitalization in Euros", example = "1000000000.00")
	private BigDecimal marketCapEur;

	@Schema(description = "Market capitalization in US Dollars", example = "1200000000.00")
	private BigDecimal marketCapUsd;

	@Schema(description = "Market capitalization in Bitcoin", example = "20000.00")
	private BigDecimal marketCapBtc;

	@Schema(description = "Market capitalization in Ethereum", example = "400000.00")
	private BigDecimal marketCapEth;

	@Schema(description = "Total trading volume in Euros", example = "500000000.00")
	private BigDecimal totalVolumeEur;

	@Schema(description = "Total trading volume in US Dollars", example = "600000000.00")
	private BigDecimal totalVolumeUsd;

	@Schema(description = "Total trading volume in Bitcoin", example = "10000.00")
	private BigDecimal totalVolumeBtc;

	@Schema(description = "Total trading volume in Ethereum", example = "200000.00")
	private BigDecimal totalVolumeEth;

	@Schema(description = "Number of Twitter followers", example = "1000000")
	private long twitterFollowers;

	@Schema(description = "Average number of Reddit posts in the last 48 hours", example = "100.5")
	private BigDecimal redditAvgPosts48Hours;

	@Schema(description = "Average number of Reddit comments in the last 48 hours", example = "500.75")
	private BigDecimal redditAvgComments48Hours;

	@Schema(description = "Number of active Reddit accounts in the last 48 hours", example = "10000.0")
	private BigDecimal redditAccountsActive48Hours;

	@Schema(description = "Number of Reddit subscribers", example = "500000")
	private long redditSubscribers;

	@Schema(description = "Number of forks on the project's GitHub repository", example = "5000")
	private long devForks;

	@Schema(description = "Number of stars on the project's GitHub repository", example = "20000")
	private long devStars;

	@Schema(description = "Total number of issues on the project's GitHub repository", example = "1000")
	private long devTotalIssues;

	@Schema(description = "Number of closed issues on the project's GitHub repository", example = "950")
	private long devClosedIssues;

	@Schema(description = "Number of pull requests merged on the project's GitHub repository", example = "500")
	private long devPullRequestsMerged;

	@Schema(description = "Number of pull request contributors on the project's GitHub repository", example = "100")
	private long devPullRequestContributors;

	@Schema(description = "Number of commits in the last 4 weeks on the project's GitHub repository", example = "200")
	private long devCommitCount4Weeks;

	@Schema(description = "Number of code additions in the last 4 weeks on the project's GitHub repository", example = "10000")
	private long devCodeAdditions4Weeks;

	@Schema(description = "Number of code deletions in the last 4 weeks on the project's GitHub repository", example = "5000")
	private long devCodeDeletions4Weeks;

	@Schema(description = "Alexa rank of the project's public website", example = "10000")
	private long publicAlexaRank;
}