package com.veontomo.beadstore;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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
	
	/**
	 * Value on which width should be reduced form left and from right.
	 * @since 0.4
	 */
	private static final float WIDTHOFFSET = 0.1f;

	/**
	 * Value on which height should be reduced form top and from bottom.
	 * @since 0.4
	 */
	private static final float HEIGHTOFFSET = 0.1f;

	/**
	 * Progress of downloading.
	 * <p>0 correspond to the start, 100 - to the end.</p>
	 * 
	 * @since 0.5
	 */
	private Integer progress = 0;

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
			int response = connection.getResponseCode();
			if (response != HttpURLConnection.HTTP_OK) {
				return null;
			}
			inputStream = connection.getInputStream();
			image = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return image;
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
		int left = (int) (image.getWidth() * WIDTHOFFSET);
		int width = (int) (image.getWidth() * (1 - 2 * WIDTHOFFSET));
		int top = (int) (image.getHeight() * HEIGHTOFFSET);
		int height = (int) (image.getHeight() * (1 - 2 * HEIGHTOFFSET));

		Bitmap cropped = Bitmap.createBitmap(image, left, top, width, height);

		return cropped;
	}

	/**
	 * Progress getter
	 * @since 0.5
	 * @return
	 */
	public Integer getProgress() {
		return this.progress;
	}
	
	/**
	 * Progress setter
	 * @since 0.5
	 * @param p
	 */
	public void setProgress(Integer p) {
		this.progress = p;
	}

}