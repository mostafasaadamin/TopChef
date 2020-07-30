package com.example.topchef;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.topchef.UserPanel.category;
import com.example.topchef.UserPanel.userSignin;
import com.example.topchef.UserPanel.userSignup;
import com.tapadoo.alerter.Alerter;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login extends AppCompatActivity {
    Button Signin, Signup;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("t.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        setContentView(R.layout.activity_login);
        Paper.init(this);
        //  intitLogin();
        Signin = findViewById(R.id.LoginButton);
        Signup = findViewById(R.id.SignupButton);

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i=new Intent(Login.this, userSignin.class);
            startActivity(i);

            }
        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    initAccountKitSmsFlow();
                Intent i = new Intent(Login.this, userSignup.class);
                startActivity(i);
            }
        });

    }



    public void intitLogin() {
        String phone = Paper.book().read("key");
        String pass = Paper.book().read("password");

        if (phone != null && pass != null) {
            Intent i = new Intent(Login.this, category.class);
            userinfo.username = phone;
            startActivity(i);
        }
    }

    public void alerter(String v, int c) {
        Alerter.create(Login.this)
                .setTitle("alert")
                .setText(v)
                .setBackgroundColorRes(c)
                .setDuration(10000)
                .setIcon(R.drawable.alarm)
                .show();
    }

}
