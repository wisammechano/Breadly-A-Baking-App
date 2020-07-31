package com.funnyapps.breadly;

import com.funnyapps.breadly.models.Ingredient;
import com.funnyapps.breadly.models.Recipe;
import com.funnyapps.breadly.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

class Utils {
    private static final String
            ID = "id",
            NAME = "name",
            INGREDIENTS = "ingredients",
            QUANTITY = "quantity",
            MEASURE = "measure",
            INGREDIENT = "ingredient",
            STEPS = "steps",
            SHORT_DESC = "shortDescription",
            DESC = "description",
            VIDEO = "videoURL",
            THUMB = "thumbnailURL",
            SERVINGS = "servings",
            IMAGE = "image";

    static List<Recipe> parse(String json) throws JSONException {
        List<Recipe> recipes = new ArrayList<>();

        JSONArray recipesArray = new JSONArray(json);
        for (int i = 0; i < recipesArray.length(); i++) {
            Recipe recipe = new Recipe();
            JSONObject recipeJson = recipesArray.getJSONObject(i);
            recipe.setId(recipeJson.getInt(ID));
            recipe.setName(recipeJson.getString(NAME));
            recipe.setServings(recipeJson.getInt(SERVINGS));
            recipe.setImage(recipeJson.getString(IMAGE));
            recipe.setIngredients(new ArrayList<Ingredient>());
            recipe.setSteps(new ArrayList<Step>());

            JSONArray ingredientsJson = recipeJson.getJSONArray(INGREDIENTS);
            for (int j = 0; j < ingredientsJson.length(); j++) {
                Ingredient ing = new Ingredient();
                JSONObject ingJson = ingredientsJson.getJSONObject(j);
                ing.setIngredient(ingJson.getString(INGREDIENT));
                ing.setMeasure(ingJson.getString(MEASURE));
                ing.setQuantity(ingJson.getInt(QUANTITY));
                recipe.getIngredients().add(ing);
            }

            JSONArray stepsJson = recipeJson.getJSONArray(STEPS);
            for (int j = 0; j < stepsJson.length(); j++) {
                Step s = new Step();
                JSONObject stepJson = stepsJson.getJSONObject(j);
                s.setDescription(stepJson.getString(DESC));
                s.setShortDescription(stepJson.getString(SHORT_DESC));
                s.setId(stepJson.getInt(ID));
                s.setThumbnailURL(stepJson.getString(THUMB));
                s.setVideoURL(stepJson.getString(VIDEO));
                recipe.getSteps().add(s);
            }

            recipes.add(recipe);
        }

        return recipes;
    }

    static String inputStreamToString(InputStream in, Charset charset) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, charset));
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
