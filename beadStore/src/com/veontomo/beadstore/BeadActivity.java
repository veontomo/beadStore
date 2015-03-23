package com.veontomo.beadstore;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BeadActivity extends ListActivity {
	final private String TAG = "BeadStore";
	ArrayAdapter<String> mAdapter;
	ArrayList<String> history;
	private final String KEY = "app_key";
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bead);
		View header = (View) getLayoutInflater().inflate(
				R.layout.bead_header_layout, null);
		getListView().addHeaderView(header);
		ArrayList<String> data = new ArrayList<String>();
		data.add("first");
		data.add("second");
		data.add("third");
		mAdapter = new ArrayAdapter<String>(this, R.layout.bead_layout,
				R.id.colorNumber, data);
		listView = getListView();
		listView.setAdapter(mAdapter);

		Button btn = (Button) findViewById(R.id.btnBeadFind);
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAdapter != null) {
					EditText inputField = (EditText) findViewById(R.id.beadColor);
					String color = inputField.getEditableText().toString()
							.trim();
					if (!color.isEmpty()) {
						mAdapter.add(color);
						saveIntoHistory(color);
					}
					inputField.getEditableText().clear();
				}
			}
		};
		btn.setOnClickListener(listener);

	}

	private void saveIntoHistory(String s) {
		if (history == null) {
			Log.i(TAG, "initialize history");
			history = new ArrayList<String>();
		}
		Log.i(TAG, "add " + s + " into history");
		history.add(s);
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
	protected void onRestoreInstanceState(Bundle b) {
		if (b != null) {
			super.onRestoreInstanceState(b);
			ArrayList<String> saved = b.getStringArrayList(KEY);
			if (saved != null && mAdapter != null){
				for (String key : saved){
					mAdapter.add(key);
					saveIntoHistory(key);
				}
			} 
		}
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
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	   super.onSaveInstanceState(outState);
	   outState.putStringArrayList(KEY, history);
	}

}
