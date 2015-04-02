/**
 * 
 */
package com.veontomo.beadstore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

/**
 * @author Andrea
 *
 */
public class BitmapInserter extends AsyncTask<String, Void, File> {
	/**
	 * Image view in which the bitmap should be inserted.
	 * 
	 * @since 0.4
	 */
	private ImageView imageView = null;

	/**
	 * Folder in which the files should be stored.
	 * 
	 * @since 0.4
	 */
	private File storage = null;

	/**
	 * Extension of image file in the storage folder
	 * 
	 * @since 0.4
	 */
	private static final String EXTENSION = ".jpg";

	/**
	 * Class responsible for retrieving file (either from internet or from file
	 * system)
	 * 
	 * @since 0.4
	 */
	private BitmapDownloader downloader = new BitmapDownloader();

	private static final String TAG = "BeadStore";

	@Override
	/**
	 * Stop the task if either storage directory or image view  is not provided.
	 * @since 0.4
	 */
	public void onPreExecute() {
		if (this.imageView == null || this.storage == null) {
			Log.i(TAG,
					"cancelling task since storage or image view is missing.");
			this.cancel(false);
		}
		if (!this.storage.exists()) {
			Log.i(TAG, "storage folder " + this.storage.toString()
					+ " should be created.");
			if (!this.storage.mkdirs()) {
				Log.i(TAG, "failed to create the storage folder.");
				this.cancel(false);
			} else {
				Log.i(TAG, "the storage folder is created");

			}

		}
	}

	@Override
	/**
	 * Finds bitmap file corresponding to the argument in the specified directory.
	 * <p>In case the file does not exist, tries to download it, crop it and save 
	 * @param fileName   
	 */
	public File doInBackground(String... fileNames) {
		Log.i(TAG, "inside downloader");
		if (fileNames.length == 0) {
			return null;
		}
		String fileName = fileNames[0] + EXTENSION;
		File file = new File(this.storage, fileName);
		if (!file.exists()) {
			Log.i(TAG, "image for " + fileName + " does not exist!");
			Bitmap image = downloader.downloadBitmap(fileName);
			if (image != null) {
				Log.i(TAG,
						"image is downloaded and its size is "
								+ image.getHeight() + " x " + image.getWidth());
				this.saveBitmap(image, file);
			} else {
				Log.i(TAG, "image " + fileName + " is NOT downloaded");
			}
		} else {
			Log.i(TAG, "image for " + fileName + " is found!");
		}
		return file;

	}

	@Override
	/**
	 * Inserts the file as bitmap in the image view. 
	 * @param	f File 
	 * @since   0.4
	 * @see BitmapInserter#imageView
	 */
	public void onPostExecute(File f) {
		Log.i(TAG, "onPostExecute stub: inserting file " + f.toString());
		if(f.exists() && this.imageView != null){
		    Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
		    this.imageView.setImageBitmap(bitmap);

		}
	}

	/**
	 * Saves the image in the storage folder under the name
	 * 
	 * @param image
	 * @since 0.4
	 */
	public void saveBitmap(Bitmap image, File file) {
		Log.i(TAG, "saving bitmap as file");

		try {
			FileOutputStream fos = new FileOutputStream(file);
			image.compress(Bitmap.CompressFormat.JPEG, 50, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			Log.i(TAG, "File not found: " + e.getMessage());
		} catch (IOException e) {
			Log.i(TAG, "Error accessing file: " + e.getMessage());
		}
	}

	/**
	 * Image view setter.
	 * 
	 * @param imageView
	 * @since 0.4
	 * @see BitmapInserter#imageView
	 */
	public void setLocation(ImageView imageView) {
		this.imageView = imageView;
	}

	/**
	 * Storage setter.
	 * 
	 * @param file
	 * @since 0.4
	 * @see BitmapInserter#storage
	 */
	public void setStorage(File file) {
		this.storage = new File(Environment.getExternalStorageDirectory(), file.toString());
	}

}
