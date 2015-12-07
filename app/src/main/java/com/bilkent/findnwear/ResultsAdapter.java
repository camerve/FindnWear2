package com.bilkent.findnwear;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 1 on 06.12.2015.
 */
public class ResultsAdapter extends ArrayAdapter {
    ArrayList<String> urls;
    ArrayList<String> description;
    ArrayList<String> price;
    ArrayList<String> photoURLs;
    Activity context;
    public ResultsAdapter(Activity context, ArrayList<String> temp,ArrayList<String> aciklama,
                          ArrayList<String>price, ArrayList<String> resimler, ArrayList<String> urls){
        super(context, R.layout.home_list_item, temp);
        description = aciklama;
        this.price = price;
        photoURLs = resimler;
        this.urls = urls;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.results_list_item, null, true);
        //Views inside resuls_list_item
        ImageButton button1 = (ImageButton) rowView.findViewById(R.id.resim1);
        ImageButton button2 = (ImageButton) rowView.findViewById(R.id.resim2);
        TextView price1 = (TextView) rowView.findViewById(R.id.price1);
        TextView price2 = (TextView) rowView.findViewById(R.id.price2);
        TextView brand1 = (TextView) rowView.findViewById(R.id.brand1);
        TextView brand = (TextView) rowView.findViewById(R.id.brand2);
        
        return rowView;
    }
}
