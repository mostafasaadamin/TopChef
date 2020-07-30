package com.example.topchef.Adapters;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.RecyclerView;

import com.example.topchef.Database.Database;
import com.example.topchef.Models.foodDetailsmodel;
import com.example.topchef.R;
import com.example.topchef.UserPanel.FoodInfo;
import com.example.topchef.ViewHolder.fooddetailsholder;
import com.squareup.picasso.Picasso;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;

public class FoodDetailsAdapter extends RecyclerView.Adapter<fooddetailsholder> {
    ArrayList<foodDetailsmodel> catList = new ArrayList<>();
    Context context;
    public FoodDetailsAdapter(ArrayList<foodDetailsmodel> catList, Context context) {
        this.catList = catList;
        this.context = context;
    }
    @Override
    public fooddetailsholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View form = LayoutInflater.from(parent.getContext()).inflate(R.layout.fooddetailscardview, parent, false);
        fooddetailsholder card = new fooddetailsholder(form);
        return card;
    }
    @Override
    public void onBindViewHolder(final fooddetailsholder holder, final int position) {
        final foodDetailsmodel po = catList.get(position);
        holder.name.setText(po.getName());
         final Database b=new Database(context);
         boolean status=b.isFavourite(po.getId());
          if(status)
          {
              holder.favourite_Btn.setImageResource(R.drawable.ic_favorite_black_24dp);
          }
        Picasso.with(context).load(po.getImage()).placeholder(R.drawable.loginbackk).fit().into( holder.food_image);
        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, FoodInfo.class);
                i.putExtra("food_id",po.getId());
                i.putExtra("Food_price",po.getPrice());
                i.putExtra("Food_des",po.getDescription());
                i.putExtra("Food_image",po.getImage());
                i.putExtra("Food_name",po.getName());
                i.putExtra("discount",po.getDiscount());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        holder.favourite_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean status=b.isFavourite(po.getId());
                if(!status) {
                    holder.favourite_Btn.setImageResource(R.drawable.ic_favorite_black_24dp);
                    b.addToFavourite(po.getId(),po.getName(),po.getImage(),po.getPrice(),po.getDescription(),po.getDiscount());
                    MDToast mdToast = MDToast.makeText(context, po.getName()+"  Added To Favourite", 2000, MDToast.TYPE_SUCCESS);
                    mdToast.show();
                }else
                    {
                        b.removeFromFavourite(po.getId());
                        holder.favourite_Btn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        MDToast mdToast = MDToast.makeText(context, po.getName()+"  removed from Favourite", 2000, MDToast.TYPE_SUCCESS);
                        mdToast.show();
                    }
            }
        });
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }
}