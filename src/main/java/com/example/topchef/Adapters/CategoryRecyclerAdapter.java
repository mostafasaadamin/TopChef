package com.example.topchef.Adapters;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.topchef.Models.categorymodel;
import com.example.topchef.R;
import com.example.topchef.UserPanel.foodDetails;
import com.example.topchef.ViewHolder.categoryviewholder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<categoryviewholder> {
    ArrayList<categorymodel> catList = new ArrayList<>();
    Context context;
    public CategoryRecyclerAdapter(ArrayList<categorymodel> catList, Context context) {
        this.catList = catList;
        this.context = context;
    }
    @Override
    public categoryviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View form = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorycardview, parent, false);
        categoryviewholder sesstion = new categoryviewholder(form);
        return sesstion;
    }
    @Override
    public void onBindViewHolder(categoryviewholder holder, final int position) {
        final categorymodel po = catList.get(position);

        holder.type_name.setText(po.getType());
        Picasso.with(context).load(po.getImage()).placeholder(R.drawable.loginbackk).fit().into( holder.cat_image);
        holder.items.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          Intent i=new Intent(context, foodDetails.class);
          i.putExtra("Category",po.getType());
          i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          context.startActivity(i);
          //Toast.makeText(context, position+"", Toast.LENGTH_SHORT).show();

      }
  });
    }
    @Override
    public int getItemCount() {
        return catList.size();
    }

}