package com.example.recepti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recepti.R;
import com.example.recepti.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyViewAdapter extends RecyclerView.Adapter<MyViewAdapter.MyViewHolder> {
    private final List<Recipe> recipeList;
    private final Context context;

    public MyViewAdapter(Context context, List<Recipe> recipeList) {
        this.recipeList = recipeList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.getTitle().setText(recipeList.get(position).getTitle());
        holder.getDescription().setText(recipeList.get(position).getDescription());
        Picasso.get().load(recipeList.get(position).getImage()).into(holder.getImage());
        //ZA SLIKU
        //Picasso.get().load(items.get(position).owner.getAvatar_url()).into(holder.getImageView());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;
        private final ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title_view);
            description = itemView.findViewById(R.id.recpie_description);
            image = itemView.findViewById(R.id.recipe_image);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getDescription() {
            return description;
        }

        public ImageView getImage() {
            return image;
        }
    }
}
