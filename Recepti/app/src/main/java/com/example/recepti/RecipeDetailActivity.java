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
    private TextView numberOfRatesText, numberOfRatesNum, categotyDetailTextView, categoryText;
    private Button loginButton, logoutButton, homeButton;
    private RatingBar recipeRate;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRefRate;

    private Rate mRate;
    private String uid;
    private String titleIntent = "";
    private String categoryDetail = "";
    private String descriptionDetail = "";
    private String createdUser = "";
    private List<Integer> allRates;
    private List<Integer> nesto;
    private List<Rate> sveOcjene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mRate = new Rate();
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
        numberOfRatesText = findViewById(R.id.number_of_rates_text);
        numberOfRatesNum = findViewById(R.id.number_of_rates_num);
        categotyDetailTextView = findViewById(R.id.category_detail);
        categoryText = findViewById(R.id.category_text);

        dbRefRate = FirebaseDatabase.getInstance().getReference("rate");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeDetailActivity.this, MainActivity.class);
            startActivity(intent);
        });

        if (currentUser == null) {
            loginButton.setVisibility(View.VISIBLE);
            recipeRate.setVisibility(View.GONE);
            loginButton.setOnClickListener(v -> {
                Intent intent = new Intent(RecipeDetailActivity.this, LoginUserActivity.class);
                startActivity(intent);
            });
            Toast.makeText(RecipeDetailActivity.this, "You are not logged in", Toast.LENGTH_SHORT).show();
        }
        if(getIntent().hasExtra("user")) {  // check if current user is creator of recipe
            if(currentUser.getEmail().equals(getIntent().getStringExtra("user")) && currentUser != null) {
                loginButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.VISIBLE);
                recipeRate.setVisibility(View.INVISIBLE);
                logoutButton.setOnClickListener(v -> {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(RecipeDetailActivity.this, "You are signed out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RecipeDetailActivity.this, MainActivity.class));
                });
            }
        }
        else {
            recipeRate.setVisibility(View.GONE);
        }

        if (getIntent().hasExtra("detailView") && getIntent().hasExtra("description")
            && getIntent().hasExtra("user") && getIntent().hasExtra("categoryDetail")
        ) {
            titleIntent = getIntent().getStringExtra("detailView");
            categoryDetail = getIntent().getStringExtra("categoryDetail");
            descriptionDetail = getIntent().getStringExtra("description");
            createdUser = getIntent().getStringExtra("user");
        }

        recipeRate.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            String rateValue = String.valueOf(recipeRate.getRating());
            mRate.setMark((int) Double.parseDouble(rateValue));
            uid = UUID.randomUUID().toString();
            //query.addListenerForSingleValueEvent(valueEventListener);
        });
        setActivity();
    }

    /*ValueEventListener valueEventListener = new ValueEventListener() {
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
                //setActivity(recipe);
                Log.d("category", recipe.getTitle());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };*/

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

    /*private void saveRate() {
        //flag = false;
        dbRefRate.child(uid).setValue(mRate);
    }*/

    private void setActivity() {
        recipeTitle.setText(titleIntent);
        recipeDescription.setText(descriptionDetail);
        authorName.setText(createdUser);
        categotyDetailTextView.setText(categoryDetail);

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

            numberOfRatesNum.setText(Integer.toString(nesto.size()));
        }
        numberView.setText(avgRate);
    }
}