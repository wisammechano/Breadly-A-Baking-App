package com.funnyapps.breadly;

import android.app.Application;

import com.funnyapps.breadly.models.Recipe;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

class RecipesRepo {
    private static RecipesRepo sInstance;
    private List<Recipe> recipes;

    static RecipesRepo get() {
        if (sInstance == null) {
            sInstance = new RecipesRepo();
        }
        return sInstance;
    }

    private RecipesRepo() {

    }

    Recipe getRecipe(int id) {
        for (Recipe r : recipes) {
            if (r.getId() == id)
                return r;
        }
        return null;
    }

    void getRecipes(final MutableLiveData<List<Recipe>> liveData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Application app = MyApplication.get();
                    if (app != null) {
                        InputStream in = app.getApplicationContext().getAssets().open("bakings.json");
                        String json = Utils.inputStreamToString(in, Charset.forName("UTF-8"));
                        recipes = Utils.parse(json);
                        liveData.postValue(recipes);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).run();
    }
}
