package com.example.grupo6.appgrup6;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

public class TabImc extends Fragment {

    ArcSeekBar  gradientSeekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_imc, container, false);

        gradientSeekBar = (ArcSeekBar) rootView.findViewById(R.id.gradientSeekBar);

        int[] colorArrays = getResources().getIntArray(R.array.gradient);
        gradientSeekBar.setProgressGradient(colorArrays);

        Button a = (Button) rootView.findViewById(R.id.queEsImc);
        a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                queEsImc(v);
            }
        });


        return rootView;
    }

    public void queEsImc(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://es.wikipedia.org/wiki/%C3%8Dndice_de_masa_corporal"));
        startActivity(intent);
    }

    public void pgWeb1(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://cuidateplus.marca.com/ejercicio-fisico/diccionario/fitness.html"));
        startActivity(intent);
    }


}
