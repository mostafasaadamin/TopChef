package com.example.topchef.UserPanel;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.example.topchef.Adapters.FoodDetailsAdapter;
import com.example.topchef.Constants;
import com.example.topchef.Models.foodDetailsmodel;
import com.example.topchef.NullOnEmptyConverterFactory;
import com.example.topchef.R;
import com.example.topchef.config;
import com.example.topchef.retrofit.connection;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.tapadoo.alerter.Alerter;
import com.valdesekamdem.library.mdtoast.MDToast;
import com.wang.avi.AVLoadingIndicatorView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class foodDetails extends AppCompatActivity {
    String category = "";
    RequestQueue rq;
    RecyclerView foodDetail;
    FoodDetailsAdapter adapter;
    ArrayList<foodDetailsmodel> food_list;
    AVLoadingIndicatorView loader;
    SwipeRefreshLayout refresh;
    config con;

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
        setContentView(R.layout.activity_food_details);
        category = getIntent().getExtras().getString("Category");
        refresh = findViewById(R.id.swipe_food);
        con = new config(getApplicationContext());
        foodDetail = findViewById(R.id.food);
        foodDetail.setLayoutManager(new LinearLayoutManager(foodDetails.this));
        foodDetail.setHasFixedSize(true);
        food_list = new ArrayList<>();
        loader = findViewById(R.id.avii);
        rq = Volley.newRequestQueue(this);
        if (con.haveNetworkConnection()) {
            if (category != null) {
                getfoodDetails(category);
            }
        } else {
            alerter("You have not internet connection", R.color.darkRed);
        }
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary)
                , getResources().getColor(R.color.darkGreen),
                getResources().getColor(R.color.orang),
                getResources().getColor(R.color.darkBlueGrey));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (con.haveNetworkConnection()) {
                    if (category != null) {

                        getfoodDetails(category);
                    }
                } else {
                    alerter("You have not internet connection", R.color.darkRed);
                }
            }
        });

    }

    public void getfoodDetails(final String category) {
        String url = Constants.Food_Link.concat(category);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://topchef.000webhostapp.com/topchef/")
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUnsafeOkHttpClient().build())
                .build();
        connection getFoods = retrofit.create(connection.class);
        Call<ArrayList<foodDetailsmodel>> connection = getFoods.getAllFoods(url);
        connection.enqueue(new Callback<ArrayList<foodDetailsmodel>>() {
            @Override
            public void onResponse(Call<ArrayList<foodDetailsmodel>> call, retrofit2.Response<ArrayList<foodDetailsmodel>> response) {
                food_list = response.body();
                if(food_list!=null) {
                    if (food_list.size() > 0) {
                        loader.hide();
                        refresh.setRefreshing(false);
                        adapter = new FoodDetailsAdapter(food_list, getApplicationContext());
                        foodDetail.setAdapter(adapter);
                    } else {
                        loader.hide();
                        refresh.setRefreshing(false);
                        alerter("No data  to show", R.color.darkRed);
                    }
                }
                refresh.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ArrayList<foodDetailsmodel>> call, Throwable t) {
                loader.hide();
                MDToast mdToast = MDToast.makeText(getBaseContext(), "you dont have any food yet!!", 2000, MDToast.TYPE_ERROR);
                refresh.setRefreshing(false);
                mdToast.show();
                t.printStackTrace();
                Log.i("error", "onResponse: " + t.getMessage());
            }
        });


    }

    public void alerter(String v, int c) {
        Alerter.create(foodDetails.this)
                .setTitle("alert")
                .setText(v)
                .setBackgroundColorRes(c)
                .setDuration(10000)
                .setIcon(R.drawable.alarm)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        foodDetail.setAdapter(null);
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
