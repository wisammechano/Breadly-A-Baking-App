package com.funnyapps.breadly;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.funnyapps.breadly.models.Recipe;
import com.funnyapps.breadly.models.Step;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    static final String ARG_STEP_ID = "step_id";
    static final String ARG_RECIPE_ID = "recipe_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Recipe recipe;
    private Step step;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEP_ID) && getArguments().containsKey(ARG_RECIPE_ID)) {
            recipe = RecipesRepo.get().getRecipe(getArguments().getInt(ARG_RECIPE_ID));
            int stepId = getArguments().getInt(ARG_STEP_ID);
            for (Step s : recipe.getSteps()) {
                if (s.getId() == stepId) step = s;
            }
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(recipe.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (step != null) {
            ((TextView) rootView.findViewById(R.id.step_detail)).setText(step.getDescription());
        }

        return rootView;
    }
}
