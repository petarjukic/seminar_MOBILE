package com.example.recepti.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recepti.R;
import com.example.recepti.RecipeListActivity;
import com.example.recepti.models.Category;

import java.util.List;

public class MyViewAdapterCategory extends RecyclerView.Adapter<MyViewAdapterCategory.MyViewHolderCategory> {

    private List<Category> categoryList;
    Context context;

    public MyViewAdapterCategory(Context context, List<Category> categoryList) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_view, parent, false);
        return new MyViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderCategory holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryText.setText(category.getCategoryName());

        holder.categoryText.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeListActivity.class);
            intent.putExtra("category", category.getCategoryName());
            //Log.d("categorija", categoryList.get(position).toString());
            context.startActivity(intent);
            //Toast.makeText(context, category.getCategoryName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    class MyViewHolderCategory extends RecyclerView.ViewHolder {

        private TextView categoryText;

        public MyViewHolderCategory(@NonNull View itemView) {
            super(itemView);
            categoryText = itemView.findViewById(R.id.category_name_text);
        }

        public TextView getCategoryText() {
            return categoryText;
        }
    }
}
