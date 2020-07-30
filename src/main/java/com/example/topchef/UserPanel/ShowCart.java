package com.example.topchef.UserPanel;
import android.Manifest;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.topchef.Adapters.cartAdapter;
import com.example.topchef.Constants;
import com.example.topchef.Database.Database;
import com.example.topchef.Helper.RecyclerItemTouchHelper;
import com.example.topchef.Helper.RecyclerTouchHelperListener;
import com.example.topchef.Models.order;
import com.example.topchef.Models.ordermodel;
import com.example.topchef.R;
import com.example.topchef.ViewHolder.cart;
import com.example.topchef.config;
import com.example.topchef.getLocation;
import com.example.topchef.retrofit.connection;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.security.ProviderInstaller;
import com.google.gson.Gson;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.SSLContext;

import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowCart extends AppCompatActivity implements RecyclerTouchHelperListener {
    PlaceAutocompleteFragment address;
    private static final int REQUEST_INTERNET = 200;
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;
    String homeAddress = "non";
    RecyclerView items;
    cartAdapter adapter;
    ArrayList<order> cart_list = new ArrayList<>();
    ArrayList<ordermodel> cart_orders = new ArrayList<>();
    TextView toatal_price;
    Button placeOrder;
    String orderrs;
    String addres = "unavailable";
    Place AutocompletePlace;
    public static final int PayPAl_RequestCode = 111;
    Database db;
    final String user_Key="key";
    android.app.AlertDialog progress;
    Intent service;
    private IntentFilter mIntentFilter;
//    static PayPalConfiguration Config = new PayPalConfiguration()
//            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
//            .clientId(config.PAyPAl_Cient_ID);

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
        setContentView(R.layout.activity_show_cart);
        Paper.init(this);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.example.unknown.topchef.UserPanel");
        progress = new SpotsDialog(this);
        progress.setTitle("Posting order");
//        Intent i = new Intent(this, PayPalService.class);
//        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, Config);
//        startService(i);
        db = new Database(this);
        items = findViewById(R.id.cart_recycler);
        toatal_price = findViewById(R.id.toatal_price);
        placeOrder = findViewById(R.id.btnPlaceOrder);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(toatal_price.getText().toString().trim())) {
                    String price = toatal_price.getText().toString();
                    showDiallogforAddress(price);
                }
            }
        });
        items.setLayoutManager(new LinearLayoutManager(ShowCart.this));
        items.setHasFixedSize(true);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(items);
        LoadcartItems();
       // orderLocation();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_INTERNET);
        }
        else {
            if (checkforplayservices()) {
                service = new Intent(this, getLocation.class);
                startService(service);
            } else {
                MDToast.makeText(getApplicationContext(), "No play Service Founded ??", 2000, MDToast.TYPE_ERROR).show();

            }
        }



    }
    private boolean checkforplayservices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }
    private void showDiallogforAddress(final String price) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("One more step");
            builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
            final LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.getcustomerinfo, null);
            builder.setView(dialogView);
            address = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
            address.getView().findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);
            ((EditText) address.getView().findViewById(R.id.place_autocomplete_search_input)).setHint("Enter your address");
            ((EditText) address.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(14);
            address.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    AutocompletePlace = place;
                }

                @Override
                public void onError(Status status) {
                    Log.e("error", status.getStatusMessage());
                }
            });
            final EditText Notes = dialogView.findViewById(R.id.Notes);
            final RadioButton ship_to_Address = dialogView.findViewById(R.id.rdb_ship_to_this_address);
            final RadioButton Home_Address = dialogView.findViewById(R.id.rdb_Home_address);
            Home_Address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        SharedPreferences sharedpreferences = getApplication().getSharedPreferences("Address", Context.MODE_PRIVATE);
                        homeAddress = sharedpreferences.getString("address", "non");
                        if (!homeAddress.equals("non")) {
                            ((EditText) address.getView().findViewById(R.id.place_autocomplete_search_input)).setText(homeAddress);
                        } else {
                            Toast.makeText(ShowCart.this, "No Saved Address found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            ship_to_Address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        if (!addres.equals("unavailable")) {
                            ((EditText) address.getView().findViewById(R.id.place_autocomplete_search_input)).setText(addres);
                        } else {
                            MDToast mdToast = MDToast.makeText(getBaseContext(), "Cant get your location try again", 2000, MDToast.TYPE_ERROR);
                            mdToast.show();
                        }
                    }
                }

            });
            builder.setCancelable(false);
            builder.setPositiveButton("cash on delivery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String phone = Paper.book().read(user_Key);
                    if (currentLatitude == 0.0 || currentLongitude == 0.0) {
                        MDToast.makeText(getBaseContext(), "Can not get your location check your connection!!", 2000, MDToast.TYPE_WARNING).show();
                        getFragmentManager().beginTransaction().remove(address).commit();
                        return;
                    }
                    Log.i("data", "onClick: " + ship_to_Address.isChecked() + Notes.getText() + addres + phone);
                    if (cart_list.size() == 0) {
                        MDToast mdToast = MDToast.makeText(getBaseContext(), "No cart item!!", 2000, MDToast.TYPE_ERROR);
                        mdToast.show();
                        getFragmentManager().beginTransaction().remove(address).commit();
                        return;
                    }
                    if (AutocompletePlace != null && !TextUtils.isEmpty(Notes.getText().toString().trim()) && !ship_to_Address.isChecked() && phone != null && !Home_Address.isChecked()) {
                        String addresstext = AutocompletePlace.getAddress().toString();
                        LatLng Lat_lng = AutocompletePlace.getLatLng();
                        String lat = String.valueOf(Lat_lng.latitude);
                        String Lng = String.valueOf(Lat_lng.longitude);
                        sendDataToServer(addresstext, Notes.getText().toString(), phone, lat, Lng, price, false, "not approved");
                        getFragmentManager().beginTransaction().remove(address).commit();
                    } else if (ship_to_Address.isChecked() && !TextUtils.isEmpty(Notes.getText().toString().trim()) && !addres.equals("unavailable") && phone != null) {
                        sendDataToServer(addres, Notes.getText().toString(), phone, String.valueOf(currentLatitude), String.valueOf(currentLongitude), price, false, "not approved");
                        getFragmentManager().beginTransaction().remove(address).commit();
                    } else if (Home_Address.isChecked() && phone != null && !TextUtils.isEmpty(Notes.getText().toString().trim())) {
                        if (homeAddress != null) {
                            sendDataToServer(homeAddress, Notes.getText().toString(), phone, String.valueOf(currentLatitude), String.valueOf(currentLongitude), price, false, "not approved");
                            getFragmentManager().beginTransaction().remove(address).commit();
                        } else {
                            MDToast mdToast = MDToast.makeText(getBaseContext(), "No Home Address Found", 2000, MDToast.TYPE_ERROR);
                            mdToast.show();
                        }
                    } else {
                        getFragmentManager().beginTransaction().remove(address).commit();
                        MDToast mdToast = MDToast.makeText(getBaseContext(), "Please Fill All info or select from radio buttons", 2000, MDToast.TYPE_ERROR);
                        mdToast.show();
                    }
                }
            })
                    .setNeutralButton("paypal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            MDToast mdToast = MDToast.makeText(getBaseContext(), "coming sooooon!!", 2000, MDToast.TYPE_WARNING);
                            mdToast.show();
                            getFragmentManager().beginTransaction().remove(address).commit();

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    getFragmentManager().beginTransaction().remove(address).commit();

                }
            });
            builder.show();
        }catch(InflateException ex){}
        }
    private void sendDataToServer(final String address, final String notes,final String phone, final String lat, final String lng, String price, Boolean payment, final String PaymentState) {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.orders_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("error")) {
                            MDToast mdToast = MDToast.makeText(getBaseContext(), "Error in submitting your order try again", 2000, MDToast.TYPE_ERROR);
                            mdToast.show();
                            return;
                        }
                        MDToast mdToast = MDToast.makeText(getBaseContext(), "your order had been submitted successfully", 2000, MDToast.TYPE_SUCCESS);
                        mdToast.show();
                        sendNotification("new Order had been submited");
                        Log.d("Rere", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MDToast mdToast = MDToast.makeText(getBaseContext(), error.getMessage(), 2000, MDToast.TYPE_ERROR);
                        mdToast.show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                orderrs = new Gson().toJson(cart_orders);//cart_orders array of object
                params.put("Notes", notes);
                params.put("phonenumber", phone);
                params.put("Address", address);
                params.put("latitude", lat);
                params.put("lngtitude", lng);
                params.put("orderList", orderrs);
                params.put("paymentstate", PaymentState);
                params.put("status", "placed");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
//    private void startPayment(String price) {
//        String fromatamount = price.replace("$", "").replace(",", "");
//        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(fromatamount)
//                ,
//                "LE",
//                "Top Chef App order",
//                PayPalPayment.PAYMENT_INTENT_SALE);
//        Intent i = new Intent(getApplicationContext(), PaymentActivity.class);
//        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, Config);
//        i.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
//        startActivityForResult(i, PayPAl_RequestCode);
//    }
    @Override
    protected void onResume() {
        registerReceiver(mReceiver, mIntentFilter);
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");
        unregisterReceiver(mReceiver);

    }

    private void LoadcartItems() {
        double sum = 0.0;
        cart_list = db.getAllNotes();
        adapter = new cartAdapter(cart_list, getApplicationContext());
        items.setAdapter(adapter);
        for (int i = 0; i < cart_list.size(); i++) {
            cart_orders.add(new ordermodel(cart_list.get(i).getId(), cart_list.get(i).getPrice(), cart_list.get(i).getProductName(), cart_list.get(i).getQuantity(), cart_list.get(i).getDiscount(), cart_list.get(i).getImage_url()));
            double price = Double.parseDouble(cart_list.get(i).getPrice());
            int amount = Integer.parseInt(cart_list.get(i).getQuantity());
            sum += (amount * price);
        }
        orderrs = new Gson().toJson(cart_orders);
        Log.i("ert", orderrs + " ");
        toatal_price.setText(
                NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                        .format(sum) + "");
    }
    private void sendNotification(final String message) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://topchef.000webhostapp.com/topchef/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(foodDetails.getUnsafeOkHttpClient().build())
                .build();
        String title="new order";
        connection Notification = retrofit.create( connection.class);
        Call<responseModel> connection = Notification.sendNotification(title,message);
        connection.enqueue(new Callback<responseModel>(){
            @Override
            public void onResponse(Call<responseModel> call, retrofit2.Response<responseModel> response) {
                responseModel model=response.body();
                if(model.getSuccess().equals("1"))
                {
                    MDToast mdToast = MDToast.makeText(getBaseContext(),"notification sent", 2000, MDToast.TYPE_SUCCESS);
                    mdToast.show();
                }
                else
                {
                    MDToast mdToast = MDToast.makeText(getBaseContext(),"failed", 2000, MDToast.TYPE_ERROR);
                    mdToast.show();
                }
                progress.dismiss();
            }
            @Override
            public void onFailure(Call<responseModel> call, Throwable t) {
                if(t.getMessage().contains("to accept malformed JSON at line 2 column 1 path $"))
                {
                    MDToast mdToast = MDToast.makeText(getBaseContext(),"notification sent", 2000, MDToast.TYPE_SUCCESS);
                    mdToast.show();
                }
                progress.dismiss();
                t.printStackTrace();
                Log.i("error", "onResponse: " + call.toString() + t.getMessage());
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_INTERNET) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                mGoogleApiClient = new GoogleApiClient.Builder(this)
//                        .addApi(LocationServices.API)
//                        .addConnectionCallbacks(this)
//                        .addOnConnectionFailedListener(this)
//                        .build();
//                createLocationRequest();
                if (checkforplayservices()) {
                    startService(new Intent(this, getLocation.class));
                } else {
                    MDToast.makeText(getApplicationContext(), "No play Service Founded ??", 2000, MDToast.TYPE_ERROR).show();

                }
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(this, "Please agree permission:(", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_INTERNET);
                } else {
                    Toast.makeText(this, "Exit :(", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        }
    }
    @Override
    public void onBackPressed() {
        if (address != null) {
            getFragmentManager().beginTransaction().remove(address).commit();
        }
        super.onBackPressed();
    }
    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof cart) {
            order item = cart_list.get(position);
            adapter.removeItem(position);
            new Database(getBaseContext()).deleteNote(item);
            LoadcartItems();
        }
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Double lat = intent.getDoubleExtra("lat", 0);
            Double lng = intent.getDoubleExtra("lng", 0);
            addres =intent.getExtras().getString("Address","unavailable");
            currentLatitude = lat;
            currentLongitude = lng;
            stopService(service);
        }
    };

}