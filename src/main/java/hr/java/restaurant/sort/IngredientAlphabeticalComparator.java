package hr.java.restaurant.sort;

import java.util.Comparator;

public class IngredientAlphabeticalComparator implements Comparator<String> {
    @Override
    public int compare(String ingredient1, String ingredient2) {
        return ingredient1.compareToIgnoreCase(ingredient2);
    }
}
