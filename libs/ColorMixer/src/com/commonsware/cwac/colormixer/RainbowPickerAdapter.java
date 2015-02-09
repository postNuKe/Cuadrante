package com.commonsware.cwac.colormixer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class RainbowPickerAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> colorList = new ArrayList<Integer>();
    int colorGridColumnWidth;

    public RainbowPickerAdapter(Context context) {

        this.context = context;

        // defines the width of each color square
        colorGridColumnWidth = context.getResources().getInteger(R.integer.colorGridColumnWidth);

        int colorCount = 96;
        int step = 256 / (colorCount / 6);
        int red = 0, green = 0, blue = 0;

        // FF 00 00 --> FF FF 00
        for (red = 255, green = 0, blue = 0; green <= 255; green += step)
            colorList.add(Color.rgb(red, green, blue));

        // FF FF 00 --> 00 FF 00
        for (red = 255, green = 255, blue = 0; red >= 0; red -= step)
            colorList.add(Color.rgb(red, green, blue));

        // 00 FF 00 --> 00 FF FF
        for (red = 0, green = 255, blue = 0; blue <= 255; blue += step)
            colorList.add(Color.rgb(red, green, blue));

        // 00 FF FF -- > 00 00 FF
        for (red = 0, green = 255, blue = 255; green >= 0; green -= step)
            colorList.add(Color.rgb(red, green, blue));

        // 00 00 FF --> FF 00 FF
        for (red = 0, green = 0, blue = 255; red <= 255; red += step)
            colorList.add(Color.rgb(red, green, blue));

        // FF 00 FF -- > FF 00 00
        for (red = 255, green = 0, blue = 255; blue >= 0; blue -= 256 / step)
            colorList.add(Color.rgb(red, green, blue));

        // add gray colors
        for (int i = 255; i >= 0; i -= 11) {
            colorList.add(Color.rgb(i, i, i));
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        // can we reuse a view?
        if (convertView == null) {
            imageView = new ImageView(context);
            // set the width of each color square
            imageView.setLayoutParams(new GridView.LayoutParams(colorGridColumnWidth, colorGridColumnWidth));

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setBackgroundColor(colorList.get(position));
        imageView.setId(colorList.get(position));

        return imageView;
    }

    public int getCount() {
        return colorList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
}