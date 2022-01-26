package com.example.recepti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.recepti.models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddNewRecipeActivity extends AppCompatActivity {

    private EditText title, description, imagePath;
    private TextView category;
    private Button insertRecipe;
    private List<Recipe> recipeList;

    private String mCategory;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_recipe);

        category = findViewById(R.id.category_view);
        title = findViewById(R.id.title_view);
        description = findViewById(R.id.recpie_description);
        imagePath = findViewById(R.id.image_path);
        insertRecipe = findViewById(R.id.insert_recipe_button);

        dbRef = FirebaseDatabase.getInstance().getReference("recipe");
        recipeList = new ArrayList<>();

        mCategory = "";
        if (getIntent().hasExtra("category")) {
            mCategory = getIntent().getStringExtra("category");
            category.setText(mCategory);
        }

        insertRecipe.setOnClickListener(v -> addToDatabase());
    }

    private void addToDatabase() {
        String titleStr = title.getText().toString().trim();
        String descriptionStr = description.getText().toString().trim();
        String imagePathStr = imagePath.getText().toString().trim();
        String uid = UUID.randomUUID().toString();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        String currentUserStr = currentUser.getEmail();


        if(titleStr.isEmpty()) {
            title.setError("Title is required");
            title.requestFocus();
            return;
        }

        if(descriptionStr.isEmpty()) {
            description.setError("Description is required");
            description.requestFocus();
            return;
        }

        if(imagePathStr.isEmpty()) {
            imagePath.setError("Image path is required");
            imagePath.requestFocus();
            return;
        }

        Recipe recipe = new Recipe(mCategory, titleStr, imagePathStr, descriptionStr, currentUserStr);
        dbRef.child(uid).setValue(recipe);
        finish();
    }
}