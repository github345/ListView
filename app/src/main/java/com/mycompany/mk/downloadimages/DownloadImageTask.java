package com.mycompany.mk.downloadimages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mk on 4/7/2015.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    LruCache<String, Bitmap> mCache;

    public DownloadImageTask(ImageView imageView, LruCache<String, Bitmap> mCache) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.mCache = mCache;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        final Bitmap bitmap = mCache.get(params[0]);

        // Memory Cache
        if (bitmap != null) {
            //Log.w("Memory Cache", "From MEMORY CACHE: " + params[0]);
            return bitmap;
        } else {
            //Log.w("Memory Cache", "From DOWNLOAD: " + params[0]);
            return downloadBitmap(params[0]);
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    Drawable placeholder = imageView.getContext().getResources().getDrawable(R.mipmap.ic_launcher);
                    imageView.setImageDrawable(placeholder);
                }
            }
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //Log.w("ImageDownloader", "Got " + url);

                if (mCache.get(url) == null) {
                    //Log.w("Memory Cache", "Put into MEMORY CACHE: " + url);
                    mCache.put(url, bitmap);
                }

                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}