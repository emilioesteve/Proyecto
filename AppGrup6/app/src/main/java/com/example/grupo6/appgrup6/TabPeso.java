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

import java.text.DecimalFormat;
import java.util.ArrayList;


import static android.content.ContentValues.TAG;

public class TabPeso extends Fragment {

    ArrayList<Entry> entries = new ArrayList<>();
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    RecyclerView mRecyclerView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    CollectionReference userPeso = db.collection("usuarios").document(user.getUid()).collection("bascula");
    ArrayList<Peso> pesoArrayList;
    MyAdapter adapter;
    TextView pesoult;
    TextView imc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_peso, container, false);

        pesoArrayList = new ArrayList<>();
        mRecyclerView = rootView.findViewById(R.id.recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        setUpDatos();
        loadDataFromFirestore();

        pesoult = (TextView) rootView.findViewById(R.id.pesoulti);
        imc = (TextView) rootView.findViewById(R.id.imc);
        loadLastDataFirestore();


        final LineChart chart = (LineChart) rootView.findViewById(R.id.barChart);

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
                            if (doc.get("peso") != null) {
                                double measure = doc.getDouble("peso");
                                entries.add(new Entry(i, Float.valueOf(String.valueOf(measure))));
                                i++;
                            }
                        }
                        //Log.d(TAG, "Current measures: " + measures.toArray()[0].toString());


                        LineDataSet dataSet = new LineDataSet(entries, "Customized values");
                        //dataSet.setColor(ContextCompat.getColor(context, R.color.black));
                        //dataSet.setValueTextColor(ContextCompat.getColor(context, R.color.white));

                        /*
                        // Controlling X axis
                        XAxis xAxis = chart.getXAxis();
                        // Set the xAxis position to bottom. Default is top
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        //Customizing x axis value
                        final String[] months = new String[]{"Jan", "Feb", "Mar", "Apr"};

                        IAxisValueFormatter formatter = new IAxisValueFormatter() {
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                return months[(int) value];
                            }
                        };
                        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
                        xAxis.setValueFormatter(formatter);*/

                        //*
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

        if (pesoArrayList.size() > 0) {
            pesoArrayList.clear();
        }

        final CollectionReference medidasInfo = db.collection("usuarios").document(user.getUid()).collection("bascula");

        medidasInfo.orderBy("fecha", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                            Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());

                            Peso mimedida = new Peso(documentSnapshot.getString("fecha"),documentSnapshot.getDouble("peso"));
                            pesoArrayList.add(mimedida);

                        }

                        adapter = new MyAdapter(TabPeso.this, pesoArrayList);
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

                            Peso mimedida = new Peso(documentSnapshot.getString("fecha"),documentSnapshot.getDouble("peso"));
                            pesoArrayList.add(mimedida);
                            String peso = Double.toString(mimedida.getPeso());
                            pesoult.setText(peso);
                            double im = mimedida.getPeso()/(1.79*1.79);
                            DecimalFormat df = new DecimalFormat("#.00");
                            String i = df.format(im);
                            imc.setText(i);

                        }
                    }
                });
    }

}
