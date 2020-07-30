package com.example.topchef.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.topchef.Models.order;
import com.example.topchef.R;
import com.example.topchef.ViewHolder.cart;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * Created by unknown on 6/7/2018.
 */

public class cartAdapter extends RecyclerView.Adapter<cart> {
    ArrayList<order> catList = new ArrayList<>();
    Context context;
    public cartAdapter(ArrayList<order> catList, Context context) {
        this.catList = catList;
        this.context = context;
    }
    @Override
    public cart onCreateViewHolder(ViewGroup parent, int viewType) {
        View form = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemcard, parent, false);
        cart item = new cart(form);
        return item;
    }
    @Override
    public void onBindViewHolder(cart holder, final int position) {
        final order po = catList.get(position);
        holder.cart_item_name.setText(po.getProductName());
        holder.cart_item_price.setText(po.getPrice());
        Picasso.with(context).load(po.getImage_url()).placeholder(R.drawable.loginbackk).fit().into( holder.food_image);
        TextDrawable drawable = TextDrawable.builder() .beginConfig()
                .textColor(Color.BLACK)
                .useFont(Typeface.DEFAULT)
                .fontSize(15) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRect(po.getQuantity(),context.getResources().getColor(R.color.cartcount));
        holder.cart_item_count.setImageDrawable(drawable);
    }
    @Override
    public int getItemCount() {
        return catList.size();
    }
    public void removeItem(int position) {
        catList.remove(position);
        notifyItemRemoved(position);

    }

}