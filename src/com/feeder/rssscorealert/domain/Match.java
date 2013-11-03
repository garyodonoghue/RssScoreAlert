package com.feeder.rssscorealert.domain;

public class Match {

	BettingOdds odds = new BettingOdds();
	Team homeTeam = new Team();
	Team awayTeam = new Team();
	String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BettingOdds getOdds() {
		return odds;
	}

	public void setOdds(BettingOdds odds) {
		this.odds = odds;
	}

	public Team getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}

	public Team getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
	}
}
