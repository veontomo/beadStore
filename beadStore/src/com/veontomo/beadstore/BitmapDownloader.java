package com.veontomo.beadstore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

class BitmapDownloader {
	private static final String TAG = "BeadStore";
	/**
	 * Location where bead images reside.
	 * 
	 * See <a href="http://www.preciosaornela.com/catalog/jablonex_traditional_czech_beads/find_results.php">this page</a> for more results.
	 * 
	 * @since 0.3
	 */
	private static final String IMAGEONLINESTORE = "http://www.preciosaornela.com/catalog/jablonex_traditional_czech_beads/img/rocailles/prod/thread/";

	// private static final String EXTENSION = ".jpg";

	/**
	 * Tries to detect the location of the file on the server and downloads it.
	 * 
	 * @param string
	 * @return Bitmap
	 * @since 0.4
	 */
	public Bitmap downloadBitmap(String name) {
		InputStream inputStream = null;
		Bitmap image = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(IMAGEONLINESTORE + name);
			connection = (HttpURLConnection) url.openConnection();
			connection.setReadTimeout(10000 /* milliseconds */);
			connection.setConnectTimeout(15000 /* milliseconds */);
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.connect();
			Log.i(TAG, String.valueOf(connection.getResponseCode()));
			int response = connection.getResponseCode();
			if (response != HttpURLConnection.HTTP_OK) {
				Log.i(TAG, "response is NOT OK");
				return null;
			}
			Log.i(TAG, "response OK");
			inputStream = connection.getInputStream();
			image = BitmapFactory.decodeStream(inputStream);
			if (image == null) {
				Log.i(TAG, "url does not correspond to an image");
			} else {
				Log.i(TAG, "image size: " + String.valueOf(image.getHeight())
						+ " x " + image.getWidth());

			}

			// data = readStream(inputStream);
			Log.i(TAG, "Disconnecting and closing");

			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return this.cropImage(image);
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

}