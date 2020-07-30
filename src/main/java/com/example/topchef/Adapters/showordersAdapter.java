package com.example.topchef.Adapters;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.topchef.Models.showordersmodel;
import com.example.topchef.R;
import com.example.topchef.Track_shipper;
import com.example.topchef.UserPanel.foodDetails;
import com.example.topchef.ViewHolder.showorders;
import com.example.topchef.retrofit.connection;
import com.valdesekamdem.library.mdtoast.MDToast;
import java.util.ArrayList;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.android.volley.VolleyLog.TAG;
public class showordersAdapter extends RecyclerView.Adapter<showorders> {
    ArrayList<showordersmodel> catList = new ArrayList<>();
    Context context;
    Activity myactivity;
    android.app.AlertDialog progress;
    public showordersAdapter(ArrayList<showordersmodel> catList, Context context,Activity myactivity) {
        this.catList = catList;
        Paper.init(context);
        this.context = context;
        this.myactivity=myactivity;
        }

    @Override
    public showorders onCreateViewHolder(ViewGroup parent, int viewType) {
        View form = LayoutInflater.from(parent.getContext()).inflate(R.layout.showorderscard, parent, false);
        showorders item = new showorders(form);
        return item;
    }

    @Override
    public void onBindViewHolder(final showorders holder, final int position) {
        final showordersmodel po = catList.get(position);
        holder.order_address.setText(po.getAddress());
        holder.order_id.setText(po.getId());
        holder.order_phone.setText(po.getPhone());
        holder.order_status.setText(po.getStatus());
        holder.cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelorder(po.getId(), holder.getAdapterPosition());
            }
        });
        holder.order_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().write("order_id", po.getId());
                Intent i = new Intent(context, Track_shipper.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    private void cancelorder(String id, final int po) {
        progress = new SpotsDialog(myactivity);
        progress.setTitle("deleting");
        progress.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://topchef.000webhostapp.com/topchef/")
                .client(foodDetails.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        connection updataorders = retrofit.create(connection.class);
        Call<ResponseBody> connection = updataorders.cancelOrder(id);
        connection.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                MDToast mdToast = MDToast.makeText(context, "order canceled", 2000, MDToast.TYPE_SUCCESS);
                mdToast.show();
                catList.remove(po);
                notifyItemRemoved(po);
                notifyItemRangeRemoved(po, getItemCount());
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                if (t.getMessage().equals("Use JsonReader.setLenient(true) to accept malformed JSON at line 2 column 1 path $")) {
                    MDToast mdToast = MDToast.makeText(context, "order canceled", 2000, MDToast.TYPE_SUCCESS);
                    mdToast.show();
                } else {
                    MDToast.makeText(context, t.getMessage(), 2000, MDToast.TYPE_ERROR).show();
                }
                progress.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }
}
