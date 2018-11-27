package com.example.grupo6.appgrup6;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.content.ContentValues.TAG;
import static android.content.Context.NOTIFICATION_SERVICE;


public class TabPeso extends Fragment {

    private BarChart barChart;
    private int[] pesos = { 74, 50, 65, 80, 76, 65, 80 };
    private String[] dias = {"L", "M", "X", "J","V", "S", "D"};
    private int[] colors = new int[]{ Color.BLACK, Color.RED, Color.BLUE, Color.GREEN,
            Color.MAGENTA, Color.YELLOW, Color.LTGRAY };
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    RecyclerView mRecyclerView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    CollectionReference userPeso = db.collection("usuarios").document(user.getUid()).collection("bascula");
    ArrayList<Peso> pesoArrayList;
    ArrayList<Peso> misPesos;
    ArrayList<Peso> misFechas;
    MyAdapter adapter;

    //Sistema de notificaciones parcialmente implementado.
    int a = 20;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_peso, container, false);
        barChart = (BarChart)rootView.findViewById(R.id.barChart);


        pesoArrayList = new ArrayList<>();
        mRecyclerView = rootView.findViewById(R.id.recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        setUpDatos();
        loadDataFromFirestore();

        createCharts();

        return rootView;


    }

    private Chart getSameChart(Chart chart, String description, int textColor, int animateY ){
        chart.getDescription().setText(description);
        chart.getDescription().setTextSize(15);
        chart.animateY(animateY);
        legend(chart);

        return chart;
    }

    private void legend ( Chart chart ){
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry> entries = new ArrayList<>();
        for( int i = 0; i < dias.length; i++ ){
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            entry.label = dias[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }

    private ArrayList<BarEntry>getBarEntries(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i < pesos.length; i++)
            entries.add(new BarEntry(i, pesos[i]));
        return entries;
    }

    private void axisX(XAxis axis){
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setTextSize(13);
        axis.setValueFormatter(new IndexAxisValueFormatter(dias));
    }

    private void axisLeft(YAxis axis){
        axis.setSpaceTop(35);
        axis.setAxisMinimum(30);
        axis.setTextSize(13);
    }

    private void axisRight(YAxis axis){
        axis.setEnabled(false);
    }

    public void createCharts(){
        barChart = (BarChart)getSameChart(barChart, "Pesos", Color.RED, 3000);
        barChart.setDrawGridBackground(true);
        barChart.setDrawBarShadow(true);
        barChart.setData(getBarData());
        barChart.invalidate();

        axisX(barChart.getXAxis());
        axisLeft(barChart.getAxisLeft());
        axisRight(barChart.getAxisRight());
    }

    private DataSet getData(DataSet dataSet){
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12);
        return dataSet;
    }

    private BarData getBarData(){
        BarDataSet barDataSet = (BarDataSet)getData(new BarDataSet(getBarEntries(), ""));

        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.65f);
        return barData;
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



}
