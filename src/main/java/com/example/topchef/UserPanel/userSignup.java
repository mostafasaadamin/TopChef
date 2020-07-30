package com.example.topchef.UserPanel;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.topchef.Constants;
import com.example.topchef.R;
import com.example.topchef.retrofit.connection;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tapadoo.alerter.Alerter;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.content.ContentValues.TAG;
import static com.example.topchef.UserPanel.foodDetails.getUnsafeOkHttpClient;

public class userSignup extends AppCompatActivity {
    Button Signup;
    String token = "Notoken";
    android.app.AlertDialog dialog;
    EditText phone, password, name;

    final static int APP_REQUEST_CODE = 111;
    boolean phone_check = false;
    String phoneNumberString = "";

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
        setContentView(R.layout.activity_user_signup);
        Paper.init(this);
        getToken();
        name = findViewById(R.id.signupname);
        phone = findViewById(R.id.signupphone);
        password = findViewById(R.id.signuppassword);
        Signup = findViewById(R.id.SignupB);
        dialog = new SpotsDialog(this);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Loginprocess();
            }
        });
//        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (b) {
//                    initAccountKitSmsFlow();
//                }
//            }
//        });
    }

    public void Loginprocess() {
        SharedPreferences sharedpreferences = getApplication().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sharedpreferences.getString("token", "null");
        if (token.equals("null")) {
            alerter("No Token Created please try again..", R.color.darkRed);
            return;
        }
        if (check()) {

            dialog.show();

//            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Signup_LINK, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    dialog.dismiss();
//                    if (response.contains("sucessfully")) {
//                        SharedPreferences sharedpreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor;
//                        editor = sharedpreferences.edit();
//                        editor.putString("phone", phone.getText().toString());
//                        editor.commit();
//                        Paper.book().write(user_Key, phone.getText().toString());
//                        Paper.book().write(user_pass, password.getText().toString());
//
//                        Intent i = new Intent(userSignup.this, category.class);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(i);
//                        finish();
//
//                    } else {
//                        alerter(response, R.color.darkRed);
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    dialog.dismiss();
//                    Log.i("dars", "" + error);
//                    alerter(error.getMessage(), R.color.darkRed);
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> values = new HashMap<String, String>();
//                    values.put(user_name, name.getText().toString());
//                    values.put(user_password, password.getText().toString());
//                    values.put(user_phone, phone.getText().toString());
//                    values.put("token", token);
//                    return values;
//                }
//            };
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            requestQueue.add(stringRequest);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http:/topchefffff.000webhostapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getUnsafeOkHttpClient().build())
                    .build();
            connection Token = retrofit.create(connection.class);
            Call<ResponseBody> connection = Token.Register(name.getText().toString(),phone.getText().toString(),password.getText().toString(),token);
            connection.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String respo=response.message();
                                        if (respo.contains("sucessfully")) {
                        SharedPreferences sharedpreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor;
                        editor = sharedpreferences.edit();
                        editor.putString("phone", phone.getText().toString());
                        editor.commit();
                        Paper.book().write("phone", phone.getText().toString());
                        Paper.book().write("password", password.getText().toString());

                        Intent i = new Intent(userSignup.this, category.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();

                    } else {
                        alerter(respo, R.color.darkRed);
                    }





                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } else {
            alerter("Please Enter all required Filleds", R.color.darkRed);

        }
    }

    public boolean check() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            return false;
        }
        if (TextUtils.isEmpty(phone.getText().toString())) {
            return false;
        }
//        if (!phone_check) {
//            MDToast.makeText(getBaseContext(), "phone is not  approved", 2000, MDToast.TYPE_ERROR).show();
//            return false;
//        }

        else if (TextUtils.isEmpty(password.getText().toString())) {
            return false;
        }
        return true;
    }

    public void alerter(String v, int c) {
        Alerter.create(userSignup.this)
                .setTitle("alert")
                .setText(v)
                .setDuration(10000)
                .setBackgroundColorRes(c)
                .setIcon(R.drawable.alarm)
                .show();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) {
            // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage = "";
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:Phone is real";
                    getAccount();
                }
            }
            // Surface the result to your user in an appropriate way.
            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
        }
    }

    private void getAccount() {
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();
                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                int country = account.getPhoneNumber().getCountryCode().length();
                String phonenum = phoneNumber.toString();
                Log.i("codee", "onSuccess: " + account.getPhoneNumber().getCountryCode());
                Log.i("code", "onSuccess: " + country);
                Log.i("codey", "onSuccess: " + phonenum);
                phoneNumberString = phonenum.substring(country, phonenum.length());
                phone_check = true;
                phone.setText(phoneNumberString);
                phone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checked, 0);
                phone.setEnabled(false);
            }

            @Override
            public void onError(final AccountKitError error) {
                Log.e("AccountKit", error.toString());
                // Handle Error
            }
        });
    }

    public void initAccountKitSmsFlow() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    public void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(userSignup.this, "can not get token", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get new Instance ID token
                        String refreshedToken = task.getResult().getToken();
                        Log.i("tokken", "onComplete: "+refreshedToken);
                        Log.d("token", "Refreshed token: " + refreshedToken);
                            SharedPreferences sharedpreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor;
                            editor = sharedpreferences.edit();
                            editor.putString("token", refreshedToken);
                            editor.commit();
                            String phone= Paper.book().read("key","notyet");
                            if(!phone.equals("notyet")) {
                                sendToken(refreshedToken,phone);
                            }

                        //    Toast.makeText(application, "token:"+token, Toast.LENGTH_SHORT).show();
                        // Log and toast
                        //  String msg = getString(R.string.msg_token_fmt, token);
                        // Log.d(TAG, msg);
                        // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        Log.i("token", "onComplete: " + token);
                    }
                });

    }
    public void sendToken(String token,String phone)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://topchef.000webhostapp.com/topchef/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUnsafeOkHttpClient().build())
                .build();
        connection Token = retrofit.create( connection.class);
        Call<ResponseBody> connection = Token.sendToken(token,phone);
        connection.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    Log.i("service", "onResponse: " + response.body().string());
                }catch (IOException ex)
                {
                    Log.i(TAG, "onResponse: "+ex.getMessage());
                }catch (NullPointerException ex)
                {
                    Log.i(TAG, "onResponse: "+ex.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.i("error", "onResponse: " + call.toString() + t.getMessage());
            }
        });


    }
}