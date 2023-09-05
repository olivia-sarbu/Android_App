package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class PasswordForgotten extends AppCompatActivity {

    EditText emailEditText;
    CardView resetButton;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_forgotten);

        emailEditText = findViewById(R.id.send_email);
        resetButton = findViewById(R.id.send_email_btn);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();

                if (email.isEmpty()) {
                    emailEditText.setError("Introduceti un email!");
                    emailEditText.requestFocus();
                    return;
                }

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PasswordForgotten.this, "A fost trimis un email de resetare a parolei.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PasswordForgotten.this, "Nu s-a putut trimite email de resetare a parolei.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
