package com.example.grupo6.appgrup6;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

public class TabPeso extends Fragment {

    private BarChart barChart;
    private int[] pesos = { 74, 50, 65, 80, 76, 65, 80 };
    private String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves",
            "Viernes", "Sábado", "Domingo"};
    private int[] colors = new int[]{ Color.BLACK, Color.RED, Color.BLUE, Color.GREEN,
            Color.MAGENTA, Color.YELLOW, Color.LTGRAY };
    //private RecyclerView.layoutManager layoutManager;
    //public static AdaptadorLugaresFirestoreUI adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_peso, container, false);
        barChart = (BarChart)rootView.findViewById(R.id.barChart);
        createCharts();




        /*final FragmentActivity c = getActivity();
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);



        final AdaptadorPesosFirestoreUI adapter = new AdaptadorPesosFirestoreUI(c);

        recyclerView.setAdapter(adapter);*/

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

}
