package com.example.grupo6.appgrup6;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView mFecha, mPeso;

    public MyRecyclerViewHolder(View itemView){
        super(itemView);

        mFecha = itemView.findViewById(R.id.fecha_ele);
        mPeso = itemView.findViewById(R.id.peso_ele);
    }
}
