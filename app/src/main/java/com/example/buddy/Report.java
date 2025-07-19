package com.example.buddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;

public class Report extends AppCompatActivity {

    PieChart genderPieChart;
    BarChart birthdayBarChart;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        genderPieChart = findViewById(R.id.genderPieChart);
        birthdayBarChart = findViewById(R.id.birthdayBarChart);


        // Get userId from intent
        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupPieChart();
        setupBarChart();

        //Navigation Button
        findViewById(R.id.btnHome).setOnClickListener(v -> {
            finish();
        });

        findViewById(R.id.btnFriend).setOnClickListener(v -> {
            int userId = getIntent().getIntExtra("userId", -1);
            Intent intent = new Intent(Report.this, Friends.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        findViewById(R.id.btnReport).setOnClickListener(v -> {
            int userId = getIntent().getIntExtra("userId", -1);
            Intent intent = new Intent(Report.this, ListByMonthActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }

    private void setupPieChart() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        HashMap<String, Integer> genderCounts = dbHelper.getGenderCounts(userId);

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : genderCounts.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Gender Distribution");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(0f);
        dataSet.setSliceSpace(3f);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(14f);

        genderPieChart.setData(data);
        genderPieChart.setUsePercentValues(false);
        genderPieChart.setDrawHoleEnabled(false);
        genderPieChart.setDescription(null);
        genderPieChart.setDrawEntryLabels(false);
        genderPieChart.getLegend().setEnabled(false);
        genderPieChart.setCenterTextSize(18f);
        genderPieChart.invalidate();
    }

    private void setupBarChart() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int[] monthCounts = dbHelper.getBirthMonthCounts(userId);

        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry(i, monthCounts[i]));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Birthdays per Month");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(0f);
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.7f); //thickness

        birthdayBarChart.setData(data);
        birthdayBarChart.setFitBars(true);
        birthdayBarChart.getDescription().setEnabled(false);
        birthdayBarChart.getXAxis().setDrawGridLines(false);
        birthdayBarChart.getAxisLeft().setDrawGridLines(false);
        birthdayBarChart.getAxisRight().setEnabled(false);
        birthdayBarChart.getLegend().setEnabled(false);

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        XAxis xAxis = birthdayBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(45f);

        birthdayBarChart.invalidate();
    }


}
