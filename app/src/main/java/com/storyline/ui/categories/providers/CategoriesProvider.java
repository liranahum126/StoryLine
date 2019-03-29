package com.storyline.ui.categories.providers;

import com.storyline.R;
import com.storyline.ui.categories.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoriesProvider {


    public static ArrayList<Category> getCategories() {

        ArrayList<Category> categories = new ArrayList<>();

        List<String> adventureCategoryLines = new ArrayList<>();
        adventureCategoryLines.add("The story begins when we met a shy dino...");
        adventureCategoryLines.add("I found this treasure map and i had to fly to...");
        adventureCategoryLines.add("Quietly we entered into the tomb to find...");

        categories.add(new Category("Adventures",
                R.drawable.storyline_theme_bg_adventure,
                R.drawable.storyline_story_logo_adventure,
                0,
                adventureCategoryLines));

        List<String> fairytaleCategoryLines = new ArrayList<>();
        fairytaleCategoryLines.add("Once upon a time the kingdom was under...");
        fairytaleCategoryLines.add("In a faraway land there was a magical...");
        fairytaleCategoryLines.add("The princess was sad the day because...");

        categories.add(new Category("Fairytale",
                R.drawable.storyline_theme_bg_fairytale,
                R.drawable.storyline_story_logo_fairytale,
                0,
                fairytaleCategoryLines));

        List<String> superHeroCategoryLines = new ArrayList<>();
        superHeroCategoryLines.add("The night was dark and the city was in trouble...");
        superHeroCategoryLines.add("The giant eye ball is attack us again...");
        superHeroCategoryLines.add("We need someone to save us from the...");

        categories.add(new Category("Super Hero",
                R.drawable.storyline_theme_bg_superhero,
                R.drawable.storyline_story_logo_superhero,
                0,
                superHeroCategoryLines));

        return categories;
    }
}
