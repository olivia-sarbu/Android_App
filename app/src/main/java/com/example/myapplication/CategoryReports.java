package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.CategoryReportsBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CategoryReports extends AppCompatActivity {

    public String selectedItem;
    public LineChart lineChart;
    public ArrayList<ExpenseModel> transModelArrayList;
    public CategoryReportsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CategoryReportsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lineChart = findViewById(R.id.lineChart);
        transModelArrayList=new ArrayList<>();

        getData();
        final Spinner spinner = findViewById(R.id.reports_spinner);
        final String[] items = {"Nici o selectie", "Salariu", "Chirie", "Transport", "Mancare", "Utilitati", "Haine", "Sanatate", "Asigurare", "Cumparaturi casa", "Personal", "Educatie", "Cadouri", "Divertisment"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedItem = items[position];

                ArrayList<ExpenseModel> categoryTransactions = filterTransactionsByCategory(selectedItem);
                createLineChart(selectedItem, categoryTransactions);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
}

    private void createLineChart(String selectedItem, ArrayList<ExpenseModel> categoryTransactions) {
        List<Entry> entries = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        for (int i = 1; i <= getDaysInMonth(currentMonth, currentYear); i++) {
            int totalSum = 0;

            for (ExpenseModel transaction : categoryTransactions) {
                int day = getDayOfMonth(transaction.getData());
                int month = getMonth(transaction.getData());
                if (day == i && month == currentMonth) {
                    totalSum += Integer.parseInt(transaction.getSuma());
                }
            }

            entries.add(new Entry(i, totalSum));
        }

        LineDataSet dataSet = new LineDataSet(entries, selectedItem);
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        lineChart.invalidate();
    }

    private int getMonth(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            return calendar.get(Calendar.MONTH) + 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private int getDayOfMonth(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            return calendar.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private int getDaysInMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void getData() {
        String id = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection("Cheltuieli")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        ExpenseModel model = new ExpenseModel(
                                doc.getString("categorie"),
                                doc.getString("id"),
                                doc.getString("suma"),
                                doc.getString("tip"),
                                doc.getString("data"),
                                doc.getString("nota"));
                        transModelArrayList.add(model);
                    }
                })
                .addOnFailureListener(e -> {
                });
    }

    private ArrayList<ExpenseModel> filterTransactionsByCategory(String selectedItem) {
        ArrayList<ExpenseModel> categoryTransactions = new ArrayList<>();
        for (ExpenseModel transaction : transModelArrayList) {
            if (transaction.getCategorie().equals(selectedItem)) {
                categoryTransactions.add(transaction);
            }
        }
        return categoryTransactions;
    }
}
