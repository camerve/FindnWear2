package com.bilkent.findnwear.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilkent.findnwear.Model.Cloth;
import com.bilkent.findnwear.R;
import com.bilkent.findnwear.Utilities.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Cloth> data;
    private OnItemClickListener listener;

    public WishlistAdapter(Context con){
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
        public Button deleteButton;
        public TextView descriptionTextView;
        public ImageView imageView;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            super(itemView);
            this.descriptionTextView = (TextView) itemView.findViewById(R.id.description_textview);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageview);
            this.deleteButton = (Button) itemView.findViewById(R.id.delete_button);
        }
    }

    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(context).
            inflate(R.layout.item_view_wishlist, parent, false);
        // Return a new holder instance
        return new WishlistAdapter.ViewHolder(itemView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final WishlistAdapter.ViewHolder holder, final int position) {
        // Get the data model based on position
        final Cloth cloth = data.get(position);
        // Set item views based on the data model
        holder.descriptionTextView.setText(cloth.getDescription());

        Picasso.with(context).load(cloth.thumb_url).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onClick(position, cloth.id, holder);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDelete(position, cloth.id, holder);
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