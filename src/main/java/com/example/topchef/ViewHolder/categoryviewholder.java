package com.example.topchef.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topchef.R;


public class categoryviewholder extends RecyclerView.ViewHolder{
public TextView type_name;
public ImageView cat_image;
    public CardView items;
public categoryviewholder(View itemView) {
        super(itemView);
        type_name=itemView.findViewById(R.id.type_name);
        cat_image=itemView.findViewById(R.id.cat_image);
        items=itemView.findViewById(R.id.item);
    }
}
