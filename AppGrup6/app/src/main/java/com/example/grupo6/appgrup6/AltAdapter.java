package com.example.grupo6.appgrup6;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AltAdapter extends RecyclerView.Adapter<AltRecViewHolder> {
    TabAltura tabAltura;
    ArrayList<Altura> alturaArrayList;

    public AltAdapter(TabAltura tabAltura, ArrayList<Altura> alturaArrayList) {
        this.tabAltura = tabAltura;
        this.alturaArrayList = alturaArrayList;
    }

    @NonNull
    @Override
    public AltRecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        LayoutInflater layoutInflater = LayoutInflater.from(tabAltura.getActivity());
        View view = layoutInflater.inflate(R.layout.elementos_altura, parent, false);

        return new AltRecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AltRecViewHolder holder, int position){

        holder.mAlt.setText(Double.toString(alturaArrayList.get(position).getAltura()));
        holder.mFecha.setText(alturaArrayList.get(position).getFecha());


    }

    @Override
    public int getItemCount(){
        return alturaArrayList.size();
    }
}
