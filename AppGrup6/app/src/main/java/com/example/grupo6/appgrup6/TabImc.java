package com.example.grupo6.appgrup6;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        return rootView;
    }
}
