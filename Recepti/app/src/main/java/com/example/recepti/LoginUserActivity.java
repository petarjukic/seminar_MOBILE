package com.example.recepti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginUserActivity extends AppCompatActivity {

    private Button register, home;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        register = findViewById(R.id.register);
        signIn = findViewById(R.id.login);
        editTextEmail =  findViewById(R.id.email);
        editTextPassword =  findViewById(R.id.password);
        progressBar = findViewById(R.id.loading);
        home = findViewById(R.id.home);

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(v -> {
            Intent intent = new Intent(LoginUserActivity.this, RegisterUserActivity.class);
            startActivity(intent);
        });

        signIn.setOnClickListener(v -> userLogin());

        home.setOnClickListener(v -> {
            Intent intent = new Intent(LoginUserActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is necessary");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Password length must be 6 charactes");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Intent intent = new Intent(LoginUserActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(LoginUserActivity.this, "Login failed!", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }
}