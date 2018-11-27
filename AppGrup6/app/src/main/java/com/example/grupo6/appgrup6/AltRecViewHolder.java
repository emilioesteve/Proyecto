package com.example.grupo6.appgrup6;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class AltRecViewHolder extends RecyclerView.ViewHolder  {
    public TextView mFecha, mAlt;

    public AltRecViewHolder(View itemView){
        super(itemView);

        mFecha = itemView.findViewById(R.id.fecha_ele);
        mAlt = itemView.findViewById(R.id.altura_ele);
    }
}
