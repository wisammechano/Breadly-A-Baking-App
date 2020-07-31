package com.funnyapps.breadly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.funnyapps.breadly.models.Recipe;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recipes_rv)
    RecyclerView recyclerView;

    @BindView(R.id.root_layout)
    FrameLayout frameLayout;

    RecipesAdapter mAdapter;
    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecipesAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int recipeId = (int) v.getTag();
                Intent i = new Intent(MainActivity.this, StepListActivity.class);
                i.putExtra(StepListActivity.RECIPE_ID, recipeId);
                startActivity(i);
            }
        });
        recyclerView.setAdapter(mAdapter);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        Observer<List<Recipe>> observer = new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> list) {
                mAdapter.setList(list);
            }
        };

        viewModel.getRecipesLD().observe(this, observer);
    }
}
