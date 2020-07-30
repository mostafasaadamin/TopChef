package com.example.topchef.UserPanel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.topchef.Adapters.showordersAdapter;
import com.example.topchef.Constants;
import com.example.topchef.Models.showordersmodel;
import com.example.topchef.NullOnEmptyConverterFactory;
import com.example.topchef.R;
import com.example.topchef.retrofit.connection;
import com.tapadoo.alerter.Alerter;
import com.valdesekamdem.library.mdtoast.MDToast;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowOrders extends AppCompatActivity {
    RecyclerView orders_recycler;
    RequestQueue rq;
    showordersAdapter adapter;
   public AVLoadingIndicatorView loader;
    ArrayList<showordersmodel> order_list = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("f.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        setContentView(R.layout.activity_show_orders);
        rq = Volley.newRequestQueue(this);
        loader = findViewById(R.id.avi);
        loader.show();
        orders_recycler = findViewById(R.id.orders_recycler);
        orders_recycler.setLayoutManager(new LinearLayoutManager(ShowOrders.this));
        orders_recycler.setHasFixedSize(true);
        getOrdersInfo();
    }
    private void getOrdersInfo() {
        String url = "";
        SharedPreferences sharedpreferences = getApplication().getSharedPreferences("email", Context.MODE_PRIVATE);
        String phone = sharedpreferences.getString("phone", "null");
        if (!phone.equals("null")) {
            url = Constants.get_orders_url.concat(phone);
            Log.i("url", "getOrdersInfo: "+url);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://topchef.000webhostapp.com/topchef/")
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(foodDetails.getUnsafeOkHttpClient().build())
                .build();
        connection getFoods = retrofit.create(connection.class);
        Call<ArrayList<showordersmodel>> connection = getFoods.getAllorders(url);
        connection.enqueue(new Callback<ArrayList<showordersmodel>>() {
            @Override
            public void onResponse(Call<ArrayList<showordersmodel>> call, retrofit2.Response<ArrayList<showordersmodel>> response) {
                try {
                    order_list = response.body();
                    if (order_list.size() > 0) {
                        loader.hide();
                        adapter = new showordersAdapter(order_list, getApplicationContext(),ShowOrders.this);
                        orders_recycler.setAdapter(adapter);
                    } else {
                        alerter("No Orders  to show", R.color.darkRed);
                    }
                } catch (NullPointerException ex) {

                }
                loader.hide();
            }
            @Override
            public void onFailure(Call<ArrayList<showordersmodel>> call, Throwable t) {
                loader.hide();
                MDToast mdToast = MDToast.makeText(getBaseContext(), "you dont have any food yet!!", 2000, MDToast.TYPE_ERROR);
                mdToast.show();
                t.printStackTrace();
                Log.i("error", "onResponse: " + t.getMessage());
            }
        });
    }
    public void alerter(String v, int c) {
        Alerter.create(ShowOrders.this)
                .setTitle("alert")
                .setText(v)
                .setBackgroundColorRes(c)
                .setDuration(10000)
                .setIcon(R.drawable.alarm)
                .show();
    }
}
