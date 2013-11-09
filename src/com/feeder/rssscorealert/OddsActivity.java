package com.feeder.rssscorealert;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.feeder.rssscorealert.domain.Match;
import com.google.example.games.basegameutils.BaseGameActivity;

public class OddsActivity extends BaseGameActivity implements
		View.OnClickListener {

	Bundle extras;
	private float homeOdds;
	private float awayOdds;
	private float drawOdds;
	private Adapter arrayAdapter;
	ListView listView;
	private int betTotalVal = 0;
	private static String desc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_odds);
		getOddsData();
		setUpCoinListener(); // not working
		checkIfBetAlreadyApplied();
		// findViewById(R.id.coin_btn).setOnClickListener(this);
		findViewById(R.id.sign_in_button).setOnClickListener(this);
		findViewById(R.id.sign_out_button).setOnClickListener(this);
	}

	private void checkIfBetAlreadyApplied() {
		extras = getIntent().getExtras();
		boolean betAleadyApplied = extras.getBoolean("BET_APPLIED_ALREADY");
		if (betAleadyApplied) { // disable the apply bet button or do something
								// to let the user know a bet already exists
			findViewById(R.id.apply_btn).setVisibility(View.GONE);
		}

	}

	// TODO Fix this
	private void setUpCoinListener() {
		ImageButton imageBtn = (ImageButton) findViewById(R.id.coin_btn);
		if (imageBtn != null) {
			imageBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					betTotalVal += 5;
					// TextView betTotal = (TextView)
					// findViewById(R.id.bet_total);
					// betTotal.setText("Total Bet Value: " + betTotalVal);
				}
			});
		}

	}

	// Get odds passed in from MainActivity, populate listview with these odds
	// then
	private void getOddsData() {
		extras = getIntent().getExtras();
		if (extras != null) {
			homeOdds = extras.getFloat("HOME_ODDS");
			awayOdds = extras.getFloat("AWAY_ODDS");
			drawOdds = extras.getFloat("DRAW_ODDS");
			desc = extras.getString("MATCH_DESC");

			setUpList(homeOdds, awayOdds, drawOdds);

		}
	}

	private void setUpList(float homeOdds, float awayOdds, float drawOdds) {

		// Populate odds
		List<String> listOdds = new ArrayList<String>();
		listOdds.add("Home Win: " + Float.toString(homeOdds));
		listOdds.add("Draw: " + Float.toString(awayOdds));
		listOdds.add("Away Win: " + Float.toString(drawOdds));

		arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_row_odds,
				R.id.odds_value, listOdds);

		listView = (ListView) findViewById(R.id.oddsList);
		listView.setAdapter((ListAdapter) arrayAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.odds, menu);
		return true;
	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSignInSucceeded() {
		findViewById(R.id.sign_in_button).setVisibility(View.GONE);
		findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button) {
			// start the asynchronous sign in flow
			beginUserInitiatedSignIn();

		} else if (view.getId() == R.id.sign_out_button) {
			// sign out.
			signOut();

			// show sign-in button, hide the sign-out button
			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
			findViewById(R.id.sign_out_button).setVisibility(View.GONE);
		}

		else if (view.getId() == R.id.cancel_btn) {

			finish(); // close button clicked, close activity

		} else if (view.getId() == R.id.apply_btn) {
			// apply button clicked, update user's bet total amount and update
			// list row appearance
			// set betApplied on match in listMatches
			for (Object match : MainActivity.listMatches) {
				Match clickedMatch = (Match) match;
				if (desc.equals(clickedMatch.getDescription())) {
					clickedMatch.setBetApplied(true); // bet applied, disable
					// bet icons if opened again
				}
			}
		}
	}

}
