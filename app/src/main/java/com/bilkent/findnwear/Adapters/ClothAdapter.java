package com.bilkent.findnwear.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilkent.findnwear.Model.Cloth;
import com.bilkent.findnwear.R;
import com.bilkent.findnwear.Utilities.OnItemClickListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ClothAdapter extends RecyclerView.Adapter<ClothAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Cloth> data;
    private OnItemClickListener listener;

    public ClothAdapter(Context con){
        this.context = con;
        this.data = new ArrayList<>();
    }

    public void setData(ArrayList<Cloth> data){
        this.data = new ArrayList<>();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public ArrayList<Cloth> getData() {
        return data;
    }

    public Cloth getItem(int position) {
        return data.get(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvName;
        public ImageView ivArt;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.dizi_textview);
            this.ivArt = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    @Override
    public ClothAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(context).
            inflate(R.layout.item_view_cloth, parent, false);
        // Return a new holder instance
        return new ClothAdapter.ViewHolder(itemView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final ClothAdapter.ViewHolder holder, final int position) {
        // Get the data model based on position
        final Cloth cloth = data.get(position);
        // Set item views based on the data model
        holder.tvName.setText(cloth.getDescription());

        int bgColor = context.getResources().getColor(R.color.primary);
        int textColor = context.getResources().getColor(R.color.primary_light);

        holder.tvName.setBackgroundColor(bgColor);
        holder.tvName.setTextColor(textColor);

        Picasso.with(context).load(cloth.thumb_url).into(holder.ivArt, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) holder.ivArt.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch vibrant =
                                palette.getVibrantSwatch();
                        if (vibrant != null) {

                            holder.tvName.setBackgroundColor(vibrant.getRgb());
                            holder.tvName.setTextColor(vibrant.getTitleTextColor());
                        }
                    }
                });
            }

            @Override
            public void onError() {
                Log.d("artwork", "error occurred");
            }
        });

        holder.ivArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onClick(position, cloth.id, holder);
            }
        });

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        if(data==null)
            return 0;
        return data.size();
    }
}