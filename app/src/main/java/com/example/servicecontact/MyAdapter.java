package com.example.servicecontact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context ;
    private final ArrayList firstName_id, lastName_id, phoneMobile_id, email_id, adresse_id ;


    public MyAdapter(Context context, ArrayList firstName_id, ArrayList lastName_id, ArrayList phoneMobile_id, ArrayList email_id, ArrayList adresse_id) {
        this.context = context;
        this.firstName_id = firstName_id;
        this.lastName_id = lastName_id;
        this.phoneMobile_id = phoneMobile_id;
        this.email_id = email_id;
        this.adresse_id = adresse_id;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.recycler_view_row,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.firstName_id.setText(String.valueOf(firstName_id.get(position)));
        holder.lastName_id.setText(String.valueOf(lastName_id.get(position)));
        holder.phoneMobile_id.setText(String.valueOf(phoneMobile_id.get(position)));
        holder.email_id.setText(String.valueOf(email_id.get(position)));
        holder.adresse_id.setText(String.valueOf(adresse_id.get(position)));

    }

    @Override
    public int getItemCount() {
        return firstName_id.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView firstName_id, lastName_id, phoneMobile_id, email_id, adresse_id ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName_id=itemView.findViewById(R.id.textfirstName) ;
            lastName_id=itemView.findViewById(R.id.textlastName) ;
            phoneMobile_id=itemView.findViewById(R.id.textphoneMobile) ;
            email_id=itemView.findViewById(R.id.textemail) ;
            adresse_id=itemView.findViewById(R.id.textadresse) ;
        }
    }
}
