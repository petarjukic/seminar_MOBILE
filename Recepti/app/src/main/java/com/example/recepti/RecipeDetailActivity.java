package com.example.recepti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeDescription, numberOfRates;
    private Button loginButton, logoutButton;
    private RatingBar recipeRate;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipeDescription = findViewById(R.id.description_recpie_view);
        numberOfRates = findViewById(R.id.number_view);
        loginButton = findViewById(R.id.login_button2);
        logoutButton = findViewById(R.id.logout_button2);
        recipeRate = findViewById(R.id.rate_bar);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null) {
            loginButton.setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(v -> {
                Intent intent = new Intent(RecipeDetailActivity.this, LoginUserActivity.class);
                startActivity(intent);
                loginButton.setVisibility(View.GONE);
            });
            Toast.makeText(RecipeDetailActivity.this, "You are not logged in", Toast.LENGTH_SHORT).show();
        }
        else {
            loginButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);

            logoutButton.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(RecipeDetailActivity.this, "You are signed out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RecipeDetailActivity.this, MainActivity.class));
            });
        }



        // OVO JE ZA DOHVACANJE ODABRANIH VRIJEDNOSTI, A MENI TRIBA PRIKAZIVANJE
        int numOfStars = recipeRate.getNumStars();
        float getStarRating = recipeRate.getRating(); //OVO BI TRIBA DOHVATIT IZ BAZE

        //OVO BI MOGLO BIT ZA POSTAVLJANJE OCJENE TJ ZA PRIKAZ
        recipeRate.setRating(getStarRating);
    }
}