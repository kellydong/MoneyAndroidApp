package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class PieChart extends AppCompatActivity {
    private static final String TAG = "PieChart";
    AnyChartView anyChartView;
    String[] category;
    int[] cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);


        Intent intent = getIntent();


        category = intent.getStringArrayExtra(MainActivity.CATEGORY_ARRAY);
        cost = intent.getIntArrayExtra(MainActivity.COST_ARRAY);
        for (int i = 0; i < category.length; i++){
            Log.d(TAG, "onCreate: category: " + category[i]);
            Log.d(TAG, "onCreate: cost: " + cost[i]);
        }

        //Bundle bundle = getIntent().getExtras();
        //exampleList = (ArrayList<ExampleItem>) bundle.get("exampleItemArray");
        anyChartView = findViewById(R.id.any_chart_view);
        setupPieChart();
    }

    private void setupPieChart() {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        for (int i = 0; i < category.length; i++){
            dataEntries.add(new ValueDataEntry(category[i], cost[i]));
        }
        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }
}
