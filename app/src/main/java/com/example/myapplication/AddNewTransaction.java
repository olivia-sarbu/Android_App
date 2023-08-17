package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityAddNewTransactionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AddNewTransaction extends AppCompatActivity {

    ActivityAddNewTransactionBinding binding;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String tip_tranzactie="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAddNewTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

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
                String categorie = binding.categorie.getText().toString().trim();
                String nota = binding.nota.getText().toString().trim();
                String data=binding.data.getText().toString().trim();
                if (suma.length() <= 0) {
                    return;
                }
                if (tip_tranzactie.length() <= 0) {
                    Toast.makeText(AddNewTransaction.this, "Selecteaza tip tranzactie", Toast.LENGTH_SHORT).show();
                }

                String id = UUID.randomUUID().toString();
                Map<String, Object> tranzactii = new HashMap<>();
                tranzactii.put("id", id);
                tranzactii.put("suma", suma);
                tranzactii.put("categorie", categorie);
                tranzactii.put("nota", nota);
                tranzactii.put("tip", tip_tranzactie);
                tranzactii.put("data", data);
                db.collection("Cheltuieli").document(id)
                        .set(tranzactii)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddNewTransaction.this, "Adaugat", Toast.LENGTH_SHORT).show();
                                binding.categorie.setText("");
                                binding.sumaBani.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddNewTransaction.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

}