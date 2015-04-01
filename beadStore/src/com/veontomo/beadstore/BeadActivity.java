package com.veontomo.beadstore;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
	private final String KEY = this.getClass().getName();

	/**
	 * A list view whose items visualize Bead instances.
	 */
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bead_search);

		// example();
		donwload();

		ArrayList<String> data = new ArrayList<String>();

		View header = (View) getLayoutInflater().inflate(
				R.layout.bead_list_header, null);

		listView = (ListView) findViewById(R.id.list);
		listView.addHeaderView(header);
		mAdapter = new BeadAdapter(this, data);
		listView.setAdapter(mAdapter);

		Button btn = (Button) findViewById(R.id.btnBeadFind);
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// / hiding soft keyboard
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				if (mAdapter != null) {
					EditText inputField = (EditText) findViewById(R.id.beadColor);
					String color = inputField.getEditableText().toString()
							.trim();
					String colorCode = Bead.canonicalColorCode(color);
					if (!colorCode.isEmpty()) {
						mAdapter.insert(colorCode, 0);
						mAdapter.notifyDataSetChanged();
						saveIntoHistory(colorCode);

					}
					inputField.getEditableText().clear();
				}
			}
		};
		btn.setOnClickListener(listener);

	}

	
	// TODO
	// move this code into a separate thread of AsyncTask
	// otherwise it crashes
	
	private void donwload() {
		URL url;
		try {
			url = new URL("http://www.android.com/");
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			if (urlConnection == null){
				Log.i(TAG, "conection is null");
				return;
			}
			try {
				InputStream inStream = urlConnection.getInputStream();
				if (null == inStream){
					Log.i(TAG, "input stream is null");
					return;
				}

				InputStream in = new BufferedInputStream(inStream);
				String res = readStream(in); 
				if (res == null){
					Log.i(TAG, "buffered input stream is null");
					return;
				}
				 Log.i(TAG, res);
			} catch (Throwable  e){
				Log.i(TAG, "exception: " + e.getMessage());
				e.printStackTrace();
			} 
			finally {
				urlConnection.disconnect();

			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
				ArrayList<String> data = new ArrayList<String>();
				for (String color : savedHistory) {
					data.add(color);
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

	/**
	 * Example of usage of image crop
	 */
	public void example() {
		File path = new File(Environment.getExternalStorageDirectory(), TAG
				+ "/10050.jpg");
		Bitmap image = (Bitmap) BitmapFactory
				.decodeFile(path.getAbsolutePath());
		Bitmap cropped = cropImage(image);
		saveBitmapAs(cropped, "cropped_image.jpg");
		int[] pixels = new int[image.getHeight() * image.getWidth()];
		image.getPixels(pixels, 0, image.getWidth(), 0, 0,
				image.getWidth() - 1, image.getHeight());

	}

	/**
	 * Example of cropping bitmap
	 * 
	 * @param image
	 */
	public Bitmap cropImage(final Bitmap image) {
		Log.i(TAG,
				String.valueOf(image.getHeight()) + " x "
						+ String.valueOf(image.getWidth()));
		float horOffset = 0.1f;
		float verOffset = 0.3f;
		int left = (int) (image.getWidth() * horOffset);
		int width = (int) (image.getWidth() * (1 - 2 * horOffset));
		int top = (int) (image.getHeight() * verOffset);
		int height = (int) (image.getHeight() * (1 - 2 * verOffset));

		Bitmap cropped = Bitmap.createBitmap(image, left, top, width, height);

		return cropped;
	}

	public void saveBitmapAs(Bitmap bitmap, String name) {

		FileOutputStream out = null;
		try {
			File f = new File(Environment.getExternalStorageDirectory(), name);
			out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	};
}
