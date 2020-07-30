package com.example.topchef;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by unknown on 7/24/2018.
 */

public class config {
     Context con;
    public config(Context con) {
        this.con=con;
    }
    public static final String PAyPAl_Cient_ID="AY5Laku2omS6F4Hh3EwniOCDSs_inrXxcmLJ2Zds_O2PiPdB_YTo8Y-sugJsOni12xY2KerqZxyQatSO";
    public  boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager)con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
