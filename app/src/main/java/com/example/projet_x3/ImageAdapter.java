package com.example.projet_x3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

    //recuperation du nom de l'image, et creatuin d"une grid
    private Context ctx;
    private final String[] filesNames;
    private final String[] filesPaths;

    public ImageAdapter(Context ctx, String[] filesNames, String[] filesPaths) {
        this.ctx = ctx;
        this.filesNames = filesNames;
        this.filesPaths = filesPaths;
    }

    @Override
    public int getCount() {
        return filesNames.length;
    }

    @Override
    public Object getItem(int pos) {
        return pos;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        ImageView imageView;

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);





            if (convertView == null) {
            // if it's not recycled, initialize some attributes
                grid = inflater.inflate(R.layout.gridview_item, null);

                imageView = new ImageView(ctx);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
                grid = (View) convertView;
        }
        TextView textView = (TextView) grid.findViewById(R.id.gridview_text);
      imageView = (ImageView)grid.findViewById(R.id.gridview_image);
        textView.setText(filesNames[position]);
        Bitmap bmp = BitmapFactory.decodeFile(filesPaths[position]);
        imageView.setImageBitmap(bmp);
        return grid;
    }

    // references to our images

}