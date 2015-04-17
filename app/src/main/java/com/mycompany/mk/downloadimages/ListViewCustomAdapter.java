package com.mycompany.mk.downloadimages;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mk on 4/5/2015.
 */
public class ListViewCustomAdapter extends ArrayAdapter<String> {

    private ArrayList<String> arrayListValues;
    private LayoutInflater inflater;
    ViewHolder viewHolder;
    Context context;
    LruCache<String, Bitmap> mCache;

    public ListViewCustomAdapter(Context context, ArrayList<String> values, LruCache<String, Bitmap> mCache) {
        super(context, R.layout.listview_item, values);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arrayListValues = values;
        this.mCache = mCache;
        this.context = context;
    }

    public View getView(int position,  View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textViewImageTitle);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageViewImageItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (viewHolder.textView != null) {
            viewHolder.textView.setText(arrayListValues.get(position));
        }

        if (viewHolder.imageView != null) {
            new DownloadImageTask(viewHolder.imageView,mCache).execute(arrayListValues.get(position));
            //Picasso.with(context).load(arrayListValues.get(position)).into(viewHolder.imageView);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
