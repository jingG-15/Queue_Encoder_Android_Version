package com.example.queue_encoder_2024;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class sqlite_helper extends SQLiteOpenHelper {


    public static final String Database_Name = "Queue_Enc.db";
    public static final String Table_Name = "App_Settings";
    public static final String ID = "ID";
    public static final String Server_IP = "Server_IP";
    public static final String Printer_IP = "Printer_IP";
    public static final String Display_Port = "Display_Port";



    public sqlite_helper(@Nullable Context context) {
        super(context, Database_Name, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();


    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Table_Name + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Server_IP + " TEXT, "
                + Printer_IP +" TEXT, "  + Display_Port +" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Cursor Check_Data_Exists(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT Server_IP, Printer_IP, Display_Port" +
                " FROM App_Settings WHERE ID = 1",null);
        return result;
    }

    public String Insert_New_Settings(String... args){
        String ret_val = "";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + Table_Name);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Table_Name + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Server_IP + " TEXT, "
                + Printer_IP +" TEXT, "  + Display_Port +" TEXT)");

        try{

            ContentValues cv = new  ContentValues();
            cv.put(ID, 1);
            cv.put(Server_IP, args[0]);
            cv.put(Printer_IP, args[1]);
            cv.put(Display_Port, args[2]);
            db.insert(Table_Name, null, cv );

            ret_val = "Insert Success!";
            return ret_val;
        } catch (Exception e) {
            e.printStackTrace();
            ret_val = e.getMessage();
            return ret_val;
        }

    }



}
