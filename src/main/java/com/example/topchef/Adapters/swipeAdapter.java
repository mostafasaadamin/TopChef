package com.example.topchef.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.topchef.Models.siderModel;
import com.example.topchef.R;
import com.huxq17.swipecardsview.BaseCardAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by unknown on 12/6/2018.
 */

public class swipeAdapter extends BaseCardAdapter {
    ArrayList<siderModel> data;
    Context context;

    public swipeAdapter(ArrayList<siderModel> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.swipecard;
    }

    @Override
    public void onBindData(int position, View cardview) {
        if (data == null || data.size() == 0
                ) {
            return;
        }
        ImageView img = cardview.findViewById(R.id.ads_image);
        TextView txt = cardview.findViewById(R.id.Ads_desc);
        siderModel model = data.get(position);
        String url=model.getFoodimage().replaceFirst("s","");
        Picasso.with(context).load(url).fit().into(img);
        txt.setText(model.getFoodname());

    }
}
