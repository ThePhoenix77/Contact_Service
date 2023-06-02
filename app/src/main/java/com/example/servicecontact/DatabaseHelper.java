package com.example.servicecontact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_CONTACTS="contacts" ;


    public DatabaseHelper(Context context){
        super(context,"contactsList.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Userdetails(firstName TEXT primary key, lastName TEXT, phoneMobile TEXT" +
                ", email TEXT, adresse TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    //insertion :
    public boolean insertuserdata(String firstName, String lastName, String phoneMobile, String email, String adresse){
        SQLiteDatabase db=this.getWritableDatabase() ;
        ContentValues contentValues=new ContentValues() ;

        contentValues.put("firstName",firstName);
        contentValues.put("lastName",lastName);
        contentValues.put("phoneMobile",phoneMobile);
        contentValues.put("email",email);
        contentValues.put("adresse",adresse);

        long result=db.insert(TABLE_CONTACTS,null,contentValues) ;
        if(result == -1){
            return false ;
        }else {
            return true ;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db=this.getWritableDatabase() ;
        return db.rawQuery("SELECT * FROM "+TABLE_CONTACTS,null);
    }

}
