package com.mycompany.mk.downloadimages;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mk on 4/5/2015.
 */
public class DownloadWebsiteTask extends AsyncTask<String, Void, String> {

    mInterface listener;

    public DownloadWebsiteTask(Context context)
    {
        listener = (mInterface) context;
    }

    public interface mInterface {
        public void getReturnedXML(String result);
    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            String link = (String)arg0[0];
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader
                    (is, "UTF-8") );
            String data = null;
            String webPage = "";
            while ((data = reader.readLine()) != null){
                webPage += data + "\n";
            }
            return webPage;
        }catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result){
        if (listener != null)
        {
            listener.getReturnedXML(result);
        }
        //Log.d("URL Connection Return", result);
    }

}
