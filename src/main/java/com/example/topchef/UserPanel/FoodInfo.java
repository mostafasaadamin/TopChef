package com.example.topchef.UserPanel;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.example.topchef.Adapters.comments;
import com.example.topchef.Constants;
import com.example.topchef.Database.Database;
import com.example.topchef.Models.rating;
import com.example.topchef.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FoodInfo extends AppCompatActivity implements RatingDialogListener {
    TextView Food_name, Food_price, Food_description;
    ElegantNumberButton order_number;
    android.app.AlertDialog progress;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab, rateFood;
    ImageView food_image;
    RequestQueue rq;
    RatingBar food_rate;
    comments comment_adapter;
    private Database db;
    ArrayList<rating>commentList;
    public static final String DATABASE_NAME = "prod";
    SQLiteDatabase mDatabase;
    String id = "", description = "", name = "", price = "", image = "", discount = "";
    RecyclerView comments_recycler;
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
        setContentView(R.layout.activity_food_info);
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        db = new Database(this);
        commentList=new ArrayList<>();
        rq = Volley.newRequestQueue(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Food_name = findViewById(R.id.food_name);
        Food_price = findViewById(R.id.food_price);
        order_number = findViewById(R.id.number_button);
        Food_description = findViewById(R.id.food_description);
        food_rate = findViewById(R.id.rating_food);
        rateFood = findViewById(R.id.fab_rating);
        comments_recycler=findViewById(R.id.comment_Recycler_view);
        comments_recycler .setLayoutManager(new LinearLayoutManager(FoodInfo.this));
        comments_recycler.setHasFixedSize(true);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        fab = findViewById(R.id.fab);
        progress = new SpotsDialog(this);
        progress.setTitle("Posting feedback");
        food_image = findViewById(R.id.food_img_info);
        id = getIntent().getExtras().getString("food_id");
        description = getIntent().getExtras().getString("Food_des");
        name = getIntent().getExtras().getString("Food_name");
        price = getIntent().getExtras().getString("Food_price");
        image = getIntent().getExtras().getString("Food_image");
        discount = getIntent().getExtras().getString("discount");
        Food_name.setText(name);
        getRates();
        Food_price.setText(price);
        Food_description.setText(description);
        Picasso.with(this).load(image).placeholder(R.drawable.loginbackk).error(R.drawable.loginbackk).fit().into(food_image);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantit = order_number.getNumber();
                addorder(name, price, quantit, discount, id,image);
                MDToast mdToast = MDToast.makeText(getBaseContext(), "Added To Cart", 2000, MDToast.TYPE_SUCCESS);
                mdToast.show();
            }
        });
        //show rating dialog
        rateFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();

            }
        });
       getComents(id);

    }

    private void getComents(String id) {
        String url= Constants.Get_Comments_LINK.concat(id);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
              //  cat_list.clear();
                for (int i = 0; i < response.length(); i++) {
                    rating comment_model = new rating();
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        comment_model .setFoodid(jsonObject.getString("FoodId"));
                        comment_model .setRatingvalue(jsonObject.getString("ratingvalue"));
                        comment_model .setComment(jsonObject.getString("comment"));
                        comment_model .setUserphone(jsonObject.getString("userphone"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                   commentList.add(comment_model );
                }
                if (commentList.size() > 0) {

                    comment_adapter = new comments(commentList, getApplicationContext());
                   comments_recycler.setAdapter(comment_adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

           }
        });
        rq.add(jsonArrayRequest);
    }

    private void showDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setDefaultRating(2)
                .setTitle("Rate this Food")
                .setDescription("Please select some stars and give your feedback")
                .setDefaultComment("This food  is  very good !")
                .setStarColor(R.color.starColor)
                .setNoteDescriptionTextColor(R.color.noteDescriptionTextColor)
                .setTitleTextColor(R.color.titleTextColor)
                .setDescriptionTextColor(R.color.darkGreen)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.hintTextColor)
                .setCommentTextColor(R.color.commentTextColor)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.MyDialogFadeAnimatio)
                .create(FoodInfo.this)
                .show();
    }

    private void addorder(String name, String Price, String quantity, String discount, String id,String image) {
        long idd = db.insertProduct(name, Price, quantity, discount, id,image);
    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {
        sendDataToServer(i, s, id);
    }

    private void sendDataToServer(final int i, final String s, final String id) {
        SharedPreferences sharedpreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
        final String phone = sharedpreferences.getString("phone", "111");
        if (phone.equals("111")) {
            Toast.makeText(this, "No  phone founded", Toast.LENGTH_SHORT).show();
            return;

        }
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Post_Rating_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                MDToast mdToast = MDToast.makeText(getBaseContext(), response, 2000, MDToast.TYPE_SUCCESS);
                mdToast.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Log.i("dars", "" + error);
                MDToast mdToast = MDToast.makeText(getBaseContext(), error + "error happend", 2000, MDToast.TYPE_ERROR);
                mdToast.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> values = new HashMap<String, String>();
                values.put("FoodId", id);
                values.put("phonenumber", phone);
                values.put("Rvalue", String.valueOf(i));
                values.put("comment", s);
                return values;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }
    public void getRates() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Get_Rating_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    float rate = Float.parseFloat(response);
                    food_rate.setRating(rate);
                }
                catch(NumberFormatException ex)
                {
                    Toast.makeText(FoodInfo.this, response, Toast.LENGTH_SHORT).show();
                }
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);

}
}