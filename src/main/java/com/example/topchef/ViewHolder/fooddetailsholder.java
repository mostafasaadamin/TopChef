package com.example.topchef.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topchef.R;


/**
 * Created by unknown on 5/13/2018.
 */

public class fooddetailsholder extends RecyclerView.ViewHolder{
    public TextView name;
    public ImageView food_image,favourite_Btn,share_Btn;
    public CardView items;
    public fooddetailsholder(View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.foo_name);
        food_image=itemView.findViewById(R.id.food_image);
        items=itemView.findViewById(R.id.f_details);
        favourite_Btn=itemView.findViewById(R.id.favourite_Btn);
        share_Btn=itemView.findViewById(R.id.share_Btn);
    }
}

