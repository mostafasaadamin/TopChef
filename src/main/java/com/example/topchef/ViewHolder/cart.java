package com.example.topchef.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topchef.R;


/**
 * Created by unknown on 6/8/2018.
 */

public class cart  extends RecyclerView.ViewHolder{
    public TextView cart_item_name,cart_item_price,cart_description;
    public ImageView cart_item_count,food_image;
    public CardView view_forground;
    public  RelativeLayout View_background;
    public cart(View itemView) {
        super(itemView);
        cart_item_name=itemView.findViewById(R.id.name);
        cart_item_price=itemView.findViewById(R.id.price);
        cart_item_count=itemView.findViewById(R.id.cart_item_count);
      //  cart_description=itemView.findViewById(R.id.description);
        food_image=itemView.findViewById(R.id.thumbnail);
        view_forground=itemView.findViewById(R.id.view_foreground);
        View_background=itemView.findViewById(R.id.view_background);

    }
}