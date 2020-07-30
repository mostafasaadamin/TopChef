package com.example.topchef.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topchef.R;


/**
 * Created by unknown on 8/4/2018.
 */

public class favouriteviewholder extends RecyclerView.ViewHolder{
    public TextView foodname,foodprice;
    public ImageView food_image,addtocard,remove;
    public CardView items;
    public favouriteviewholder(View itemView) {
        super(itemView);
        foodname=itemView.findViewById(R.id.food_namefavourite);
        foodprice=itemView.findViewById(R.id.food_pricefavourite);
        food_image=itemView.findViewById(R.id.food_img);
        items=itemView.findViewById(R.id.card);
        addtocard=itemView.findViewById(R.id.add_to_cart);
        remove=itemView.findViewById(R.id.remove_from_favourite);
    }
}
