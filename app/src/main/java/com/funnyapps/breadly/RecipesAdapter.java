package com.funnyapps.breadly;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyapps.breadly.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private final List<Recipe> list = new ArrayList<>();

    void setList(List<Recipe> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe r = list.get(position);
        if (!r.getImage().isEmpty())
            Picasso.with(holder.recipeImage.getContext()).load(r.getImage()).into(holder.recipeImage);
        holder.recipeName.setText(r.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_iv)
        ImageView recipeImage;
        @BindView(R.id.recipe_tv)
        TextView recipeName;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Typeface tf = Typeface.createFromAsset(itemView.getContext().getAssets(), "Pacifico-Regular.ttf");
            recipeName.setTypeface(tf);
        }
    }
}
