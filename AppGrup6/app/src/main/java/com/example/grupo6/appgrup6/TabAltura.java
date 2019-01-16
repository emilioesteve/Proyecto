package com.example.grupo6.appgrup6;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class TabAltura extends Fragment {

    ArrayList<Entry> entries = new ArrayList<>();
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    RecyclerView mRecyclerView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    CollectionReference userAltura = db.collection("usuarios").document(user.getUid()).collection("bascula");
    ArrayList<Altura> altArrayList;
    AltAdapter adapter;
    TextView alturault;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_altura, container, false);


        altArrayList = new ArrayList<>();
        mRecyclerView = rootView.findViewById(R.id.recycler1);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setUpDatos();
        loadDataFromFirestore();

        alturault = (TextView) rootView.findViewById(R.id.pesoll);
        loadLastDataFirestore();

        final LineChart chart = (LineChart) rootView.findViewById(R.id.barChart1);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(user.getUid())
                .collection("bascula") // Documento del usuario
                .orderBy("fecha", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        entries = new ArrayList<>();
                        int i = 0;
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("altura") != null) {
                                double measure = doc.getDouble("altura");
                                entries.add(new Entry(i, Float.valueOf(String.valueOf(measure))));
                                i++;
                            }
                        }

                        LineDataSet dataSet = new LineDataSet(entries, "Customized values");

                        // Controlling right side of y axis
                        YAxis yAxisRight = chart.getAxisRight();
                        yAxisRight.setEnabled(false);

                        //*
                        // Controlling left side of y axis
                        YAxis yAxisLeft = chart.getAxisLeft();
                        yAxisLeft.setGranularity(1f);

                        chart.getAxisLeft().setDrawGridLines(false);
                        chart.getXAxis().setDrawGridLines(false);
                        chart.getAxisLeft().setDrawLabels(false);
                        chart.getAxisRight().setDrawLabels(false);
                        chart.getXAxis().setDrawLabels(false);
                        chart.getLegend().setEnabled(false);   // Hide the legend
                        Legend l = chart.getLegend();
                        l.setEnabled(false);

                        // Setting Data
                        LineData data = new LineData(dataSet);
                        chart.setData(data);
                        chart.animateX(2500);
                        //refresh
                        chart.invalidate();

                    }
                });

        return rootView;
    }

    private void loadDataFromFirestore() {

        if (altArrayList.size() > 0) {
            altArrayList.clear();
        }

        final CollectionReference medidasInfo = db.collection("usuarios").document(user.getUid()).collection("bascula");

        medidasInfo.orderBy("fecha", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                            Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());

                            Altura mimedida = new Altura(documentSnapshot.getString("fecha"),documentSnapshot.getDouble("altura"));
                            altArrayList.add(mimedida);

                        }

                        adapter = new AltAdapter(TabAltura.this, altArrayList);
                        mRecyclerView.setAdapter(adapter);

                    }
                });

    }//loadDataFromFirestore()

    private void setUpDatos() {

        db = FirebaseFirestore.getInstance();
    }

    private void loadLastDataFirestore(){
        final CollectionReference medidasInfo = db.collection("usuarios").document(user.getUid()).collection("bascula");

        medidasInfo.orderBy("fecha", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                            Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());

                            Altura mimedida = new Altura(documentSnapshot.getString("fecha"),documentSnapshot.getDouble("altura"));
                            altArrayList.add(mimedida);
                            String altura = Double.toString(mimedida.getAltura());
                            alturault.setText(altura);

                        }
                    }
                });
    }

}
