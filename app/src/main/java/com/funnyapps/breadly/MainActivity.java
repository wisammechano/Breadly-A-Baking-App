package com.funnyapps.breadly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.funnyapps.breadly.models.Recipe;

import java.util.List;

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
        mAdapter = new RecipesAdapter();
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
