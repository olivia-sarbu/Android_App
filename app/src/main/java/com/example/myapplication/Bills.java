package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.BillsBinding;

public class Bills extends AppCompatActivity {

    BillsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=BillsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
    }
}
