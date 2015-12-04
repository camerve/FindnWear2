package com.bilkent.findnwear;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bilkent.findnwear.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by 1 on 05.11.2015.
 */
public class HomeAdapter extends ArrayAdapter{
    private ArrayList<String> photoExplanation, photoURLs;
    private static Activity context;
    public static Cloth seciliUrun = null;
    public HomeAdapter(Activity context, ArrayList<String> temp,ArrayList<String> aciklama, ArrayList<String> resimler) {
        super(context, R.layout.home_list_item, temp);
        photoExplanation = aciklama;
        photoURLs = resimler;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.home_list_item, null, true);
        //Views inside home_list_item
        TextView resim1 = (TextView)rowView.findViewById(R.id.aciklama1);
        TextView resim2 = (TextView)rowView.findViewById(R.id.aciklama2);
        TextView resim3 = (TextView)rowView.findViewById(R.id.aciklama3);
        ImageButton resimButton1 = (ImageButton) rowView.findViewById(R.id.resim1);
        ImageButton resimButton2 = (ImageButton) rowView.findViewById(R.id.resim2);
        ImageButton resimButton3 = (ImageButton) rowView.findViewById(R.id.resim3);
        resimButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        resimButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        resimButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        String image_url;
        int i =0;
        if (photoExplanation.size() > 3 * position + i) {
            if (i == 0) {
                resim1.setText(photoExplanation.get(3 * position + i));
                // Image url is assigned to image_url string
                image_url = photoURLs.get(3*position);
                if(image_url != null){
                    Picasso.with(context).load(image_url).memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE).into(resimButton1);
                    i++;
                    if (photoExplanation.size() > 3 * position + i) {
                        image_url = photoURLs.get(3*position+i);
                        resim2.setText(photoExplanation.get(3*position+i));
                        if(image_url != null){
                            //Use image to fetch the object from server
                            Picasso.with(context).load(image_url).memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE).into(resimButton2);
                        }
                        i++;
                        if (photoExplanation.size() > 3 * position + i) {
                            resim3.setText(photoExplanation.get(3 * position + i));
                            // Image url is assigned to image_url string
                            image_url = photoURLs.get(3*position+i);
                            if(image_url != null){
                                Picasso.with(context).load(image_url).memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .networkPolicy(NetworkPolicy.NO_CACHE).into(resimButton3);
                            }

                        } else {
                            //If this info does not exist set them invisible
                            resim3.setVisibility(View.INVISIBLE);
                            resimButton3.setVisibility(View.INVISIBLE);
                        }
                        i++;
                    } else {
                        //If this info does not exist set them invisible
                        resim2.setVisibility(View.INVISIBLE);
                        resimButton2.setVisibility(View.INVISIBLE);
                        //If this info does not exist set them invisible
                        resim3.setVisibility(View.INVISIBLE);
                        resimButton3.setVisibility(View.INVISIBLE);
                    }
                }
            }
        } else {
            rowView.setVisibility(View.INVISIBLE);
        }
        return rowView;
    }
}
