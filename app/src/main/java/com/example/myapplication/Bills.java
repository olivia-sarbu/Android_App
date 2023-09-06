package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.BillsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Bills extends AppCompatActivity {

    BillsBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    static ArrayList<BillModel> billModelList;
    BillAdapter billAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BillsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        billModelList = new ArrayList<>();
        binding.billsRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.billsRecycler.setHasFixedSize(true);

        binding.addNewBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Bills.this, BillCategory.class));
                } catch (Exception e) {

                }
            }
        });

        binding.refreshPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Bills.this, Bills.class));
                    finish();
                } catch (Exception e) {

                }
            }
        });
        getData();
    }

    private void getData() {
        String id = FirebaseAuth.getInstance().getUid();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            FirebaseFirestore.getInstance().collection("Facturi")
                    .whereEqualTo("id", currentUserId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            BillModel model = new BillModel(
                                    doc.getString("id"),
                                    doc.getString("categorie_factura"),
                                    doc.getString("suma_plata"),
                                    doc.getString("termen_limita"),
                                    doc.getString("data_notificare"));

                            billModelList.add(model);
                        }

                        if (billAdapter == null) {
                            billAdapter = new BillAdapter(Bills.this, billModelList);
                            binding.billsRecycler.setAdapter(billAdapter);

                        } else {
                            billAdapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(e -> {
                    });
        }
    }

        @Override
        protected void onStop () {
            super.onStop();
            startService(new Intent(this, NotificationService.class));
        }
}



