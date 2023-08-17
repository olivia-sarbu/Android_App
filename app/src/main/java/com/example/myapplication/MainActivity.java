package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.eazegraph.lib.charts.PieChart;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    ArrayList<ExpenseModel> transModelArrayList;
    ExpensesAdapter adapter;

    int totalVenit=0, totalCheltuieli=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        transModelArrayList=new ArrayList<>();

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setHasFixedSize(true);

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    startActivity(new Intent(MainActivity.this, AddNewTransaction.class));
                }
                catch(Exception e){

                }
            }
        });

        getData();

    }

    private void getData() {
        String id = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection("Cheltuieli")
               // .whereEqualTo("id", id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    //List<ExpenseModel> transModelArrayList = new ArrayList<>();

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        ExpenseModel model = new ExpenseModel(
                                doc.getString("categorie"),
                                doc.getString("id"),
                                doc.getString("suma"),
                                doc.getString("tip"),
                                doc.getString("data"),
                                doc.getString("nota"));
                        int suma = Integer.parseInt(doc.getString("suma"));
                        if(doc.getString("tip").equals("Cheltuiala")){
                            totalCheltuieli=totalCheltuieli+suma;
                        }else if(doc.getString("tip").equals("Venit")){
                            totalVenit=totalVenit+suma;
                        }
                        transModelArrayList.add(model);
                    }

                    binding.totalVenit.setText(String.valueOf(totalVenit));
                    binding.totalChetuieli.setText(String.valueOf(totalCheltuieli));
                    binding.balantaBani.setText(String.valueOf(totalVenit-totalCheltuieli));

                    if (adapter == null) {
                        adapter = new ExpensesAdapter(MainActivity.this, transModelArrayList);
                        binding.recycler.setAdapter(adapter);
                    } else {
                        adapter.updateData(transModelArrayList);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the failure to retrieve data
                });
    }

}