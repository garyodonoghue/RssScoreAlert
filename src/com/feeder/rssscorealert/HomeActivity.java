package com.feeder.rssscorealert;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.example.games.basegameutils.BaseGameActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.*;

public class HomeActivity extends BaseGameActivity {

	private boolean userViewLeaderboard = false;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		drawerLayout.openDrawer(drawerListView);
	}

	private String[] drawerListViewItems;
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private ActionBarDrawerToggle actionBarDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// get list items from strings.xml
		drawerListViewItems = getResources().getStringArray(R.array.items);

		// get ListView defined in activity_main.xml
		drawerListView = (ListView) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		drawerListView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_listview_item, drawerListViewItems));

		// 2. App Icon
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// 2.1 create ActionBarDrawerToggle
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		drawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		);

		// 2.2 Set actionBarDrawerToggle as the DrawerListener
		drawerLayout.setDrawerListener(actionBarDrawerToggle);

		// 2.3 enable and show "up" arrow
		if (getActionBar() != null)
			getActionBar().setDisplayHomeAsUpEnabled(true);

		// just styling option
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		drawerListView.setOnItemClickListener(new DrawerItemClickListener());

		drawerLayout.openDrawer(drawerListView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns
		// true
		// then it has handled the app icon touch event

		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// Toast.makeText(HomeActivity.this, ((TextView) view).getText(),
			// Toast.LENGTH_LONG).show();

			if (((TextView) view).getText().toString().compareTo("Matches") == 0) {
				Intent matchesIntent = new Intent(HomeActivity.this,
						MainActivity.class);
				startActivity(matchesIntent);
			} else if (((TextView) view).getText().toString()
					.compareTo("Leaderboard") == 0) {
				getLeaderboard();
				userViewLeaderboard = true;
				onSignInSucceeded();
				userViewLeaderboard = false;
			}

			drawerLayout.closeDrawer(drawerListView);

		}
	}

	private void getLeaderboard() {

		int isGooglePlayServiceAvilable = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());
		if (isGooglePlayServiceAvilable == ConnectionResult.SUCCESS) {
			beginUserInitiatedSignIn();
		} else {
			// GooglePlayServicesUtil.getErrorDialog(isGooglePlayServiceAvilable,
			// MainMenu.this, REQUEST_DIALOG).show();
		}

	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSignInSucceeded() {
		if (userViewLeaderboard) {
			String LEADERBOARD_ID = getResources().getString(

			R.string.leaderboard_points_earned);
			startActivityForResult(
					getGamesClient().getLeaderboardIntent(LEADERBOARD_ID), 1);
		}
	}

}
