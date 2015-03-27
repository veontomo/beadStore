package com.veontomo.beadstore;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BeadActivity extends Activity {
	private ImageView mImageView;
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
				if (mAdapter != null) {
					EditText inputField = (EditText) findViewById(R.id.beadColor);
					String color = inputField.getEditableText().toString()
							.trim();
					if (!color.isEmpty()) {
						(new HttpGetTask()).execute(color);

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

	private class HttpGetTask extends AsyncTask<String, Void, Bitmap> {

		private static final String TAG = "HttpGetTask";
		/**
		 * Location where bead images reside
		 * 
		 * @since 0.3
		 */
		private static final String IMAGEONLINESTORE = "http://www.preciosaornela.com/catalog/jablonex_traditional_czech_beads/img/rocailles/prod/thread/";
		private static final String EXTENSION = ".jpg";

		@Override
		protected Bitmap doInBackground(String... colorCodes) {
			int count = colorCodes.length;
			if (count == 0) {
				Log.i(TAG, "No parameters are given for async task!!!");
				return null;
			}
			Log.i(TAG, "async task has " + count + " parameters.");
			String beadFileName = colorCodes[0] + HttpGetTask.EXTENSION;

			File dir = Environment.getExternalStorageDirectory();
			File imgFile = new File(dir, beadFileName);
			if (imgFile.exists()) {
				Log.i(TAG, "file " + imgFile.toString() + " exists.");
				return (Bitmap) BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());
			}

			Log.i(TAG, "file " + imgFile.toString() + " does not exist.");
			String data = "";
			String imageUrl = IMAGEONLINESTORE + beadFileName;
			Log.i(TAG, "Downloading file from " + imageUrl);

			HttpURLConnection httpUrlConnection = null;
			try {
				Log.i(TAG, "Inside try-catch block");
				new DefaultHttpClient()
						.execute(new HttpGet(imageUrl))
						.getEntity()
						.writeTo(
								new FileOutputStream(
										new File(dir, beadFileName)));
				Log.i(TAG, "After downloading");

				imgFile = new File(dir, beadFileName);
				if (imgFile.exists()) {
					Log.i(TAG, "file " + imgFile.toString() + " now exists.");
					return (Bitmap) BitmapFactory.decodeFile(imgFile
							.getAbsolutePath());
				} 
				Log.i(TAG, "Give up...");
				return (Bitmap) BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());
			} catch (FileNotFoundException e) {
				Log.i(TAG, "FileNotFoundException " + e.getMessage());
			} catch (IOException e) {
				Log.i(TAG, "IOException " + e.getMessage());
			} finally {
				if (null != httpUrlConnection)
					httpUrlConnection.disconnect();
			}

			// downloaded file might differ from the original
			// due to the fact (?) that readStream does not in fact read
			// requested number of bytes
			// http://stackoverflow.com/questions/576513/android-download-binary-file-problems
			
			
			
			// try {
			// imgFile.createNewFile();
			// httpUrlConnection = (HttpURLConnection) new URL(imageUrl)
			// .openConnection();
			//
			// InputStream in = new BufferedInputStream(
			// httpUrlConnection.getInputStream());
			//
			// data = readStream(in);
			//
			// BufferedWriter writer = new BufferedWriter(new FileWriter(
			// imgFile, true));
			// writer.write(data);
			// writer.close();
			// imgFile = new File(dir, beadFileName);
			// return (Bitmap) BitmapFactory.decodeFile(imgFile
			// .getAbsolutePath());
			// } catch (MalformedURLException exception) {
			// Log.e(TAG, "MalformedURLException");
			// } catch (IOException exception) {
			// Log.e(TAG, "IOException " + exception.getMessage());
			// } finally {
			// if (null != httpUrlConnection)
			// httpUrlConnection.disconnect();
			// }
			Log.i(TAG, "Returning null...");
			return null;

		}

		@Override
		protected void onPostExecute(Bitmap image) {
			mImageView.setImageBitmap(image);
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
