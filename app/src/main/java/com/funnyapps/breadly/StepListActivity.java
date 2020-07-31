package com.funnyapps.breadly;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.funnyapps.breadly.models.Ingredient;
import com.funnyapps.breadly.models.Recipe;
import com.funnyapps.breadly.models.Step;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity implements IngredientsFragment.OnListFragmentInteractionListener {

    public static final String RECIPE_ID = "recipe_id";
    @BindView(R.id.steps_list)
    public RecyclerView stepsRV;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Recipe recipe;
    private IngredientsFragment ingredientsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        ButterKnife.bind(this);
        int recipeId = getIntent().getIntExtra(RECIPE_ID, -1);
        recipe = RecipesRepo.get().getRecipe(recipeId);
        if (recipe == null) {
            Toast.makeText(this, getString(R.string.invalid_id), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(recipe.getName());


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        stepsRV.setLayoutManager(new LinearLayoutManager(this));
        setupRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        stepsRV.setAdapter(new StepsAdapter(this, recipe, mTwoPane));
    }

    private void dispatchIngredientsDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment dialogFragment = IngredientsFragment.newInstance(recipe.getId());
        dialogFragment.show(ft, "dialog");
    }

    @Override
    public void onListFragmentInteraction(Ingredient item) {
        for (Ingredient in : recipe.getIngredients()) {
            if (in.getIngredient().equals(item.getIngredient()) && in.getMeasure().equals(item.getMeasure())) {
                in.setChecked(true);
            }
        }
    }

    public static class StepsAdapter
            extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

        private final StepListActivity mParentActivity;
        private final Recipe recipe;
        private final boolean mTwoPane;
        private SparseBooleanArray stepsChecked;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                stepsChecked.put(pos, true);
                notifyDataSetChanged();
                if (pos == 0) {
                    //Ingredient click
                    //We a DialogFragment to show a checkable bill of materials
                    mParentActivity.dispatchIngredientsDialog();

                } else if (false) {
                    Step s = recipe.getSteps().get(pos - 1);
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(StepDetailFragment.ARG_STEP_ID, s.getId());
                        arguments.putInt(StepDetailFragment.ARG_RECIPE_ID, recipe.getId());
                        StepDetailFragment fragment = new StepDetailFragment();
                        fragment.setArguments(arguments);
                        mParentActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.step_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, StepDetailActivity.class);
                        intent.putExtra(StepDetailFragment.ARG_STEP_ID, s.getId());
                        intent.putExtra(StepDetailFragment.ARG_RECIPE_ID, recipe.getId());

                        context.startActivity(intent);
                    }
                }
            }
        };

        StepsAdapter(StepListActivity parent, Recipe recipe, boolean twoPane) {
            this.recipe = recipe;
            stepsChecked = new SparseBooleanArray(recipe.getSteps().size() + 1); //1 is for ingredients item
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Boolean checked = stepsChecked.get(position);

            if (checked) {
                holder.mContainer.setBackgroundResource(R.drawable.step_bg_checked);
            } else {
                holder.mContainer.setBackgroundResource(R.drawable.step_bg_unchecked);
            }

            if (position == 0) {
                if (checked) {
                    holder.mCheckedView.setImageResource(R.drawable.ic_shopping_cart_gr);
                } else {
                    holder.mCheckedView.setImageResource(R.drawable.ic_shopping_cart);
                }
                holder.mDescView.setText(mParentActivity.getString(R.string.ingredients));
            } else {
                Step s = recipe.getSteps().get(position - 1);
                holder.mDescView.setText(s.getShortDescription());
                if (checked) {
                    holder.mCheckedView.setImageResource(R.drawable.ic_check);
                } else {
                    holder.mCheckedView.setImageResource(R.drawable.ic_play);
                }
            }
            holder.mContainer.setTag(position);
            holder.mContainer.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return recipe.getSteps().size() + 1;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final FrameLayout mContainer;
            @BindView(R.id.step_check)
            ImageView mCheckedView;
            @BindView(R.id.step_desc)
            TextView mDescView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
                mContainer = (FrameLayout) view;
            }
        }
    }
}
