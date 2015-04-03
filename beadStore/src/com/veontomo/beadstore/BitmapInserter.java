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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Andrea
 *
 */
public class BitmapInserter extends AsyncTask<String, Integer, File> {
	
	private static final int IMAGEQUALITY = 50;
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
		if (fileNames.length == 0) {
			return null;
		}
		String fileName = fileNames[0] + EXTENSION;
		File file = new File(this.storage, fileName);
		if (!file.exists()) {
			this.prepareFile(fileName, file);
		}
		return file;

	}

	/**
	 * Prepares the file with bitmap (i.e., downloads it from internet, [crops it,] saves) 
	 * @param fileName   file to search in internet
	 * @param file       where to save downloaded file
	 * @return boolean
	 * @since 0.4
	 */
	private boolean prepareFile(String fileName, File file) {
		Bitmap image = downloader.downloadBitmap(fileName);
		if (image != null) {
			Bitmap croppedImage = downloader.cropImage(image);
			if (croppedImage != null){
				return this.saveBitmap(croppedImage, file);
			}
		}
		return false;
	}

	@Override
	/**
	 * Inserts the file as bitmap in the image view. 
	 * @param	f File 
	 * @since   0.4
	 * @see BitmapInserter#imageView
	 */
	public void onPostExecute(File f) {
		if(f.exists() && this.imageView != null){
		    Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
		    this.imageView.setImageBitmap(bitmap);
		}
	}

	/**
	 * Saves the image in the storage folder in the given file.
	 * 
	 * Returns true in case of success, false otherwise. 
	 * 
	 * @param image
	 * @return boolean
	 * @since 0.4
	 */
	public boolean saveBitmap(Bitmap image, File file) {
		boolean isSaved = false;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			isSaved = image.compress(Bitmap.CompressFormat.JPEG, IMAGEQUALITY, fos);
			fos.close();
			return isSaved;
		} catch (FileNotFoundException e) {
			Log.i(TAG, "File not found: " + e.getMessage());
		} catch (IOException e) {
			Log.i(TAG, "Error accessing file: " + e.getMessage());
		}
		return isSaved;
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
