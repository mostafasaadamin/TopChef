package com.example.topchef.ViewHolder;


import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.topchef.R;

public class commentviewholder extends RecyclerView.ViewHolder{
    public TextView user_phone,comment;
    public RatingBar food_rate;
    public commentviewholder(View itemView) {
        super(itemView);
        user_phone=itemView.findViewById(R.id.user_phone);
        food_rate=itemView.findViewById(R.id.rating_bar);
        comment=itemView.findViewById(R.id.comment);
         }
}
