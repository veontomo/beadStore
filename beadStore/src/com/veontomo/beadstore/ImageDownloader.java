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
	 * image view getter
	 * @return ImageView
	 * @since 0.3
	 */
	public ImageView getImageView(){
		return this.imageView;
	}
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
		String beadFileName = colorCodes[0] + ImageDownloader.EXTENSION;

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
}