package com.veontomo.beadstore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
	
	/**
	 * A path to a folder in which downloaded files are to be saved.
	 * 
	 * @since 0.4 
	 */
	private File appDir;

	/**
	 * image view into which the downloaded image should be displayed
	 * @since 0.3
	 */
	private ImageView imageView;
	
	/**
	 * image view setter
	 * @param im
	 * @since 0.3
	 */
	public void setImageView(ImageView im){
		this.imageView = im;
	}
	
	/**
	 * Tries to create a folder (if it does not exist) inside external storage directory where 
	 * to store downloaded images. 
	 * @param appDir
	 * @param mImageView
	 * @since 0.4
	 */
	public void setDir(String appDir){
		File dir = new File(Environment.getExternalStorageDirectory(), appDir);
		if (!dir.exists()){
			if (dir.mkdir()){
				this.appDir = dir;
			} else {
				Log.i(TAG, "Can not create directory " + dir.toString());
			}
		} else {
			this.appDir = dir;
		}
	}
	
	/**
	 * Example of usage of image crop
	 */
	public void example(){
		File path = new File(Environment.getExternalStorageDirectory(), TAG
				+ "/10050.jpg");
		Bitmap image = (Bitmap) BitmapFactory
				.decodeFile(path.getAbsolutePath());
		cropImage(image);

	}
	
	/**
	 * image view getter
	 * @return ImageView
	 * @since 0.3
	 */
	public ImageView getImageView(){
		return this.imageView;
	}
	private static final String TAG = "ImageDownloader";
	/**
	 * Location where bead images reside
	 * 
	 * @since 0.3
	 */
	private static final String IMAGEONLINESTORE = "http://www.preciosaornela.com/catalog/jablonex_traditional_czech_beads/img/rocailles/prod/thread/";
	private static final String EXTENSION = ".jpg";

	@Override
	protected Bitmap doInBackground(String... colorCodes) {
		Log.i(TAG, "loading image into " + String.valueOf(this.getImageView().getId()));
		int count = colorCodes.length;
		if (count == 0) {
			Log.i(TAG, "No parameters are given for async task!!!");
			return null;
		}
		Log.i(TAG, "async task has " + count + " parameters.");
		String beadFileName = colorCodes[0] + ImageDownloader.EXTENSION;

		File dir = this.appDir;
		Log.i(TAG, dir.toString());
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
		Log.i(TAG, "onPostExecute: loading image");
		this.getImageView().setImageBitmap(image);
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
	 * Example of cropping bitmap
	 * @param image
	 */
	public void cropImage(final Bitmap image) {
		Log.i(TAG,
				String.valueOf(image.getHeight()) + " x "
						+ String.valueOf(image.getWidth()));
		float horOffset = 0.1f;
		float verOffset = 0.3f;
		int left = (int) (image.getWidth() * horOffset);
		int width = (int) (image.getWidth() * (1 - 2 * horOffset));
		int top = (int) (image.getHeight() * verOffset);
		int height = (int) (image.getHeight() * (1 - 2 * verOffset));

		Bitmap cropped = Bitmap.createBitmap(image, left, top,	width, height);

		FileOutputStream out = null;
		try {
			File f = new File(Environment.getExternalStorageDirectory(), TAG
					+ "/10050__1.jpg");
			out = new FileOutputStream(f);
			cropped.compress(Bitmap.CompressFormat.JPEG, 40, out);
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