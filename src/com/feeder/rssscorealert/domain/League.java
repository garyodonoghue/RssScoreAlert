package com.feeder.rssscorealert.domain;

public class League {

	public String leagueTitle;

	public static final String[] teamArr = new String[] { "Manchester United",
			"Manchester City", "Arsenal", "Chelsea", "Tottenham",
			"Aston Villa", "Everton", "Newcastle", "Sunderland", "Fulham",
			"Stoke", "West Brom", "West Ham", "Wigan", "Swansea", "Norwich",
			"Queens", "Wolverhampton", "Liverpool", "Hull", "Crystal Palace" };

	public String getLeagueTitle() {
		return leagueTitle;
	}

	public void setLeagueTitle(String leagueTitle) {
		this.leagueTitle = leagueTitle;
	}

	public String[] getTeamArr() {
		return teamArr;
	}
}
