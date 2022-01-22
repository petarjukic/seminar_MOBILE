package com.example.recepti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recepti.models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeDescription, numberOfRates, recipeTitle;
    private Button loginButton, logoutButton, homeButton;
    private RatingBar recipeRate;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipeTitle = findViewById(R.id.title_detail);
        recipeDescription = findViewById(R.id.description_recpie_view);
        numberOfRates = findViewById(R.id.number_view);
        loginButton = findViewById(R.id.login_button2);
        logoutButton = findViewById(R.id.logout_button2);
        homeButton = findViewById(R.id.home2);
        recipeRate = findViewById(R.id.rate_bar);

        dbRef = FirebaseDatabase.getInstance().getReference("recipe");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeDetailActivity.this, MainActivity.class);
            startActivity(intent);
        });

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

        String title = "";
        if(getIntent().hasExtra("detailView")) {
            title = getIntent().getStringExtra("detailView");
        }

        Query query = FirebaseDatabase.getInstance().getReference("recipe")
                .orderByChild("title")
                .equalTo(title);

        query.addListenerForSingleValueEvent(valueEventListener);

        // OVO JE ZA DOHVACANJE ODABRANIH VRIJEDNOSTI, A MENI TRIBA PRIKAZIVANJE
        int numOfStars = recipeRate.getNumStars();
        float getStarRating = recipeRate.getRating(); //OVO BI TRIBA DOHVATIT IZ BAZE

        //OVO BI MOGLO BIT ZA POSTAVLJANJE OCJENE TJ ZA PRIKAZ
        recipeRate.setRating(getStarRating);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                setActivity(recipe);
                Log.d("category", recipe.getTitle());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private void setActivity(Recipe recipe) {
        //Toast.makeText(getApplicationContext(), recipe.getTitle(), Toast.LENGTH_LONG).show();
        recipeTitle.setText(recipe.getTitle());
        recipeDescription.setText(recipe.getDescription());
    }
}