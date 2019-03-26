package com.storyline.ui.categories.providers;

import com.storyline.ui.categories.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoriesProvider {


    public static ArrayList<Category> getCategories() {

        ArrayList<Category> categories = new ArrayList<>();

        List<String> categoryLines = new ArrayList<>();
        categoryLines.add("Once upon a time...");
        categoryLines.add("Once upon a time...");
        categoryLines.add("Once upon a time...");

        categories.add(new Category("Adventures", 0, categoryLines));
        categories.add(new Category("Action", 0, categoryLines));
        categories.add(new Category("Horror", 0, categoryLines));
        categories.add(new Category("Love", 0, categoryLines));

        return categories;
    }
}
