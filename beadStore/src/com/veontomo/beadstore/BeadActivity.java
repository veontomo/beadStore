package com.veontomo.beadstore;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

public class BeadActivity extends Activity {
	private TextView mTextView;
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
		
		mTextView = (TextView) findViewById(R.id.dumbText);
//		new HttpGetTask().execute();

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
				if (mAdapter != null) {
					EditText inputField = (EditText) findViewById(R.id.beadColor);
					String color = inputField.getEditableText().toString()
							.trim();
					if (!color.isEmpty()) {
						BeadInfo beadInfo = new BeadInfo();
						// Bead bead = new Bead(color);
						beadInfo.setColorCode(color);
						Location loc = beadStand.getByColor(color);

						if (loc != null) {
							beadInfo.setLocation(loc);
						}
						mAdapter.insert(beadInfo, 0);
						mAdapter.notifyDataSetChanged();
						saveIntoHistory(color);
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
	
	
	
	private class HttpGetTask extends AsyncTask<Void, Void, String> {

		private static final String TAG = "HttpGetTask";

		// Get your own user name at http://www.geonames.org/login
		private static final String URL = "https://www.ietf.org/rfc/rfc2616.txt";

		@Override
		protected String doInBackground(Void... params) {
			String data = "";
			HttpURLConnection httpUrlConnection = null;

			try {
				httpUrlConnection = (HttpURLConnection) new URL(URL)
						.openConnection();

				InputStream in = new BufferedInputStream(
						httpUrlConnection.getInputStream());

				data = readStream(in);

			} catch (MalformedURLException exception) {
				Log.e(TAG, "MalformedURLException");
			} catch (IOException exception) {
				Log.e(TAG, "IOException");
			} finally {
				if (null != httpUrlConnection)
					httpUrlConnection.disconnect();
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			mTextView.setText(result);
		}

		private String readStream(InputStream in) {
			BufferedReader reader = null;
			StringBuffer data = new StringBuffer("");
			try {
				reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while ((line = reader.readLine()) != null) {
					data.append(line);
				}
			} catch (IOException e) {
				Log.e(TAG, "IOException");
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return data.toString();
		}
	}
}
