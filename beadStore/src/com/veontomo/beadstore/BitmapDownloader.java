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
	
	private static final int READTIMEOUT = 10000;
	
	private static final int CONNECTTIMEOUT = 15000;
	/**
	 * Location where bead images reside.
	 * 
	 * See <a href="http://www.preciosaornela.com/catalog/jablonex_traditional_czech_beads/find_results.php">this page</a> for more results.
	 * 
	 * @since 0.3
	 */
	private static final String IMAGEONLINESTORE = "http://www.preciosaornela.com/catalog/jablonex_traditional_czech_beads/img/#/prod/thread/";

	private static final String[] TEMPLATES = {"bugles", "farfalle", "rocailles", "selak", "twocut"};
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
		Log.i(TAG, "Downloading file " + name);
		InputStream inputStream = null;
		Bitmap image = null;
		URL url = findOnServer(name, TEMPLATES);
		if (url == null){
			return null;
		}
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setReadTimeout(READTIMEOUT);
			connection.setConnectTimeout(CONNECTTIMEOUT);
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
				inputStream = connection.getInputStream();
				image = BitmapFactory.decodeStream(inputStream);
				inputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return image;
	}
	
	private URL findOnServer(String name, String[] locations){
		int len = locations.length;
		int i = 0;
		String path;
		URL url;
		HttpURLConnection connection = null;
		int response;
		for (i = 0; i < len; i++){
			path = IMAGEONLINESTORE.replace("#", locations[i]) + name;
			try {
				url = new URL(path);
				connection = (HttpURLConnection) url.openConnection();
				connection.setReadTimeout(READTIMEOUT);
				connection.setConnectTimeout(CONNECTTIMEOUT);
				connection.setRequestMethod("HEAD");
				connection.setDoInput(true);
				connection.connect();
				response = connection.getResponseCode();
				if (response == HttpURLConnection.HTTP_OK){
					return url;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
		}
		return null;
	};

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
