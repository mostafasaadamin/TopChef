package com.example.topchef.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.topchef.Database.Database;
import com.example.topchef.Models.favouriteModel;
import com.example.topchef.R;
import com.example.topchef.UserPanel.FoodInfo;
import com.example.topchef.ViewHolder.favouriteviewholder;
import com.squareup.picasso.Picasso;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;

public class favouriteAdapter  extends RecyclerView.Adapter<favouriteviewholder> {
    private Database db;
    ArrayList<favouriteModel> catList = new ArrayList<>();
    Context context;
    public favouriteAdapter(ArrayList<favouriteModel> catList, Context context) {
        this.catList = catList;
        db = new Database(context);
        this.context = context;
    }
    @Override
    public favouriteviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View form = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_food_card, parent, false);
        favouriteviewholder sesstion = new favouriteviewholder(form);
        return sesstion;
    }
    @Override
    public void onBindViewHolder(final favouriteviewholder holder, final int position) {
        final favouriteModel po = catList.get(position);
        holder.foodname.setText(po.getFoodName());
        holder.foodprice.setText(po.getPrice());
        Picasso.with(context)
                .load(po.getImageURl())
                .placeholder(R.drawable.loginbackk)
                .error(R.drawable.loginbackk)
                .resize(70,72)
                .into(holder.food_image);

        holder.items.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent i=new Intent(context, FoodInfo.class);
                i.putExtra("food_id",po.getFoodID());
                i.putExtra("Food_price",po.getPrice());
                i.putExtra("Food_des",po.getDescription());
                i.putExtra("Food_image",po.getImageURl());
                i.putExtra("Food_name",po.getFoodName());
                i.putExtra("discount",po.getDiscount());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                return false;
            }
        });
        holder.addtocard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long idd = db.insertProduct(po.getFoodName(),po.getPrice(), "1", "0",po.getFoodID(),po.getImageURl());
                MDToast mdToast = MDToast.makeText(context, "Added To Cart", 2000, MDToast.TYPE_SUCCESS);
                mdToast.show();
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.removeFromFavourite(po.getFoodID());
                favouriteModel posit = catList.get(position);
                catList.remove(posit);
                notifyItemRemoved(position);
                notifyItemRangeRemoved(position, getItemCount());

                MDToast mdToast = MDToast.makeText(context, "removed from favourite", 2000, MDToast.TYPE_SUCCESS);
                mdToast.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return catList.size();
    }

}