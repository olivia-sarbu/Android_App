package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.BillsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Bills extends AppCompatActivity {

    BillsBinding binding;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    ArrayList<BillModel> billModelList;
    BillAdapter billAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=BillsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        /*firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    try{
                        startActivity(new Intent(Bills.this, MainActivity.class));
                        finish();
                    }catch (Exception e){

                    }
                }
            }
        });*/
        billModelList=new ArrayList<>();

        binding.billsRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.billsRecycler.setHasFixedSize(true);

        binding.addNewBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    startActivity(new Intent(Bills.this, BillCategory.class));
                }
                catch (Exception e){

                }
            }
        });

        binding.refreshPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    startActivity(new Intent(Bills.this, Bills.class));
                    finish();
                }catch (Exception e){

                }
            }
        });

        getData();
    }

    private void getData() {
        String id = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection("Facturi")
                // .whereEqualTo("id", id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    //List<ExpenseModel> transModelArrayList = new ArrayList<>();

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
                        //binding.billsRecycler.removeAllViews();
                        //billAdapter.updateData(billModelList);
                        billAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the failure to retrieve data
                });
    }

}
