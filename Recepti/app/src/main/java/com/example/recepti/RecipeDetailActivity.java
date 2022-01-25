package com.example.recepti;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recepti.models.Rate;
import com.example.recepti.models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeDescription, numberView, recipeTitle, authorText, authorName;
    private Button loginButton, logoutButton, homeButton;
    private RatingBar recipeRate;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef, dbRefRate;

    private Rate mRate;
    private String uid;
    private Boolean flag;
    private List<Integer> allRates;
    private List<Integer> nesto;
    private List<Rate> sveOcjene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mRate = new Rate();
        flag = false;
        allRates = new ArrayList<>();
        sveOcjene = new ArrayList<>();
        nesto = new ArrayList<>();

        recipeTitle = findViewById(R.id.title_detail);
        recipeDescription = findViewById(R.id.description_recpie_view);
        numberView = findViewById(R.id.number_view);
        loginButton = findViewById(R.id.login_button2);
        logoutButton = findViewById(R.id.logout_button2);
        homeButton = findViewById(R.id.home2);
        recipeRate = findViewById(R.id.rate_bar);
        authorText = findViewById(R.id.author_text);
        authorName = findViewById(R.id.author_name);

        dbRef = FirebaseDatabase.getInstance().getReference("recipe");
        dbRefRate = FirebaseDatabase.getInstance().getReference("rate");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeDetailActivity.this, MainActivity.class);
            startActivity(intent);
        });

        if (currentUser == null) {
            loginButton.setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(v -> {
                Intent intent = new Intent(RecipeDetailActivity.this, LoginUserActivity.class);
                startActivity(intent);
                loginButton.setVisibility(View.GONE);
            });
            Toast.makeText(RecipeDetailActivity.this, "You are not logged in", Toast.LENGTH_SHORT).show();
        } else {
            loginButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);

            logoutButton.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(RecipeDetailActivity.this, "You are signed out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RecipeDetailActivity.this, MainActivity.class));
            });
        }

        String title = "";
        if (getIntent().hasExtra("detailView")) {
            title = getIntent().getStringExtra("detailView");
        }

        Query query = FirebaseDatabase.getInstance().getReference("recipe")
                .orderByChild("title")
                .equalTo(title);

        query.addListenerForSingleValueEvent(valueEventListener);

        recipeRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String rateValue = String.valueOf(recipeRate.getRating());
                mRate.setMark((int) Double.parseDouble(rateValue));
                uid = UUID.randomUUID().toString();
                flag = true;
                query.addListenerForSingleValueEvent(valueEventListener);
            }
        });
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                Log.d("receptID", "pravi id je " + dataSnapshot.getKey());
                assert recipe != null;

                if (flag) {
                    mRate.setRecipeId(dataSnapshot.getKey());
                    saveRate();
                }
                for (Rate r : sveOcjene) {
                    if (r.getRecipeId().equals(dataSnapshot.getKey())) {
                        nesto.add(r.getMark());
                    }
                }
                setActivity(recipe);
                Log.d("category", recipe.getTitle());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    ValueEventListener rateValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                Rate rrRate = dataSnapshot.getValue(Rate.class);
                assert rrRate != null;
                //Log.d("receptId", "ovo je za ocjene " + rrRate.getMark());
                allRates.add(rrRate.getMark());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private void saveRate() {
        flag = false;
        dbRefRate.child(uid).setValue(mRate);
    }

    private void setActivity(Recipe recipe) {
        recipeTitle.setText(recipe.getTitle());
        recipeDescription.setText(recipe.getDescription());
        authorName.setText(recipe.getUser());
        //Log.d("receptId", dbRef.push().getKey());
        //Log.d("receptId", mRate.getMark() + " ovo je ocjena");

        /*Query rateQuery = FirebaseDatabase.getInstance().getReference("rate")
                .orderByChild("recipeId")
                .equalTo(mRate.getRecipeId());

        rateQuery.addListenerForSingleValueEvent(rateValueEventListener);*/

        dbRefRate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Rate rrRate = dataSnapshot.getValue(Rate.class);
                    sveOcjene.add(rrRate);
                    allRates.add(rrRate.getMark());
                }
                //Log.d("receptId", "cila tablica ocjena " + allRates.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        double averageRate = 0.0;
        String avgRate = "0";
        if (nesto.size() > 0) {
            for (int i = 0; i < nesto.size(); i++) {
                averageRate += nesto.get(i);
            }
            avgRate = Double.valueOf(averageRate / nesto.size()).toString();
        }
        numberView.setText(avgRate);
    }
}