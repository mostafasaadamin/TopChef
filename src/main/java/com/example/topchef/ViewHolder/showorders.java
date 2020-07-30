package com.example.topchef.ViewHolder;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topchef.R;


/**
 * Created by unknown on 6/9/2018.
 */

public class showorders extends RecyclerView.ViewHolder{
    public TextView order_id,order_status,order_address,order_phone;
    public CardView order_items;
    public Button cancel_order;
    public showorders(View itemView) {
        super(itemView);
        order_id=itemView.findViewById(R.id.order_id);
        order_address=itemView.findViewById(R.id.order_address);
        order_status=itemView.findViewById(R.id.order_status);
        order_phone=itemView.findViewById(R.id.order_phone);
        order_items=itemView.findViewById(R.id.order_items);
        cancel_order=itemView.findViewById(R.id.cancel_order);
    }
}