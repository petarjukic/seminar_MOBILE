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
    private String titleIntent;
    private String categoryDetail;
    private String descriptionDetail;
    private String createdUser;

    private List<Rate> rateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        rateList = new ArrayList<>();
        mRate = new Rate();

        titleIntent = "";
        categoryDetail = "";
        descriptionDetail = "";
        createdUser = "";

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
        if(getIntent().hasExtra("user")) {
            assert currentUser != null;
            if(currentUser.getEmail().equals(getIntent().getStringExtra("user"))) { // check if current user is creator of recipe
                recipeRate.setVisibility(View.INVISIBLE);
            }
            else {
                recipeRate.setVisibility(View.VISIBLE);
            }
            logoutButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            logoutButton.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(RecipeDetailActivity.this, "You are signed out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RecipeDetailActivity.this, MainActivity.class));
            });
        }

        if (getIntent().hasExtra("detailView") && getIntent().hasExtra("description")
            && getIntent().hasExtra("user") && getIntent().hasExtra("categoryDetail")
        ) {
            titleIntent = getIntent().getStringExtra("detailView");
            categoryDetail = getIntent().getStringExtra("categoryDetail");
            descriptionDetail = getIntent().getStringExtra("description");
            createdUser = getIntent().getStringExtra("user");
        }

        checkForMark();
        recipeRate.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            assert currentUser != null;
            boolean flag = false;
            String rateValue = String.valueOf(recipeRate.getRating());
            uid = UUID.randomUUID().toString();

            for(Rate rate: rateList) {
                if(rate.getUser().equals(currentUser.getEmail()) && titleIntent.equals(rate.getRecipeId())) {
                    flag = true;
                    dbRefRate.child(rate.getUid()).child("mark").setValue((int) Double.parseDouble(rateValue));
                    break;
                }
                if(rate.getMark() == (int) Double.parseDouble(rateValue)) {
                    flag = true;
                }
            }
            if(!flag) {
                mRate.setUid(uid);
                mRate.setMark((int) Double.parseDouble(rateValue));
                mRate.setUser(currentUser.getEmail());
                mRate.setRecipeId(titleIntent);
                dbRefRate.child(uid).setValue(mRate);
            }
            setActivity();
        });

        setActivity();
    }

    private void checkForMark() {
        dbRefRate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Rate mmRate = dataSnapshot.getValue(Rate.class);
                    rateList.add(mmRate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setActivity() {
        recipeTitle.setText(titleIntent);
        recipeDescription.setText(descriptionDetail);
        authorName.setText(createdUser);
        categotyDetailTextView.setText(categoryDetail);

        int averageRate = 0;
        double average = 0.0;
        if (rateList.size() > 0) {
            for(Rate rate: rateList) {
                averageRate += rate.getMark();
            }
            average = (double) (averageRate / rateList.size());
            numberOfRatesNum.setText(Integer.toString(rateList.size()));
        }
        numberView.setText(Double.valueOf(average).toString());
    }
}