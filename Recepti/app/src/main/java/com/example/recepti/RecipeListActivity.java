package com.example.recepti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.recepti.adapter.MyViewAdapter;
import com.example.recepti.models.Category;
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
import java.util.Locale;


public class RecipeListActivity extends AppCompatActivity {

    private RecyclerView recView;
    private Button loginButton, logoutButton, homeButton;

    private List<Recipe> recipeList;
    private MyViewAdapter recipeAdapter;
    private RecyclerView recViewRecipe;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        dbRef = FirebaseDatabase.getInstance().getReference("recipe");

        recViewRecipe = findViewById(R.id.recipe_rec_view);
        recViewRecipe.setLayoutManager(new LinearLayoutManager(this));

        loginButton = findViewById(R.id.login_button);
        logoutButton = findViewById(R.id.logout_button);
        homeButton = findViewById(R.id.home_button);

        recView = findViewById(R.id.recipe_rec_view);
        recView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        recipeList = new ArrayList<>();
        recipeAdapter = new MyViewAdapter(this, recipeList);
        recViewRecipe.setAdapter(recipeAdapter);

        if (currentUser == null) {
            loginButton.setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(v -> {
                Intent intent = new Intent(RecipeListActivity.this, LoginUserActivity.class);
                startActivity(intent);
                loginButton.setVisibility(View.GONE);
            });
            Toast.makeText(RecipeListActivity.this, "You are not logged in", Toast.LENGTH_SHORT).show();
        } else {
            loginButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);

            logoutButton.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(RecipeListActivity.this, "You are signed out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RecipeListActivity.this, MainActivity.class));
            });
        }

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeListActivity.this, MainActivity.class);
            startActivity(intent);
        });
        String category = "";
        if (getIntent().hasExtra("category")) {
            category = getIntent().getStringExtra("category");
            category = category.toLowerCase();
            //Toast.makeText(getApplicationContext(), category + " ovo je u kategoriji", Toast.LENGTH_SHORT).show();
        }

        Query query = FirebaseDatabase.getInstance().getReference("recipe")
                .orderByChild("category")
                .equalTo(category);

        query.addListenerForSingleValueEvent(valueEventListener);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                recipeList.add(recipe);
            }
            recipeAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}