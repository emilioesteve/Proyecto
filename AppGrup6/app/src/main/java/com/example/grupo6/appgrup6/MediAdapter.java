package com.example.grupo6.appgrup6;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MediAdapter extends RecyclerView.Adapter<MediRecViewHolder> {
    MedicinasActivity tabAltura;
    ArrayList<Medicina> alturaArrayList;

    public MediAdapter(MedicinasActivity tabAltura, ArrayList<Medicina> alturaArrayList) {
        this.tabAltura = tabAltura;
        this.alturaArrayList = alturaArrayList;
    }

    @NonNull
    @Override
    public MediRecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        LayoutInflater layoutInflater = LayoutInflater.from(tabAltura.getBaseContext());
        View view = layoutInflater.inflate(R.layout.elementos_medicina, parent, false);

        return new MediRecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediRecViewHolder holder, int position){

        holder.tomas.setText(Double.toString(alturaArrayList.get(position).getTomas()));
        holder.tiempo.setText(alturaArrayList.get(position).getTiempo());
        holder.nombre.setText(alturaArrayList.get(position).getNombre());

    }

    @Override
    public int getItemCount(){
        return alturaArrayList.size();
    }
}

