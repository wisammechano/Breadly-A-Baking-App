package com.funnyapps.breadly;

import com.funnyapps.breadly.models.Recipe;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

class MainViewModel extends ViewModel {
    private MutableLiveData<List<Recipe>> recipes;

    public MainViewModel() {
        recipes = new MutableLiveData<>();
        RecipesRepo.get().getRecipes(recipes);
    }

    LiveData<List<Recipe>> getRecipesLD() {
        return recipes;
    }
}
