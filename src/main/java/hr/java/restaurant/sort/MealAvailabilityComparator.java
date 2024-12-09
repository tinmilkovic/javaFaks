package hr.java.restaurant.sort;

import hr.java.restaurant.model.Meal;

import java.util.Comparator;
import java.util.Map;

public class MealAvailabilityComparator implements Comparator<Meal> {
    private final Map<Meal, Integer> mealToRestaurantCount;

    /**
     * Konstruktor prima mapu koja sadrži jela i broj restorana u kojima su dostupna.
     *
     * @param mealToRestaurantCount mapa gdje je ključ jelo, a vrijednost broj restorana.
     */
    public MealAvailabilityComparator(Map<Meal, Integer> mealToRestaurantCount) {
        this.mealToRestaurantCount = mealToRestaurantCount;
    }

    @Override
    public int compare(Meal meal1, Meal meal2) {
        // Usporedi brojeve restorana u kojima su jela dostupna
        return Integer.compare(mealToRestaurantCount.get(meal2), mealToRestaurantCount.get(meal1));
    }
}
