package com.mycompany.mk.downloadimages;


import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import java.util.ArrayList;


public class ListActivity extends ActionBarActivity implements DownloadWebsiteTask.mInterface{

    private final String URL = "https://api.flickr.com/services/feeds/photos_public.gne?tags=boston";

    LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        new DownloadWebsiteTask(this).execute(URL);
    }

    @Override
    public void getReturnedXML(String result) {

        ArrayList<String> pictures = simpleLinkExtractor(result);

        ListView listView = (ListView) findViewById(R.id.listView);
        final ListViewCustomAdapter adapter = new ListViewCustomAdapter(this, pictures, mMemoryCache);
        listView.setAdapter(adapter);

    }

    private ArrayList<String> simpleLinkExtractor(String inputString) {

        ArrayList<String> links = new ArrayList<String>();

        String temp[];
        temp = inputString.split("href=\"");
        for(int i =0; i < temp.length ; i++) {
            temp[i] = temp[i].split("\"")[0];
            if ((temp[i].substring(temp[i].length() - 4, temp[i].length()).equals(".jpg")) && (links.size() < 10)) {
                links.add(temp[i]);
            }
        }
        return links;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
