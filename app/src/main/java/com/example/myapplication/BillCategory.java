package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.BillCategoryBinding;
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

public class BillCategory extends AppCompatActivity {

    BillCategoryBinding binding;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private EditText termenLimita, dataNotificare;
    private Calendar calendarLimita, calendarNotificare;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BillCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        termenLimita =findViewById(R.id.termen_limita);
        dataNotificare=findViewById(R.id.data_notificare);
        calendarLimita=Calendar.getInstance();
        calendarNotificare=Calendar.getInstance();

        termenLimita.setOnClickListener(view -> displayCalendarTermenLimita());
        dataNotificare.setOnClickListener(view -> displayCalendarDataNotificare());

        binding.addBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categorie_factura = binding.categorieFactura.getText().toString().trim();
                String suma_plata = binding.sumaPlata.getText().toString().trim();
                String termen_limita = binding.termenLimita.getText().toString().trim();
                String data_notificare=binding.dataNotificare.getText().toString().trim();
                if (suma_plata.length() <= 0) {
                    return;
                }
                if (categorie_factura.length() <= 0) {
                    Toast.makeText(BillCategory.this, "Adauga o factura", Toast.LENGTH_SHORT).show();
                }

                String id = UUID.randomUUID().toString();
                Map<String, Object> facturi = new HashMap<>();
                facturi.put("id", id);
                facturi.put("suma_plata", suma_plata);
                facturi.put("categorie_factura", categorie_factura);
                facturi.put("termen_limita", termen_limita);
                facturi.put("data_notificare", data_notificare);
                db.collection("Facturi").document(id)
                        .set(facturi)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(BillCategory.this, "Adaugat", Toast.LENGTH_SHORT).show();
                                binding.categorieFactura.setText("");
                                binding.sumaPlata.setText("");
                                binding.termenLimita.setText("");
                                binding.dataNotificare.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(BillCategory.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    private void displayCalendarDataNotificare() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendarNotificare.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String selectedDate = sdf.format(calendarNotificare.getTime());
                    dataNotificare.setText(selectedDate);
                },
                calendarNotificare.get(Calendar.YEAR),
                calendarNotificare.get(Calendar.MONTH),
                calendarNotificare.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void displayCalendarTermenLimita() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendarLimita.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    String selectedDate = sdf.format(calendarLimita.getTime());
                    termenLimita.setText(selectedDate);
                },
                calendarLimita.get(Calendar.YEAR),
                calendarLimita.get(Calendar.MONTH),
                calendarLimita.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }


}
