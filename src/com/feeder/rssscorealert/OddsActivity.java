package com.feeder.rssscorealert;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

public class OddsActivity extends Activity {

	Bundle extras;
	private float homeOdds;
	private float awayOdds;
	private float drawOdds;
	private Adapter arrayAdapter;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_odds);
		getOddsData();
		setUpCloseBtnListener();
	}

	// Get odds passed in from MainActivity, populate listview with these odds
	// then
	private void getOddsData() {
		extras = getIntent().getExtras();
		if (extras != null) {
			homeOdds = extras.getFloat("HOME_ODDS");
			awayOdds = extras.getFloat("AWAY_ODDS");
			drawOdds = extras.getFloat("DRAW_ODDS");

			setUpList(homeOdds, awayOdds, drawOdds);

		}
	}

	private void setUpList(float homeOdds, float awayOdds, float drawOdds) {

		String[] oddsArr = new String[3];
		oddsArr[0] = Float.toString(homeOdds);
		oddsArr[1] = Float.toString(awayOdds);
		oddsArr[2] = Float.toString(drawOdds);

		listView = (ListView) findViewById(R.id.oddsList);

		// TODO Test this to see if it sets up the list ok
		arrayAdapter = new ArrayAdapter<String>(MainActivity.getAppContext(),
				R.layout.list_row, R.id.oddsList, oddsArr);
		listView.setAdapter((ListAdapter) arrayAdapter);

	}

	private void setUpCloseBtnListener() {
		Button close_button = (Button) findViewById(R.id.close_btn);
		close_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.odds, menu);
		return true;
	}

}
