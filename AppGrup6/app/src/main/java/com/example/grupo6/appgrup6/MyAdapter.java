package com.example.grupo6.appgrup6;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {

    TabPeso tabPeso;
    ArrayList<Peso> pesoArrayList;

    public MyAdapter(TabPeso tabPeso, ArrayList<Peso> pesoArrayList) {
        this.tabPeso = tabPeso;
        this.pesoArrayList = pesoArrayList;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        LayoutInflater layoutInflater = LayoutInflater.from(tabPeso.getActivity());
        View view = layoutInflater.inflate(R.layout.elementos_peso, parent, false);

        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position){

        holder.mPeso.setText(Double.toString(pesoArrayList.get(position).getPeso()));
        holder.mFecha.setText(pesoArrayList.get(position).getFecha());


    }

    @Override
    public int getItemCount(){
        return pesoArrayList.size();
    }

}
