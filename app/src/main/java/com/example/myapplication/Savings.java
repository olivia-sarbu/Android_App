package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.SavingsBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.eazegraph.lib.charts.BarChart;

import java.util.ArrayList;

public class Savings extends AppCompatActivity {

    SavingsBinding binding;
    EditText tinta_economii, economii_curente;
    PieChart pieChart;
    BarChart barChart;
    int economii=0;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SavingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference();
        pieChart = findViewById(R.id.pieChart_savings);
        tinta_economii=findViewById(R.id.tinta_economii);
        economii_curente=findViewById(R.id.economii_curente);
        //BarChart barChart = findViewById(R.id.grafic_economii);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedEconomii = sharedPreferences.getInt("economii", 0);
        String savedGoalValue = sharedPreferences.getString("goalValue", "0");

        economii = savedEconomii;
        economii_curente.setText(String.valueOf(savedEconomii));
        tinta_economii.setText(savedGoalValue);

        // Initialize pie chart with saved values
        updatePieChart(savedEconomii, savedGoalValue);

        // Set a listener to save the entered value when it changes
        tinta_economii.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                // Save the current value to SharedPreferences
                String currentValue = editable.toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("lastValue", currentValue);
                editor.apply();
            }
        });

        binding.salvareEconomii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newInput = economii_curente.getText().toString();
                if (!newInput.isEmpty()) {
                    int newNumber = Integer.parseInt(newInput);
                    economii += newNumber;
                    // Update the UI to display the new sum
                    economii_curente.setText(String.valueOf(economii));

                    // Update pie chart entries and save updated values
                    updatePieChart(economii, tinta_economii.getText().toString());
                    saveData(economii, tinta_economii.getText().toString());
                }
            }
        });
    }

    private void updatePieChart(int currentEconomii, String goalValue) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(currentEconomii, "Economii"));

        try {
            int goalIntValue = Integer.parseInt(goalValue);
            entries.add(new PieEntry(goalIntValue - currentEconomii, "Țintă"));
        } catch (NumberFormatException e) {
            // Handle parsing error if needed
        }

        int red = 114;
        int green = 101;
        int blue = 103;

        PieDataSet dataSet = new PieDataSet(entries, "");
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(red, green, blue)); // Color for Segment 1
        colors.add(Color.rgb(239, 234, 221));  // Color for Segment 2
        dataSet.setColors(colors);

        // Create pie data object
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);

        // Optional: Customize chart appearance
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.animateY(1000);

        // Refresh chart
        pieChart.invalidate();
    }

    private void saveData(int currentEconomii, String goalValue) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("economii", currentEconomii);
        editor.putString("goalValue", goalValue);
        editor.apply();
    }


    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("economii", economii);
        editor.putString("goalValue", tinta_economii.getText().toString());
        editor.apply();
    }

}
