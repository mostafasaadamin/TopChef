package com.example.topchef.UserPanel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topchef.Adapters.favouriteAdapter;
import com.example.topchef.Database.Database;
import com.example.topchef.Models.favouriteModel;
import com.example.topchef.R;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;

public class favourite_food extends AppCompatActivity {
    favouriteAdapter adapter;
    Database db;
    ArrayList<favouriteModel> favourite_list=new ArrayList<>();
    RecyclerView items ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_food);
        items=findViewById(R.id.favourite_recycler);
        items.setLayoutManager(new LinearLayoutManager(favourite_food.this));
        items.setHasFixedSize(true);
        db=new Database(this);
        LoadItems();
    }

    private void LoadItems() {
        favourite_list=db.getFavourite();
        if(favourite_list.size()>0) {
            adapter = new favouriteAdapter(favourite_list, getApplicationContext());
            items.setAdapter(adapter);

        }else
            {
                MDToast.makeText(this,"no favourite foods ",2000,MDToast.TYPE_WARNING).show();
            }
    }
}
