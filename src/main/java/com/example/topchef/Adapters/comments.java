package com.example.topchef.Adapters;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.topchef.Models.rating;
import com.example.topchef.R;
import com.example.topchef.ViewHolder.commentviewholder;

import java.util.ArrayList;
public class comments extends RecyclerView.Adapter<commentviewholder> {
    ArrayList<rating>commentList = new ArrayList<>();
    Context context;
    public comments(ArrayList<rating> catList, Context context) {
        this.commentList = catList;
        this.context = context;
    }
    @Override
    public commentviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View form = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_card, parent, false);
        commentviewholder item = new commentviewholder(form);
        return item;
    }
    @Override
    public void onBindViewHolder(commentviewholder holder, final int position) {
        final rating po = commentList.get(position);
        holder.comment.setText(po.getComment());
        holder.user_phone.setText(po.getUserphone());
        try
        {
            float rate = Float.parseFloat(po.getRatingvalue());
            holder.food_rate.setRating(rate);
        }
        catch(NumberFormatException ex)
        {
            Log.e("formaterror", ex.getMessage() );
        }
    }
    @Override
    public int getItemCount() {
        return commentList.size();
    }

}

