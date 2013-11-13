package com.feeder.rssscorealert;

import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.example.games.basegameutils.GameHelper;

import android.os.Bundle;
import android.view.Menu;

public class LeaderboardActivity extends BaseGameActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard);

		getLeaderboard();
	}

	private void getLeaderboard() {

		String LEADERBOARD_ID = getResources().getString(
				R.string.leaderboard_points_earned);
		startActivityForResult(
				mGamesClient.getLeaderboardIntent(LEADERBOARD_ID),
				REQUEST_LEADERBOARD);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leaderboard, menu);
		return true;
	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub

	}

}
