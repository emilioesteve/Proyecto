package com.example.grupo6.appgrup6;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MediRecViewHolder extends RecyclerView.ViewHolder  {
    public TextView nombre, tomas, tiempo;

    public MediRecViewHolder(View itemView){
        super(itemView);

        nombre = itemView.findViewById(R.id.nombre);
        tomas = itemView.findViewById(R.id.tomas);
        tiempo = itemView.findViewById(R.id.tiempo);
    }
}
