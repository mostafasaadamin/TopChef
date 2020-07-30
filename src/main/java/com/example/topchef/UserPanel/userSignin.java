package com.example.topchef.UserPanel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.topchef.Constants;
import com.example.topchef.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tapadoo.alerter.Alerter;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class userSignin extends AppCompatActivity {
    Button Login;
    android.app.AlertDialog dialog;
     EditText phone, password;
    final String user_Key="key";
    final String user_pass="password";
    final String user_phone = "phonenumber";
    final String user_password = "password";
     CheckBox chb_remember;
     String token="";
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
        setContentView(R.layout.activity_user_signin);
        Paper.init(this);
        TextView text=findViewById(R.id.formLogo);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "f.ttf");
        text.setTypeface(face);
        phone = findViewById(R.id.loginphone);
        password = findViewById(R.id.loginpassword);
        Login = findViewById(R.id.LoginB);
        chb_remember=findViewById(R.id.chb_remember);
        dialog = new SpotsDialog(this);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Loginprocess();

            }
        });
    }
    public void Loginprocess() {
        if (check()) {
            SharedPreferences sharedpreferences=getApplication().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
            token=sharedpreferences.getString("token","null");
            if(token.equals("null"))
            {
                alerter("No Token Created please try again..",R.color.darkRed);
                return;
            }
            dialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Login_LINK, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    if (response.contains("sucessfully")) {
                        Paper.book().write(user_Key,phone.getText().toString());
                        Paper.book().write(user_pass,password.getText().toString());
                        Intent i = new Intent(userSignin.this, category.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        SharedPreferences sharedpreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor;
                        editor = sharedpreferences.edit();
                        editor.putString("phone",phone.getText().toString());
                        editor.commit();
                        startActivity(i);
                        finish();
                    }
                    else
                        {
                        alerter(response, R.color.darkRed);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Log.i("dars", "" + error);
                    alerter(error.getMessage(), R.color.darkRed);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> values = new HashMap<String, String>();
                    values.put(user_password, password.getText().toString());
                    values.put(user_phone, phone.getText().toString());
                    values.put("token",token);
                    return values;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            alerter("Please Enter all required Filleds", R.color.darkRed);
        }
    }
    public boolean check() {
        if (TextUtils.isEmpty(phone.getText().toString())) {
            return false;
        } else if (TextUtils.isEmpty(password.getText().toString())) {
            return false;
        }
        return true;
    }
    public void alerter(String v, int c) {
        Alerter.create(userSignin.this)
                .setTitle("alert")
                .setText(v)
                .setBackgroundColorRes(c)
                .setDuration(10000)
                .setIcon(R.drawable.alarm)
                .show();
    }
}
