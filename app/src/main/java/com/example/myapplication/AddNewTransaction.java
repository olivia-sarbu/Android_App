package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityAddNewTransactionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddNewTransaction extends AppCompatActivity {

    ActivityAddNewTransactionBinding binding;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String tip_tranzactie="";
    Calendar calendar;
    EditText data_tranzactie_box;
    String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAddNewTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        data_tranzactie_box=findViewById(R.id.data);
        calendar=Calendar.getInstance();

        data_tranzactie_box.setOnClickListener(view -> showCalendar());

        binding.cheltuialaRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tip_tranzactie = "Cheltuiala";
                binding.cheltuialaRadioBtn.setChecked(true);
                binding.venitRadioBtn.setChecked(false);
            }
        });

        binding.venitRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tip_tranzactie = "Venit";
                binding.cheltuialaRadioBtn.setChecked(false);
                binding.venitRadioBtn.setChecked(true);
            }
        });

        binding.addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String suma = binding.sumaBani.getText().toString().trim();
                String categorie = selectedItem;
                String nota = binding.nota.getText().toString().trim();
                String data = binding.data.getText().toString().trim();
                if (suma.length() <= 0) {
                    return;
                }
                if (tip_tranzactie.length() <= 0) {
                    Toast.makeText(AddNewTransaction.this, "Selecteaza tip tranzactie", Toast.LENGTH_SHORT).show();
                }

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();

                if (currentUser != null) {
                    String currentUserId = currentUser.getUid();

                    String id = UUID.randomUUID().toString();
                    Map<String, Object> tranzactii = new HashMap<>();
                    tranzactii.put("id", id);
                    tranzactii.put("suma", suma);
                    tranzactii.put("categorie", categorie);
                    tranzactii.put("nota", nota);
                    tranzactii.put("tip", tip_tranzactie);
                    tranzactii.put("data", data);
                    tranzactii.put("id", currentUserId);
                    db.collection("Cheltuieli").document(id)
                            .set(tranzactii)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddNewTransaction.this, "Adaugat", Toast.LENGTH_SHORT).show();
                                    binding.categorieSpinner.setSelection(-1);
                                    binding.sumaBani.setText("");
                                    binding.nota.setText("");
                                    binding.data.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddNewTransaction.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        final Spinner spinner = findViewById(R.id.categorie_spinner);
        final String[] items = {"Nici o selectie", "Salariu", "Chirie", "Transport", "Mancare", "Utilitati", "Haine", "Sanatate", "Asigurare", "Cumparaturi casa", "Personal", "Educatie", "Cadouri", "Divertisment"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                 selectedItem = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setPrompt("Select an option");
            }
        });
    }

    private void showCalendar() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    String selectedDate = sdf.format(calendar.getTime());
                    data_tranzactie_box.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

}