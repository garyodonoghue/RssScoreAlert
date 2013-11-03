package com.feeder.rssscorealert;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.feeder.rssscorealert.domain.League;
import com.feeder.rssscorealert.domain.Match;

public class MainActivity extends Activity {

	static List<String> listTitles = new ArrayList<String>();
	private static ArrayAdapter<String> arrayAdapter;
	static ListView ListView1 = null;
	private static Context context;
	public static List<Object> listMatches;

	public static ProgressDialog dialog;

	public boolean homeOddsFound = false;
	public boolean awayOddsFound = false;

	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MainActivity.context = getApplicationContext();
		ListView1 = (ListView) findViewById(R.id.list);

		arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.activity_list_item, 0, listTitles);

		dialog = new ProgressDialog(MainActivity.this);

		setUpListener();

	}

	private void setUpListener() {
		dialog.setMessage("Loading...");
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(false);

		Button button = (Button) findViewById(R.id.refreshBtn);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
				new AsynchTask().execute();
			}
		});

		final ListView lv = (ListView) findViewById(R.id.list);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				String selectedFromList = (String) (lv
						.getItemAtPosition(myItemInt));

				// this gets the text content, need to parse this to pick out
				// the teams and match with teams in odds feed
				getOddsFromSelectedTeam(selectedFromList);

			}
		});
	}

	public void getOddsFromSelectedTeam(String selectedFromList) {
		// parse the string, picking out the temas.
		// check if any of the Matches contain any of these teams in their
		// description, then get odds for that match
		String regularExpression = " ";
		String[] splitStringArr = selectedFromList.split(regularExpression);

		for (String str : splitStringArr) {
			str.trim();
			for (String team : League.teamArr) {
				if (str.compareTo(team) == 0) {

					// TODO Clean this up!

					for (Object oddsMatch : listMatches) {
						Match match = (Match) oddsMatch;

						homeOddsFound = false;
						awayOddsFound = false;

						if (match.getHomeTeam().getName() != null
								&& match.getAwayTeam().getName() != null) {
							if (match.getHomeTeam().getName().toString()
									.compareTo(str) == 0) {

								homeOddsFound = true;
							}
							if (match.getAwayTeam().getName().toString()
									.compareTo(str) == 0) {

								awayOddsFound = true;
							}
						}
						if (homeOddsFound && awayOddsFound) {
							// start new activity passing in the home and away
							// odds
							// TODO cannot use this yet, need to find new odds
							// feed

							Toast.makeText(
									getAppContext(),
									"Odds for "
											+ match.getHomeTeam().getName()
													.toString() + " win = "
											+ match.getOdds().getHomeWinOdds(),
									Toast.LENGTH_SHORT).show();

							Toast.makeText(
									getAppContext(),
									"Odds for " + match.getAwayTeam().getName()
											+ " win ="
											+ match.getOdds().getAwayWinOdds(),
									Toast.LENGTH_SHORT).show();

							intent = new Intent(this, OddsActivity.class);
							intent.putExtra("HOME_ODDS", match.getOdds()
									.getHomeWinOdds());
							intent.putExtra("AWAY_ODDS", match.getOdds()
									.getAwayWinOdds());
							intent.putExtra("DRAW_ODDS", match.getOdds()
									.getDrawOdds());
							startActivity(intent);
						} else {
							Toast.makeText(getAppContext(),
									"No odds found for this match",
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static Context getAppContext() {
		return context;
	}

	public void setListItemsReturned(List<String> listTitles) {

	}

	public void updateUI(List<Object> list) {
		dialog.dismiss();

		List<String> listItems = new ArrayList<String>();
		for (Object obj : list) {
			listItems.add((String) obj);
		}
		if (list.size() > 0) {
			arrayAdapter = new ArrayAdapter<String>(getAppContext(),
					R.layout.list_row, R.id.scores, listItems);
			ListView1.setAdapter(arrayAdapter);

		} else {
			Toast.makeText(getAppContext(), "No scores to show",
					Toast.LENGTH_SHORT).show();
		}

	}
}
