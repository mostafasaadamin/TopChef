package com.example.topchef.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


import com.example.topchef.Models.favouriteModel;
import com.example.topchef.Models.order;

import java.util.ArrayList;

//import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by unknown on 6/1/2018.
 */

public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "orderss";
        public Database(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(order.CREATE_TABLE);
        db.execSQL(order.CREATE_TABLE_Favourite);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + order.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + order.TABLE_NAME2);
        // Create tables again
        onCreate(db);
    }
    public long insertProduct(String name,String price,String quantity,String discount,String id,String image_url) {
       ArrayList<order>data=getAllNotes();
       int count=0;
       for(int i=0;i<data.size();i++)
       {
           if(data.get(i).getId().equals(id))
           {
            count=Integer.parseInt(quantity)+Integer.parseInt(data.get(i).getQuantity());
           deleteNote(data.get(i));
           }
       }
        Log.i("count", "insertProduct: "+count);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(order.product_Name, name);
        values.put(order.Product_price,price);
        values.put(order.product_discount,discount);
        values.put(order.food_id,id);
        values.put(order.product_image_url,image_url);
        if(count!=0)
        {
            values.put(order.product_quantity, count);

        }
        else
            {
                values.put(order.product_quantity, quantity);
            }

        long idd = db.insert(order.TABLE_NAME, null, values);
        db.close();
        return idd;
    }
    public ArrayList<order> getAllNotes() {
        ArrayList<order> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + order.TABLE_NAME + " ORDER BY " +
                order.Product_price + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                order orderr = new order();
                orderr.setId(cursor.getString(cursor.getColumnIndex(order.food_id)));
                orderr.setProductName(cursor.getString(cursor.getColumnIndex(order.product_Name)));
                orderr.setPrice(cursor.getString(cursor.getColumnIndex(order.Product_price)));
                orderr.setQuantity(cursor.getString(cursor.getColumnIndex(order.product_quantity)));
                orderr.setDiscount(cursor.getString(cursor.getColumnIndex(order.product_discount)));
                orderr.setImage_url(cursor.getString(cursor.getColumnIndex(order.product_image_url)));
                notes.add(orderr);
            } while (cursor.moveToNext());
        }
        db.close();
        return notes;
    }
    public void deleteNote(order or) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(order.TABLE_NAME, order.food_id + " = ?",
                new String[]{String.valueOf(or.getId())});
        db.close();
    }
public long addToFavourite(String FoodID,String Foodname,String imageURl,String price,String des,String discount)
{
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(order.Favourite_id,FoodID);
    values.put(order.food_name,Foodname);
    values.put(order.Favourite_image_url,imageURl);
    values.put(order.food_price,price);
    values.put(order.food_des,des);
    values.put(order.food_discount,discount);
    long idd = db.insert(order.TABLE_NAME2, null, values);
    db.close();
    return idd;
}
public ArrayList<favouriteModel> getFavourite()
{
    ArrayList<favouriteModel> favourites = new ArrayList<>();
    String selectQuery = "SELECT  * FROM " + order.TABLE_NAME2 + " ORDER BY " +
            order.Favourite_id_increment + " DESC";
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
        do {
            favouriteModel model=new favouriteModel();
            model.setFoodID(cursor.getString(cursor.getColumnIndex(order.Favourite_id)));
            model.setFoodName(cursor.getString(cursor.getColumnIndex(order.food_name)));
            model.setImageURl(cursor.getString(cursor.getColumnIndex(order.Favourite_image_url)));
            model.setPrice(cursor.getString(cursor.getColumnIndex(order.food_price)));

            favourites.add(model);
        } while (cursor.moveToNext());
    }
    db.close();
    return favourites;
}
public boolean isFavourite(String FoodID)
{
    String selectQuery = "SELECT  * FROM " + order.TABLE_NAME2 + "WHERE" +
            order.Favourite_id +" = ?"+FoodID;
    SQLiteDatabase db = this.getWritableDatabase();
   // Cursor cursor = db.rawQuery(selectQuery, null);
    Cursor cursor=  db.rawQuery("SELECT * from " + order.TABLE_NAME2  + " where " + order.Favourite_id+"=?", new String[]{FoodID});
if(cursor.getCount()>0)
{
    return true;

}else
    {
        return false;
    }

}
public void removeFromFavourite(String FoodID)
{
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(order.TABLE_NAME2, order.Favourite_id + " = ?",
            new String[]{String.valueOf(FoodID)});
    db.close();
}
}