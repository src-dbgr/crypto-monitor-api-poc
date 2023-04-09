package com.sam.coin.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompanyMetadata {
    @JsonProperty("twitterFollowers")
    Long twitterFollowers;
    @JsonProperty("pullRequestsOpen")
    Long pullRequestsOpen;
    @JsonProperty("pullRequestsClosed")
    Long pullRequestsClosed;
    @JsonProperty("activeDevelopers")
    Long activeDevelopers;
    @JsonProperty("linesOfCodeInGitHub")
    Long linesOfCodeInGitHub;
}
