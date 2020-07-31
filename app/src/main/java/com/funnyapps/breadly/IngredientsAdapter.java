package com.funnyapps.breadly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.funnyapps.breadly.IngredientsFragment.OnListFragmentInteractionListener;
import com.funnyapps.breadly.dummy.DummyContent.DummyItem;
import com.funnyapps.breadly.models.Ingredient;
import com.funnyapps.breadly.models.Recipe;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private final Recipe recipe;
    private final OnListFragmentInteractionListener mListener;

    IngredientsAdapter(Recipe r, OnListFragmentInteractionListener listener) {
        recipe = r;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.ingredient = recipe.getIngredients().get(position);
        holder.mMeasure.setText(holder.ingredient.getMeasure());
        holder.mIngredient.setText(holder.ingredient.getIngredient());
        holder.mQuantity.setText(String.valueOf(holder.ingredient.getQuantity()));
        holder.mChecked.setChecked(holder.ingredient.isChecked());

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    holder.mChecked.setChecked(true);
                    //notifyDataSetChanged();
                }
            }
        });
        holder.mChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListener.onListFragmentInteraction(holder.ingredient);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipe.getIngredients().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mContainer;

        @BindView(R.id.ingredient_tv)
        TextView mIngredient;

        @BindView(R.id.measure_tv)
        TextView mMeasure;

        @BindView(R.id.quantity_tv)
        TextView mQuantity;

        @BindView(R.id.ingredient_check)
        CheckBox mChecked;

        Ingredient ingredient;

        ViewHolder(View view) {
            super(view);
            mContainer = view;
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIngredient.getText() + "'";
        }
    }
}
