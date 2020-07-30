package com.example.topchef.UserPanel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.andremion.counterfab.CounterFab;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.example.topchef.Adapters.CategoryRecyclerAdapter;
import com.example.topchef.Adapters.swipeAdapter;
import com.example.topchef.Constants;
import com.example.topchef.Database.Database;
import com.example.topchef.Models.categorymodel;
import com.example.topchef.Models.order;
import com.example.topchef.Models.siderModel;
import com.example.topchef.R;
import com.example.topchef.config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.huxq17.swipecardsview.SwipeCardsView;
import com.tapadoo.alerter.Alerter;
import com.valdesekamdem.library.mdtoast.MDToast;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class category extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RequestQueue rq;
    ArrayList<categorymodel> cat_list = new ArrayList<>();
    CategoryRecyclerAdapter adapter;
    RecyclerView categoryRecycler;
    AVLoadingIndicatorView loader;
    SwipeRefreshLayout refresh;
    config con;
    SwipeCardsView swipe;
    FloatingActionButton offers;
    Database db;
    ArrayList<siderModel> images;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("a.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        setContentView(R.layout.activity_category);
        Paper.init(this);
        db = new Database(this);
        images = new ArrayList<>();
        rq = Volley.newRequestQueue(this);
        loader = findViewById(R.id.avi);
        swipe = findViewById(R.id.swipecard);
        offers = findViewById(R.id.offers);
        swipe.retainLastCard(false);
        swipe.enableSwipe(true);
        swipe.animate().alpha(1);
        refresh = findViewById(R.id.swipe);
        loader.show();
        con = new config(getApplicationContext());
        categoryRecycler = findViewById(R.id.recycle_menu);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(category.this));
        categoryRecycler.setHasFixedSize(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        if (con.haveNetworkConnection()) {
            setSliderImages();
            getCategorys();
        } else {
            alerter("You have not internet connection", R.color.darkRed);
        }
        setSupportActionBar(toolbar);
        CounterFab fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(category.this, ShowCart.class);
                startActivity(i);
            }
        });
        ArrayList<order> arr = db.getAllNotes();
        fab.setCount(arr.size());
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary)
                , getResources().getColor(R.color.darkGreen),
                getResources().getColor(R.color.orang),
                getResources().getColor(R.color.darkBlueGrey));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (con.haveNetworkConnection()) {
                    setSliderImages();
                    getCategorys();
                } else {
                    alerter("You have not internet connection", R.color.darkRed);
                }
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeAdapter adapter = new swipeAdapter(images, category.this);
                swipe.setAdapter(adapter);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_orders) {
            Intent i = new Intent(category.this, ShowOrders.class);
            startActivity(i);
        } else if (id == R.id.nav_logout)

        {
            Paper.book().destroy();
            Intent i = new Intent(category.this, userSignin.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else if (id == R.id.nav_favourite) {
            Intent i = new Intent(category.this, favourite_food.class);
            startActivity(i);
        } else if (id == R.id.nav_cart) {
            Intent i = new Intent(category.this, ShowCart.class);
            startActivity(i);
        } else if (id == R.id.Home_addres) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter your address");
            builder.setIcon(R.drawable.ic_home_black_24dp);
            final LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.address_pop_up, null);
            final EditText address = dialogView.findViewById(R.id.homeAddress);
            builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (!TextUtils.isEmpty(address.getText().toString().trim())) {
                        SharedPreferences sharedpreferences = getSharedPreferences("Address", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor;
                        editor = sharedpreferences.edit();
                        editor.putString("address", address.getText().toString());
                        editor.apply();
                        MDToast mdToast = MDToast.makeText(getBaseContext(), "data updated successfully", 2000, MDToast.TYPE_SUCCESS);
                        mdToast.show();
                    } else {
                        Toast.makeText(category.this, "Enter your address", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setView(dialogView);
            builder.show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getCategorys() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Constants.Category_LINK, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                cat_list.clear();
                for (int i = 0; i < response.length(); i++) {
                    categorymodel cat_model = new categorymodel();
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        cat_model.setType(jsonObject.getString("type"));
                        cat_model.setImage(jsonObject.getString("image"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    cat_list.add(cat_model);
                }
                if (cat_list.size() > 0) {
                    loader.hide();
                    refresh.setRefreshing(false);
                    adapter = new CategoryRecyclerAdapter(cat_list, getApplicationContext());
                    categoryRecycler.setAdapter(adapter);
                    if (images != null) {
                        if (images.size() > 0) {
                            swipeAdapter adapter = new swipeAdapter(images, category.this);
                            swipe.setAdapter(adapter);

                        }

                    }
                } else {
                    alerter("No Category  to show", R.color.darkRed);
                    refresh.setRefreshing(false);
                    loader.hide();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.hide();
                refresh.setRefreshing(false);
                alerter("Error while loading data please check your connection", R.color.darkRed);
            }
        });
        rq.add(jsonArrayRequest);
    }

    private void setSliderImages() {
        images.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Constants.Post_banner_LINK, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    siderModel sider_model = new siderModel();
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        sider_model.setFoodname(jsonObject.getString("foodnamebanner"));
                        sider_model.setFoodimage(jsonObject.getString("foodimagebanner"));
                        sider_model.setFoodid(jsonObject.getString("foodid"));
                        images.add(sider_model);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        rq.add(jsonArrayRequest);
    }

    public void alerter(String v, int c) {
        Alerter.create(category.this)
                .setTitle("alert")
                .setText(v)
                .setBackgroundColorRes(c)
                .setDuration(10000)
                .setIcon(R.drawable.alarm)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        categoryRecycler.setAdapter(null);
    }

}
