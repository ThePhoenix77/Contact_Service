package com.example.servicecontact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewContact extends AppCompatActivity {
    RecyclerView recyclerView ;
    ArrayList<String> firstName, lastName, phoneMobile, email, adresse ;
    DatabaseHelper db ;
    MyAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        //creation instance DatabaseHelper et listes d'affichage :
        db=new DatabaseHelper(this) ;
        firstName=new ArrayList<>() ;
        lastName=new ArrayList<>() ;
        phoneMobile=new ArrayList<>() ;
        email=new ArrayList<>() ;
        adresse=new ArrayList<>() ;

        //recyclerView :
        recyclerView=findViewById(R.id.recyclerview) ;
        adapter=new MyAdapter(this,firstName, lastName, phoneMobile, email, adresse) ;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displaydata() ;
    }

    //affichage donnees d'utilisateur :
    private void displaydata() {
        Cursor cursor=db.getData() ;
        if (cursor.getCount()==0){
            Toast.makeText(ViewContact.this, "Pas de contact", Toast.LENGTH_SHORT).show();
            return;
        }else {
            while (cursor.moveToNext()){
                firstName.add(cursor.getString(0)) ;
                lastName.add(cursor.getString(1)) ;
                phoneMobile.add(cursor.getString(2)) ;
                email.add(cursor.getString(3)) ;
                adresse.add(cursor.getString(4)) ;
            }
        }
    }
}