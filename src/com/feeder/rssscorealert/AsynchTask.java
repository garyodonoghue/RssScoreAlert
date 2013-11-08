package com.feeder.rssscorealert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.AsyncTask;

import com.feeder.rssscorealert.domain.BettingOdds;
import com.feeder.rssscorealert.domain.League;
import com.feeder.rssscorealert.domain.Match;
import com.feeder.rssscorealert.domain.Team;

public class AsynchTask extends AsyncTask<Void, Void, ArrayList<List<Object>>> {

	private static final String ENG_PREMIER_LEAGUE = "Eng. Premier League";
	public static List<List<Object>> listRetItems = new ArrayList<List<Object>>();
	MainActivity main = new MainActivity();

	private static final String ENGLAND_PREMIER_LEAGUE = "England - Premier League";

	@Override
	protected void onPostExecute(ArrayList<List<Object>> result) {
		listRetItems = result;
		super.onPostExecute(result);
		main.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// display the list of matches on screen
				main.updateUI((listRetItems.get(0)));

				// parse the list of matches, and set the home and away team
				// names
				MainActivity.listMatches = listRetItems.get(1);
				parseMatchDescriptions(MainActivity.listMatches);
			}

			// At this point we should have the odds set for all the matches,
			// here we need to set the home/away team names to map to the list
			// of matches in the UI
			private void parseMatchDescriptions(List<Object> listMatches) {
				for (Object objMatch : listMatches) {
					Match match = (Match) objMatch;

					Team homeTeam = new Team();
					Team awayTeam = new Team();

					String homeTeamName = null;
					String awayTeamName = null;

					// TODO fix this, home and away teams arent being set right
					for (String premiershipTeam : League.teamArr) {
						if (match.getDescription().contains(premiershipTeam)
								&& (homeTeamName == null)) {
							homeTeamName = premiershipTeam;
						} else if (match.getDescription().contains(
								premiershipTeam)
								&& (homeTeamName != null)) {
							awayTeamName = premiershipTeam;
						}
					}

					homeTeam.setName(homeTeamName);
					match.setHomeTeam(homeTeam);

					awayTeam.setName(awayTeamName);
					match.setAwayTeam(awayTeam);
				}

			}
		});

		MainActivity.dialog.dismiss();
	}

	List<String> listItems = new ArrayList<String>();

	@Override
	protected ArrayList<List<Object>> doInBackground(Void... params) {
		List<Object> scores = new ArrayList<Object>();
		List<Object> odds = new ArrayList<Object>();

		scores = getScores();
		odds = getOdds();

		ArrayList<List<Object>> listScoresAndOdds = new ArrayList<List<Object>>();
		listScoresAndOdds.add(scores);
		listScoresAndOdds.add(odds);

		return listScoresAndOdds;

	}

	private List<Object> getOdds() {
		List<Object> matchList = new ArrayList<Object>();
		URL url = null;

		try {
			url = new URL("http://xml.cdn.betclic.com/odds_en.xml");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		URLConnection conn = null;
		Document doc = parseInput(url, conn);

		NodeList nodes = doc.getElementsByTagName("event");

		for (int i = 0; i < nodes.getLength(); i++) {
			Element element = (Element) nodes.item(i);
			if (element.getAttribute("name").compareTo(ENG_PREMIER_LEAGUE) == 0) {
				NodeList matches = element.getElementsByTagName("match");

				// build up a map of match and odds, match would need to be set
				// here and inside this next look map to the odds
				for (int j = 0; j < matches.getLength(); j++) {

					// create a new match and set its names and odds values
					Match match = new Match();

					Element elmnt = (Element) matches.item(j);
					match.setDescription(elmnt.getAttribute("name"));

					// bets tag
					NodeList bets = elmnt.getChildNodes();
					Element choices = (Element) bets.item(0);
					NodeList betValues = choices.getChildNodes();

					// bet tag
					Element bet = (Element) betValues.item(0);

					// Get home/away/draw odds
					NodeList choiceOdds = bet.getChildNodes();

					BettingOdds matchOdds = new BettingOdds();

					// choice elements
					Element HomeWinElement = (Element) choiceOdds.item(0);
					String homeWinOdds = HomeWinElement.getAttribute("odd");
					matchOdds.setHomeWinOdds(Float.parseFloat(homeWinOdds));

					Element AwayWinElement = (Element) choiceOdds.item(1);
					String awayWinOdds = AwayWinElement.getAttribute("odd");
					matchOdds.setAwayWinOdds(Float.parseFloat(awayWinOdds));

					Element DrawElement = (Element) choiceOdds.item(2);
					if (DrawElement != null) {
						String drawOdds = DrawElement.getAttribute("odd");
						matchOdds.setDrawOdds(Float.parseFloat(drawOdds));
					}
					match.setOdds(matchOdds);
					matchList.add(match);
				}

			}
		}
		return matchList;
	}

	private Document parseInput(URL url, URLConnection conn) {
		try {
			conn = url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = builder.parse(conn.getInputStream());
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	private List<Object> getScores() {
		List<Object> items = new ArrayList<Object>();
		URL url = null;

		try {
			url = new URL("http://www.soccerstand.com/rss/soccer");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}//
		URLConnection conn = null;
		Document doc = parseInput(url, conn);

		NodeList nodes = doc.getElementsByTagName("item");

		for (int i = 0; i < nodes.getLength(); i++) {
			Element element = (Element) nodes.item(i);
			NodeList title = element.getElementsByTagName("title");
			Element line = (Element) title.item(0);
			String text = line.getTextContent();

			if (text.contains(ENGLAND_PREMIER_LEAGUE))
				items.add(line.getTextContent());
		}
		// TODO Remove - Adding this is for testing puposes
		items.add("team A 1 - 1 team B");
		return items;
	}
}
