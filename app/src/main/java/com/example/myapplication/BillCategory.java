package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.BillCategoryBinding;

public class BillCategory extends AppCompatActivity {

    BillCategoryBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BillCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
