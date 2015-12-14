package com.bilkent.findnwear.Utilities;

import android.support.v7.widget.RecyclerView;

public interface OnItemClickListener {
    void onClick(int position, int id, RecyclerView.ViewHolder v);
    void onDelete(int position, int id, RecyclerView.ViewHolder v);
}