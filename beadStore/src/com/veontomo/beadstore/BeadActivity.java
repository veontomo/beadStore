package com.veontomo.beadstore;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BeadActivity extends Activity {
	ImageView mImageView;
	/**
	 * An auxiliary string used to mark log messages during development stage.
	 * 
	 * @since 0.1
	 */
	final private String TAG = "BeadStore";

	/**
	 * An adapter used to fill in a View with data of a Bead class instance into
	 * view.
	 * 
	 * @since 0.1
	 */
	private BeadAdapter mAdapter;

	private BeadStand beadStand = new BeadStand();

	// private BeadInfo beadInfo = new BeadInfo();

	/**
	 * Array list in which a history of search requests is stored.
	 * 
	 * @since 0.1
	 */
	private ArrayList<String> history;

	/**
	 * A key under which the history of search requests is accessed in the
	 * application state (that is, in a Bundle instance).
	 */
	private final String KEY = "app_key";

	/**
	 * A list view whose items visualize Bead instances.
	 */
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bead);
		mImageView = (ImageView) findViewById(R.id.icon);

		ArrayList<BeadInfo> data = new ArrayList<BeadInfo>();

		View header = (View) getLayoutInflater().inflate(
				R.layout.bead_header_layout, null);

		listView = (ListView) findViewById(R.id.list);
		listView.addHeaderView(header);
		mAdapter = new BeadAdapter(this, R.layout.bead_layout, data);

		listView.setAdapter(mAdapter);

		Button btn = (Button) findViewById(R.id.btnBeadFind);
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				/// hiding soft keyboard
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				if (mAdapter != null) {
					EditText inputField = (EditText) findViewById(R.id.beadColor);
					String color = inputField.getEditableText().toString()
							.trim();
					String colorCode = Bead.canonicalColorCode(color);
					if (!colorCode.isEmpty()) {
						ImageDownloader imageDownloader = new ImageDownloader();

						imageDownloader.setImageView(mImageView);

						imageDownloader.execute(colorCode);

						BeadInfo beadInfo = new BeadInfo();

						beadInfo.setColorCode(colorCode);
						Location loc = beadStand.getByColor(colorCode);

						if (loc != null) {
							beadInfo.setLocation(loc);
						}
						mAdapter.insert(beadInfo, 0);
						mAdapter.notifyDataSetChanged();
						saveIntoHistory(colorCode);
					}
					inputField.getEditableText().clear();
				}
			}
		};
		btn.setOnClickListener(listener);

	}

	/**
	 * Puts string into history.
	 * 
	 * @param s
	 *            bead's color code
	 * @since 0.1
	 * @see BeadActivity#history
	 */
	private void saveIntoHistory(String s) {
		if (history == null) {
			Log.i(TAG, "initialize history");
			history = new ArrayList<String>();
		}
		Log.i(TAG, "add " + s + " into history");
		history.add(0, s);
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
		mImageView = null;
		mAdapter = null;
		listView = null;
		super.onPause();
		Log.i(TAG,
				"Another activity is taking focus (this activity is about to be \"paused\")");
	}

	@Override
	protected void onRestoreInstanceState(Bundle b) {
		if (b != null) {
			super.onRestoreInstanceState(b);
			ArrayList<String> savedHistory = b.getStringArrayList(KEY);

			if (savedHistory != null) {
				ArrayList<BeadInfo> data = new ArrayList<BeadInfo>();
				for (String color : savedHistory) {
					BeadInfo beadInfo = new BeadInfo();
					beadInfo.setColorCode(color);
					Location loc = beadStand.getByColor(color);
					if (loc != null) {
						beadInfo.setLocation(loc);
					}
					data.add(beadInfo);
					saveIntoHistory(color);
				}
				mAdapter.addAll(data);
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
