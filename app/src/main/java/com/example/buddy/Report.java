package com.example.buddy;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;

public class Report extends AppCompatActivity {

    PieChart genderPieChart;
    BarChart birthdayBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        genderPieChart = findViewById(R.id.genderPieChart);
        birthdayBarChart = findViewById(R.id.birthdayBarChart);

        setupPieChart();
        setupBarChart();
    }

    private void setupPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(8f, "Female"));
        entries.add(new PieEntry(5f, "Male"));
        entries.add(new PieEntry(2f, "Other"));

        PieDataSet dataSet = new PieDataSet(entries, "Gender Distribution");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(14f);

        genderPieChart.setData(data);
        genderPieChart.setUsePercentValues(false); // show actual numbers
        genderPieChart.getDescription().setEnabled(false);
        genderPieChart.setDrawHoleEnabled(false);
        genderPieChart.setCenterText("Gender");
        genderPieChart.setCenterTextSize(16f);
        genderPieChart.invalidate();
    }


    private void setupBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        // Example: Jan = 3 people, Feb = 5, Mar = 1, etc.
        entries.add(new BarEntry(0, 3));
        entries.add(new BarEntry(1, 5));
        entries.add(new BarEntry(2, 1));
        entries.add(new BarEntry(3, 6));
        entries.add(new BarEntry(4, 2));
        entries.add(new BarEntry(5, 4));
        entries.add(new BarEntry(6, 7));
        entries.add(new BarEntry(7, 3));
        entries.add(new BarEntry(8, 0));
        entries.add(new BarEntry(9, 2));
        entries.add(new BarEntry(10, 4));
        entries.add(new BarEntry(11, 1));

        BarDataSet dataSet = new BarDataSet(entries, "Birthdays per Month");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f); // set custom bar width

        birthdayBarChart.setData(data);
        birthdayBarChart.setFitBars(true);
        birthdayBarChart.getDescription().setEnabled(false);
        birthdayBarChart.getXAxis().setDrawGridLines(false);
        birthdayBarChart.getAxisLeft().setDrawGridLines(false);
        birthdayBarChart.getAxisRight().setEnabled(false);
        birthdayBarChart.getLegend().setEnabled(false);

        // Add month labels
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        XAxis xAxis = birthdayBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(45f); // tilt labels

        birthdayBarChart.invalidate();
    }

}
