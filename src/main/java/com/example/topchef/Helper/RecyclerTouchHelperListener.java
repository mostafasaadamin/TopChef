package com.example.topchef.Helper;


import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by unknown on 10/9/2018.
 */

public interface RecyclerTouchHelperListener {
    void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
