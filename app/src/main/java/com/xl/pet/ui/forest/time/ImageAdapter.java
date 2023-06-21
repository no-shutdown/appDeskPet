package com.xl.pet.ui.forest.time;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private final Context context;
    private final List<Integer> imageIds;

    public ImageAdapter(Context context, List<List<Integer>> imageIds) {
        this.context = context;
        this.imageIds = new ArrayList<>();
        for (List<Integer> imageId : imageIds) {
            this.imageIds.add(imageId.get(imageId.size() - 1));
        }
    }

    @Override
    public int getCount() {
        return imageIds.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(imageIds.get(position));
        return imageView;
    }
}
