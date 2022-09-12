package com.quawlebs.drupp.view.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.quawlebs.drupp.R;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EarningGraphActivty extends Activity {
    TextView week_date;
    ImageView left_img, righ_img;
    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earning_with_graph);
        left_img = findViewById(R.id.left_img);
        righ_img = findViewById(R.id.right_img);
        week_date = findViewById(R.id.week_date);
        BarChart chart = (BarChart) findViewById(R.id.chart);
        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("");

        chart.setDrawValueAboveBar(false);
        chart.setBackgroundColor(getResources().getColor(R.color.colorWhite));

        chart.getLegend().setEnabled(false);
        // Remove the grid line from background
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setEnabled(false);


        // Disable the right y axis
        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(false);
        rightYAxis.setDrawGridLines(false);

        // Hide the desc value of each bar on top
        chart.getXAxis().setEnabled(true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);


        chart.animateXY(2000, 2000);
        chart.invalidate();
        date = new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault()).format(new Date());

        week_date.setText(date);
        setListners();


    }

    void setListners() {
        left_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = getCalculatedPreviousWeek(date);
                week_date.setText(date);

            }
        });
        righ_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = getCalculatedNextWeek(date);
                week_date.setText(date);


            }
        });
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(96.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(82.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(88.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(52.000f, 3); // Apr
        valueSet1.add(v1e4);

        BarEntry v1e5 = new BarEntry(50.000f, 4); // Apr
        valueSet1.add(v1e5);

        BarEntry v1e6 = new BarEntry(40.000f, 5); // Apr
        valueSet1.add(v1e6);
        BarEntry v1e7 = new BarEntry(52.000f, 6); // Apr
        valueSet1.add(v1e7);


        BarDataSet barDataSet1 = new BarDataSet(valueSet1, " ");


        //Chnaging the color
        barDataSet1.setColor(Color.rgb(0, 163, 129));

        // Hide the value on each bar
        /*barDataSet1.setDrawValues(false);*/

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> yAxis = new ArrayList<>();
        yAxis.add("S");
        yAxis.add("M");
        yAxis.add("T");
        yAxis.add("W");
        yAxis.add("T");
        yAxis.add("F");
        yAxis.add("S");


        return yAxis;
    }

    public static String getCalculatedPreviousWeek(String date) {

        String finalDate = "";
        SimpleDateFormat s = new SimpleDateFormat("MMM dd,yyyy");
        Date myDate = null;
        try {
            myDate = s.parse(date);
            Date newDate = new Date(myDate.getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000
            finalDate = s.format(newDate);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDate;
    }

    public static String getCalculatedNextWeek(String date) {

        String finalDate = "";

        SimpleDateFormat s = new SimpleDateFormat("MMM dd,yyyy");
        Date myDate = null;
        try {
            myDate = s.parse(date);
            Date newDate = new Date(myDate.getTime() + 604800000L); // 7 * 24 * 60 * 60 * 1000
            finalDate = s.format(newDate);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDate;
    }
}