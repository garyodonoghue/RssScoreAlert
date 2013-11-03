package com.feeder.rssscorealert.domain;

public class BettingOdds {

	public float getHomeWinOdds() {
		return homeWinOdds;
	}

	public void setHomeWinOdds(float homeWinOdds) {
		this.homeWinOdds = homeWinOdds;
	}

	public float getAwayWinOdds() {
		return awayWinOdds;
	}

	public void setAwayWinOdds(float awayWinOdds) {
		this.awayWinOdds = awayWinOdds;
	}

	public float getDrawOdds() {
		return drawOdds;
	}

	public void setDrawOdds(float drawOdds) {
		this.drawOdds = drawOdds;
	}

	public float homeWinOdds;
	public float awayWinOdds;
	public float drawOdds;
}
