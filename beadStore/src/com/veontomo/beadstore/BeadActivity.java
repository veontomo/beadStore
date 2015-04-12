package com.veontomo.beadstore;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

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
	private BeadBaseAdapter mAdapter;


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
		Log.i(TAG, "on create");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bead_search);

		final ArrayList<BeadInfo> beadInfoBunch = new ArrayList<BeadInfo>();
		View header = (View) getLayoutInflater().inflate(
				R.layout.bead_list_header, null);

		listView = (ListView) findViewById(R.id.list);
		listView.addHeaderView(header);
		mAdapter = new BeadBaseAdapter(this.getApplicationContext(), beadInfoBunch);
//		beadInfoBunch.add(0, new BeadInfo("00100"));
//		beadInfoBunch.add(0, new BeadInfo("abc"));
//		beadInfoBunch.add(0, new BeadInfo("10050-1"));
//		beadInfoBunch.add(0, new BeadInfo("38135"));
		mAdapter.notifyDataSetChanged();

		listView.setAdapter(mAdapter);

		Button btn = (Button) findViewById(R.id.btnBeadFind);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeyboard(v);
				if (mAdapter != null) {
					EditText inputField = (EditText) findViewById(R.id.beadColor);
					String colorCode = inputField.getEditableText().toString()
							.trim();
					String colorCodeSanitized = Bead.canonicalColorCode(colorCode);
					if (!colorCodeSanitized.isEmpty()) {
						BeadInfo beadInfo = new BeadInfo(colorCodeSanitized, (Context) getApplicationContext());
						beadInfoBunch.add(beadInfo);
						mAdapter.notifyDataSetChanged();
						saveIntoHistory(colorCodeSanitized);
					}
					inputField.getEditableText().clear();
				}
			}
		});
	
	}

	/**
	 * Hides sof keyboard
	 * @param v
	 * @since 0.7
	 */
	private void hideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null){
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
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
