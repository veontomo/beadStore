package com.veontomo.beadstore;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class BeadActivity extends Activity {
	final private String TAG = "BeadStore";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bead);

		Button btn = (Button) findViewById(R.id.btnBeadFind);

		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText inputField = (EditText) findViewById(R.id.beadColor);
				String colorNumber = inputField.getText().toString();
				// v.setEnabled(false);
				Toast.makeText(getApplicationContext(),
						colorNumber + String.valueOf(v.getAlpha()),
						Toast.LENGTH_SHORT).show();
				// View footerView =
				// getLayoutInflater().inflate(R.layout.footer_layout, null);
				// listview.addFooterView(footerView);
				ListView listView = (ListView) findViewById(R.id.beadsView);
				// Defined Array values to show in ListView

				String[] values = new String[] { "Android", "iPhone",
						"WindowsMobile", "Blackberry", "WebOS", "Ubuntu",
						"Windows7", "Max OS X", "Linux", "OS/2", "Ubuntu",
						"Windows7", "Max OS X", "Linux", "OS/2", "Ubuntu",
						"Windows7", "Max OS X", "Linux", "OS/2", "Android",
						"iPhone", "WindowsMobile" };

				final ArrayList<String> list = new ArrayList<String>();
				for (int i = 0; i < values.length; ++i) {
					Log.i(TAG, values[i]);
					list.add(values[i]);
				}
				final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.bead_layout, list);
				listView.setAdapter(adapter);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bead, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "The activity is visible and about to be started.");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i(TAG, "The activity is visible and about to be restarted.");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "The activity is and has focus (it is now \"resumed\")");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG,
				"Another activity is taking focus (this activity is about to be \"paused\")");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "The activity is no longer visible (it is now \"stopped\")");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "The activity is about to be destroyed.");
	}

}
