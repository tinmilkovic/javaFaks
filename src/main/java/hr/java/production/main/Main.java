package hr.java.production.main;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.exception.DuplicateInputException;
import hr.java.restaurant.exception.InvalidValueException;
import hr.java.restaurant.generics.RestaurantLabourExchangeOffice;
import hr.java.restaurant.model.*;
import hr.java.restaurant.sort.EmployeeContractDurationComparator;
import hr.java.restaurant.sort.EmployeeSalaryComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        log.info("The application is started...");

        Scanner scanner = new Scanner(System.in);

        List<Category>categories = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        List<Meal> meals = new ArrayList<>();
        List<Chef> chefs = new ArrayList<>();
        List<Waiter> waiters = new ArrayList<>();
        List<Deliverer> deliverers = new ArrayList<>();
        List<Restaurant> restaurants = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        List<VeganMeal> veganMeals = new ArrayList<>();
        List<VegeterianMeal> vegeterianMeals = new ArrayList<>();
        List<MeatMeal> meatMeals = new ArrayList<>();

        categoryInput(scanner, categories);
        ingredientInput(scanner, categories, ingredients);
        mealInput(scanner, categories, ingredients, meals);
        chefInput(scanner, chefs);
        waiterInput(scanner, waiters);
        delivererInput(scanner, deliverers);
        restaurantInput(scanner, meals, chefs, waiters, deliverers, restaurants);
        RestaurantLabourExchangeOffice<Restaurant> restaurantLabourExchangeOffice = new RestaurantLabourExchangeOffice<>(restaurants);
        orderInput(restaurantLabourExchangeOffice, scanner, meals, deliverers, orders);


        Map<Meal, List<Restaurant>> mealsInRestaurant = restaurants.stream()
                .flatMap(restaurant -> restaurant.getMeals().stream()
                        .map(meal -> Map.entry(meal, restaurant)))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));


        employeeSalaryCompare(restaurants);

        employeeContractLengthCompare(restaurants);


        salaryAndContract(chefs, waiters, deliverers);

        veganMealInput(scanner, categories, ingredients, veganMeals);
        vegeterianMealInput(scanner, categories, ingredients, vegeterianMeals);
        meatMealInput(scanner, categories, ingredients, meatMeals);

        restaurants.stream()
                .max(Comparator.comparingInt(restaurant ->
                        restaurant.getChefs().size() +
                                restaurant.getWaiters().size() +
                                restaurant.getDeliverers().size()
                ))
                .ifPresent(restaurant -> {
                    System.out.println("Restoran s najviše zaposlenika:");
                    System.out.println("Ime: " + restaurant.getName());
                    System.out.println("Adresa: " + restaurant.getAddress().getStreet());
                    System.out.println("Broj zaposlenika: " +
                            (restaurant.getChefs().size() +
                                    restaurant.getWaiters().size() +
                                    restaurant.getDeliverers().size())
                    );
                });


        orders.stream()
                .flatMap(order -> order.getMeals().stream()) // Prikupljanje svih jela iz svih narudžbi
                .collect(Collectors.groupingBy(meal -> meal, Collectors.counting())) // Grupiranje jela i brojanje pojavljivanja
                .entrySet()
                .stream()
                .max(Comparator.comparingLong(Map.Entry::getValue)) // Pronalaženje jela s najvećim brojem narudžbi
                .ifPresent(entry -> {
                    Meal mostOrderedMeal = entry.getKey();
                    Long count = entry.getValue();
                    System.out.println("Najčešće naručivano jelo:");
                    System.out.println("Naziv: " + mostOrderedMeal.getName());
                    System.out.println("Broj narudžbi: " + count);
                });


        double totalOrderPrice = orders.stream()
                .mapToDouble(order -> order.getMeals().stream()
                        .mapToDouble(meal->meal.getPrice().doubleValue())
                        .sum())
                .sum();

        System.out.println("Ukupna cijena svih narudžbi: " + totalOrderPrice);



        // Grupiranje restorana po gradovima
        Map<String, List<Restaurant>> restaurantsByCity = restaurants.stream()
                .collect(Collectors.groupingBy(restaurant -> restaurant.getAddress().getCity()));

        // Ispis rezultata
        restaurantsByCity.forEach((city, cityRestaurants) -> {
            System.out.println("Grad: " + city);
            cityRestaurants.forEach(restaurant ->
                    System.out.println("  Restoran: " + restaurant.getName()));
        });




        System.out.println("a");

    }

    private static void employeeContractLengthCompare(List<Restaurant> restaurants) {
        EmployeeContractDurationComparator<Object> comparatorCon = new EmployeeContractDurationComparator<>();

        for (Restaurant restaurant : restaurants) {
            System.out.println("Restoran: " + restaurant.getName());

            // Kombiniranje svih zaposlenika u jednu listu
            List<Object> allEmployees = new ArrayList<>();
            allEmployees.addAll(restaurant.getChefs());
            allEmployees.addAll(restaurant.getWaiters());
            allEmployees.addAll(restaurant.getDeliverers());

            if (!allEmployees.isEmpty()) {
                // Sortiranje zaposlenika prema trajanju ugovora
                Collections.sort(allEmployees, comparatorCon);

                // Ispis trajanja ugovora zaposlenika
                System.out.println("Sortirana trajanja ugovora zaposlenika:");
                for (Object employee : allEmployees) {
                    long contractDuration = 0;

                    if (employee instanceof Chef) {
                        Chef chef = (Chef) employee;
                        contractDuration = ChronoUnit.DAYS.between(chef.getContract().getStartDate(), chef.getContract().getEndDate());
                        System.out.println("Kuhar: " + chef.getFirstName() + " " + chef.getLastName() + " - Trajanje ugovora: " + contractDuration + " dana");
                    } else if (employee instanceof Waiter) {
                        Waiter waiter = (Waiter) employee;
                        contractDuration = ChronoUnit.DAYS.between(waiter.getContract().getStartDate(), waiter.getContract().getEndDate());
                        System.out.println("Konobar: " + waiter.getFirstName() + " " + waiter.getLastName() + " - Trajanje ugovora: " + contractDuration + " dana");
                    } else if (employee instanceof Deliverer) {
                        Deliverer deliverer = (Deliverer) employee;
                        contractDuration = ChronoUnit.DAYS.between(deliverer.getContract().getStartDate(), deliverer.getContract().getEndDate());
                        System.out.println("Dostavljač: " + deliverer.getFirstName() + " " + deliverer.getLastName() + " - Trajanje ugovora: " + contractDuration + " dana");
                    }
                }
            } else {
                System.out.println("Nema zaposlenika u ovom restoranu.");
            }

            System.out.println("-------------------");
        }
    }

    private static void employeeSalaryCompare(List<Restaurant> restaurants) {
        // Komparator za zaposlenike
        EmployeeSalaryComparator<Object> comparator = new EmployeeSalaryComparator<>();

        for (Restaurant restaurant : restaurants) {
            System.out.println("Restoran: " + restaurant.getName());

            // Kombiniranje svih zaposlenika u jednu listu
            List<Object> allEmployees = new ArrayList<>();
            allEmployees.addAll(restaurant.getChefs());
            allEmployees.addAll(restaurant.getWaiters());
            allEmployees.addAll(restaurant.getDeliverers());

            if (!allEmployees.isEmpty()) {
                // Sortiranje svih zaposlenika prema plaći
                Collections.sort(allEmployees, comparator);

                // Dohvaćanje zaposlenika s najvećom plaćom
                Object highestPaidEmployee = allEmployees.get(0);

                // Ispis zaposlenika s najvećom plaćom
                if (highestPaidEmployee instanceof Chef) {
                    Chef chef = (Chef) highestPaidEmployee;
                    System.out.println("Najplaćeniji zaposlenik: " + chef.getFirstName() + " " + chef.getLastName() +
                            " (Kuhar) - Plaća: " + chef.getContract().getSalary());
                } else if (highestPaidEmployee instanceof Waiter) {
                    Waiter waiter = (Waiter) highestPaidEmployee;
                    System.out.println("Najplaćeniji zaposlenik: " + waiter.getFirstName() + " " + waiter.getLastName() +
                            " (Konobar) - Plaća: " + waiter.getContract().getSalary());
                } else if (highestPaidEmployee instanceof Deliverer) {
                    Deliverer deliverer = (Deliverer) highestPaidEmployee;
                    System.out.println("Najplaćeniji zaposlenik: " + deliverer.getFirstName() + " " + deliverer.getLastName() +
                            " (Dostavljač) - Plaća: " + deliverer.getContract().getSalary());
                }
            } else {
                System.out.println("Nema zaposlenika u ovom restoranu.");
            }

            System.out.println("-------------------");
        }
    }

    private static void calorieCalculation(List<VeganMeal> veganMeals, List<VegeterianMeal> vegeterianMeals, List<MeatMeal> meatMeals) {
        log.info("Method calorieCalculation called.");

        Meal[] allMeals = new Meal[veganMeals.size() + vegeterianMeals.size() + meatMeals.size()];
        System.arraycopy(veganMeals, 0, allMeals, 0, veganMeals.size());
        System.arraycopy(vegeterianMeals, 0, allMeals, veganMeals.size(), vegeterianMeals.size());
        System.arraycopy(meatMeals, 0, allMeals, veganMeals.size() + vegeterianMeals.size(), meatMeals.size());

        Meal minCalorieMeal = allMeals[0];
        Meal maxCalorieMeal = allMeals[0];

        for (Meal meal : allMeals) {
            if (meal.getTotalCalories().compareTo(minCalorieMeal.getTotalCalories()) < 0) {
                minCalorieMeal = meal;
            }
            if (meal.getTotalCalories().compareTo(maxCalorieMeal.getTotalCalories()) > 0) {
                maxCalorieMeal = meal;
            }
        }

        System.out.println("Meal with Minimum Calories:");
        printMealDetails(minCalorieMeal);

        System.out.println("Meal with Maximum Calories:");
        printMealDetails(maxCalorieMeal);
    }

    private static void printMealDetails(Meal meal) {
        log.info("Method printMealDetails called.");

        System.out.println("Name: " + meal.getName());
        System.out.println("Category: " + meal.getCategory().getName());
        System.out.println("Ingredients: ");
        for (Ingredient ingredient : meal.getIngredients()) {
            if (ingredient != null) {
                System.out.println("- " + ingredient.getName() + " (" + ingredient.getKcal() + " kcal)");
            }
        }
        System.out.println("Price: " + meal.getPrice());
        System.out.println("Total Calories: " + meal.getTotalCalories());
    }

    private static void meatMealInput(Scanner scanner, List<Category> categories, List<Ingredient> ingredients, List<MeatMeal> meatMeals) {
        log.info("Method meatMealInput called.");

        for (int i = 0; i < 3; i++) {
            log.info("Starting meat meal input for Meat Meal {}.", i + 1);

            // Name input with validation
            String name = "";
            boolean validName = false;

            while (!validName) {
                System.out.print("Name " + (i + 1) + ": ");
                name = scanner.nextLine();

                try {
                    for (MeatMeal meatMeal : meatMeals) {
                        if (meatMeal != null && meatMeal.getName().equalsIgnoreCase(name)) {
                            throw new DuplicateInputException("The meat meal \"" + name + "\" already exists. Please enter a unique name.");
                        }
                    }
                    validName = true;
                    log.info("User entered valid name for Meat Meal {}: {}", i + 1, name);
                } catch (DuplicateInputException e) {
                    log.warn("Duplicate meat meal name detected: {}", name);
                    System.out.println(e.getMessage());
                }
            }

            // Category selection with validation
            int selectedCategory = -1;
            while (true) {
                try {
                    int j = 1;
                    System.out.println("Category " + (i + 1) + ": ");
                    for (Category category : categories) {
                        System.out.println(j + "." + category.getName());
                        j++;
                    }
                    System.out.print("Selection: ");
                    selectedCategory = scanner.nextInt();
                    scanner.nextLine(); // Clear the buffer

                    if (selectedCategory > 0 && selectedCategory <= categories.size()) {
                        log.info("User selected Category {}: {}", selectedCategory, categories.get(selectedCategory - 1).getName());
                        break;
                    } else {
                        log.warn("Invalid category selection: {}", selectedCategory);
                        System.out.println("Invalid selection. Please choose a valid category.");
                    }
                } catch (InputMismatchException e) {
                    log.error("Invalid input for category selection: {}", selectedCategory, e);
                    System.out.println("Invalid input. Please enter a valid number for the category.");
                    scanner.nextLine();  // Clear the buffer
                }
            }

            // Ingredient selection with validation
            boolean end = false;
            Set<Ingredient> ingredientsSet = new HashSet<>();
            while (!end) {
                int selectedIngredient = -1;
                while (true) {
                    try {
                        int j = 1;
                        System.out.println("Ingredient selection for meal " + (i + 1) + ": ");
                        for (Ingredient ingredient : ingredients) {
                            System.out.println(j + "." + ingredient.getName());
                            j++;
                        }
                        System.out.println("0. Finish");
                        System.out.print("Selection: ");
                        selectedIngredient = scanner.nextInt();
                        log.info("User selected ingredient {} for meal {}: {}", selectedIngredient, i + 1, name);

                        if (selectedIngredient < 0 || selectedIngredient > ingredients.size()) {
                            log.warn("Invalid ingredient selection: {}. Must be between 0 and {}", selectedIngredient, ingredients.size());
                            System.out.println("Invalid selection. Please choose a valid ingredient number or 0 to finish.");
                            continue;
                        }
                        break;
                    } catch (InputMismatchException e) {
                        log.error("InputMismatchException caught: Invalid input for ingredient selection.", e);
                        System.out.println("Invalid input. Please enter a valid number for the ingredient.");
                        scanner.nextLine();
                    }
                }
                scanner.nextLine();

                if (selectedIngredient == 0) {
                    end = true;
                } else {
                    Ingredient selected = ingredients.get(selectedIngredient - 1);
                    if (!ingredientsSet.add(selected)) {
                        System.out.println("Ingredient \"" + selected.getName() + "\" is already added to the meal.");
                    } else {
                        log.info("Ingredient added to meal {}: {}", name, selected.getName());
                    }
                }
            }

            // Price input with validation
            BigDecimal price = null;
            while (price == null) {
                try {
                    System.out.print("Price " + (i + 1) + ": ");
                    price = scanner.nextBigDecimal();
                    scanner.nextLine(); // Clear the buffer
                    log.info("User entered price for Meat Meal {}: {}", i + 1, price);
                } catch (InputMismatchException e) {
                    log.error("Invalid input for price selection.", e);
                    System.out.println("Invalid input. Please enter a valid number for the price.");
                    scanner.nextLine(); // Clear the buffer
                }
            }

            // Meat type input with validation
            String meatType = null;
            while (meatType == null || meatType.isEmpty()) {
                System.out.print("Meat type " + (i + 1) + ": ");
                meatType = scanner.nextLine().trim();
                if (meatType.isEmpty()) {
                    log.warn("Empty meat type entered for Meat Meal {}.", i + 1);
                    System.out.println("Meat type cannot be empty. Please enter a valid meat type.");
                }
            }
            log.info("User entered meat type for Meat Meal {}: {}", i + 1, meatType);

            // Create MeatMeal object
            meatMeals.add(new MeatMeal(name, categories.get(selectedCategory - 1), ingredientsSet, price, meatType));
            log.info("Meat Meal {} created with name: {}, category: {}, price: {}, meat type: {}",
                    i + 1, name, categories.get(selectedCategory - 1).getName(), price, meatType);
        }

        log.info("Method meatMealInput completed.");
    }

    private static void vegeterianMealInput(Scanner scanner, List<Category> categories, List<Ingredient> ingredients, List<VegeterianMeal> vegeterianMeals) {
        log.info("Method vegeterianMealInput called.");

        for (int i = 0; i < 3; i++) {
            log.info("Starting vegeterian meal input for Vegeterian Meal {}.", i + 1);

            // Name input with validation
            String name = "";
            boolean validName = false;

            while (!validName) {
                System.out.print("Name " + (i + 1) + ": ");
                name = scanner.nextLine();

                try {
                    for (VegeterianMeal vegeterianMeal : vegeterianMeals) {
                        if (vegeterianMeal != null && vegeterianMeal.getName().equalsIgnoreCase(name)) {
                            throw new DuplicateInputException("The vegeterian meal \"" + name + "\" already exists. Please enter a unique name.");
                        }
                    }
                    validName = true;
                    log.info("User entered valid name for Vegeterian Meal {}: {}", i + 1, name);
                } catch (DuplicateInputException e) {
                    log.warn("Duplicate vegeterian meal name detected: {}", name);
                    System.out.println(e.getMessage());
                }
            }

            // Category selection with validation
            int selectedCategory = -1;
            while (true) {
                try {
                    int j = 1;
                    System.out.println("Category " + (i + 1) + ": ");
                    for (Category category : categories) {
                        System.out.println(j + "." + category.getName());
                        j++;
                    }
                    System.out.print("Selection: ");
                    selectedCategory = scanner.nextInt();
                    scanner.nextLine(); // Clear the buffer

                    if (selectedCategory > 0 && selectedCategory <= categories.size()) {
                        log.info("User selected Category {}: {}", selectedCategory, categories.get(selectedCategory - 1).getName());
                        break;
                    } else {
                        log.warn("Invalid category selection: {}", selectedCategory);
                        System.out.println("Invalid selection. Please choose a valid category.");
                    }
                } catch (InputMismatchException e) {
                    log.error("Invalid input for category selection: {}", selectedCategory, e);
                    System.out.println("Invalid input. Please enter a valid number for the category.");
                    scanner.nextLine();  // Clear the buffer
                }
            }

            // Ingredient selection with validation
            boolean end = false;
            Set<Ingredient> ingredientsSet = new HashSet<>();
            while (!end) {
                int selectedIngredient = -1;
                while (true) {
                    try {
                        int j = 1;
                        System.out.println("Ingredient selection for meal " + (i + 1) + ": ");
                        for (Ingredient ingredient : ingredients) {
                            System.out.println(j + "." + ingredient.getName());
                            j++;
                        }
                        System.out.println("0. Finish");
                        System.out.print("Selection: ");
                        selectedIngredient = scanner.nextInt();
                        log.info("User selected ingredient {} for meal {}: {}", selectedIngredient, i + 1, name);

                        if (selectedIngredient < 0 || selectedIngredient > ingredients.size()) {
                            log.warn("Invalid ingredient selection: {}. Must be between 0 and {}", selectedIngredient, ingredients.size());
                            System.out.println("Invalid selection. Please choose a valid ingredient number or 0 to finish.");
                            continue;
                        }
                        break;
                    } catch (InputMismatchException e) {
                        log.error("InputMismatchException caught: Invalid input for ingredient selection.", e);
                        System.out.println("Invalid input. Please enter a valid number for the ingredient.");
                        scanner.nextLine();
                    }
                }
                scanner.nextLine();

                if (selectedIngredient == 0) {
                    end = true;
                } else {
                    Ingredient selected = ingredients.get(selectedIngredient - 1);
                    if (!ingredientsSet.add(selected)) {
                        System.out.println("Ingredient \"" + selected.getName() + "\" is already added to the meal.");
                    } else {
                        log.info("Ingredient added to meal {}: {}", name, selected.getName());
                    }
                }
            }

            // Price input with validation
            BigDecimal price = null;
            while (price == null) {
                try {
                    System.out.print("Price " + (i + 1) + ": ");
                    price = scanner.nextBigDecimal();
                    scanner.nextLine(); // Clear the buffer
                    log.info("User entered price for Vegeterian Meal {}: {}", i + 1, price);
                } catch (InputMismatchException e) {
                    log.error("Invalid input for price selection.", e);
                    System.out.println("Invalid input. Please enter a valid number for the price.");
                    scanner.nextLine(); // Clear the buffer
                }
            }

            // Eggs selection
            boolean eggs = false;
            while (true) {
                System.out.print("Does the meal have eggs (yes/no): ");
                String eggsInput = scanner.nextLine().toLowerCase();
                if (eggsInput.equals("yes")) {
                    eggs = true;
                    log.info("User confirmed the meal contains eggs.");
                    break;
                } else if (eggsInput.equals("no")) {
                    eggs = false;
                    log.info("User confirmed the meal does not contain eggs.");
                    break;
                } else {
                    log.warn("Invalid input for eggs selection: {}", eggsInput);
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }

            // Create VegeterianMeal object
            vegeterianMeals.add(new VegeterianMeal(name, categories.get(selectedCategory - 1), ingredientsSet, price, eggs));
            log.info("Vegeterian Meal {} created with name: {}, category: {}, price: {}, eggs: {}",
                    i + 1, name, categories.get(selectedCategory - 1).getName(), price, eggs);
        }

        log.info("Method vegeterianMealInput completed.");
    }

    private static void veganMealInput(Scanner scanner, List<Category> categories, List<Ingredient> ingredients, List<VeganMeal> veganMeals) {
        log.info("Method veganMealInput called.");

        for (int i = 0; i < 3; i++) {
            log.info("Starting vegan meal input for Vegan Meal {}.", i + 1);

            // Name input with validation
            String name = "";
            boolean validName = false;

            while (!validName) {
                System.out.print("Name " + (i + 1) + ": ");
                name = scanner.nextLine();

                try {
                    for (VeganMeal veganMeal : veganMeals) {
                        if (veganMeal != null && veganMeal.getName().equalsIgnoreCase(name)) {
                            throw new DuplicateInputException("The vegan meal \"" + name + "\" already exists. Please enter a unique name.");
                        }
                    }
                    validName = true;
                    log.info("User entered valid name for Vegan Meal {}: {}", i + 1, name);
                } catch (DuplicateInputException e) {
                    log.warn("Duplicate vegan meal name detected: {}", name);
                    System.out.println(e.getMessage());
                }
            }

            // Category selection with validation
            int selectedCategory = -1;
            while (true) {
                try {
                    int j = 1;
                    System.out.println("Category " + (i + 1) + ": ");
                    for (Category category : categories) {
                        System.out.println(j + "." + category.getName());
                        j++;
                    }
                    System.out.print("Selection: ");
                    selectedCategory = scanner.nextInt();
                    scanner.nextLine(); // Clear the buffer

                    if (selectedCategory > 0 && selectedCategory <= categories.size()) {
                        log.info("User selected Category {}: {}", selectedCategory, categories.get(selectedCategory - 1).getName());
                        break;
                    } else {
                        log.warn("Invalid category selection: {}", selectedCategory);
                        System.out.println("Invalid selection. Please choose a valid category.");
                    }
                } catch (InputMismatchException e) {
                    log.error("Invalid input for category selection: {}", selectedCategory, e);
                    System.out.println("Invalid input. Please enter a valid number for the category.");
                    scanner.nextLine();  // Clear the buffer
                }
            }

            // Ingredient selection with validation
            boolean end = false;
            Set<Ingredient> ingredientsSet = new HashSet<>();
            int count = 0;
            int selectedIngredient = -1;
            while (!end) {
                try {
                    int j = 1;
                    System.out.println("Ingredient " + (i + 1) + ": ");
                    for (Ingredient ingredient : ingredients) {
                        System.out.println(j + "." + ingredient.getName());
                        j++;
                    }
                    System.out.println("0. finish");
                    System.out.print("Selection: ");
                    selectedIngredient = scanner.nextInt();
                    scanner.nextLine(); // Clear the buffer

                    if (selectedIngredient == 0) {
                        end = true;
                        log.info("User finished ingredient selection.");
                    } else if (selectedIngredient > 0 && selectedIngredient <= ingredients.size()) {
                        ingredientsSet.add(ingredients.get(selectedIngredient - 1));
                        log.info("User selected Ingredient {}: {}", selectedIngredient, ingredients.get(selectedIngredient - 1).getName());
                        count++;
                    } else {
                        log.warn("Invalid ingredient selection: {}", selectedIngredient);
                        System.out.println("Invalid selection. Please choose a valid ingredient.");
                    }
                } catch (InputMismatchException e) {
                    log.error("Invalid input for ingredient selection: {}", selectedIngredient, e);
                    System.out.println("Invalid input. Please enter a valid number for the ingredient.");
                    scanner.nextLine();  // Clear the buffer
                }
            }

            // Price input with validation
            BigDecimal price = null;
            while (price == null) {
                try {
                    System.out.print("Price " + (i + 1) + ": ");
                    price = scanner.nextBigDecimal();
                    scanner.nextLine(); // Clear the buffer
                    log.info("User entered price for Vegan Meal {}: {}", i + 1, price);
                } catch (InputMismatchException e) {
                    log.error("Invalid input for price selection.", e);
                    System.out.println("Invalid input. Please enter a valid number for the price.");
                    scanner.nextLine(); // Clear the buffer
                }
            }

            // Portion size input
            System.out.print("Portion size " + (i + 1) + ": ");
            String portionSize = scanner.nextLine();
            log.info("User entered portion size for Vegan Meal {}: {}", i + 1, portionSize);

            // Create VeganMeal object
            veganMeals.add(new VeganMeal(name, categories.get(selectedCategory - 1), ingredientsSet, price, portionSize));
            log.info("Vegan Meal {} created with name: {}, category: {}, price: {}, portion size: {}",
                    i + 1, name, categories.get(selectedCategory - 1).getName(), price, portionSize);
        }

        log.info("Method veganMealInput completed.");
    }

    private static void salaryAndContract(List<Chef> chefs, List<Waiter> waiters, List<Deliverer> deliverers) {
        log.info("Method salaryAndContract called.");

        Object[] employees = new Object[chefs.size() + waiters.size() + deliverers.size()];
        int index = 0;

        for (Chef chef : chefs) {
            employees[index++] = chef;
        }
        for (Waiter waiter : waiters) {
            employees[index++] = waiter;
        }
        for (Deliverer deliverer : deliverers) {
            employees[index++] = deliverer;
        }

        Object highestSalaryEmployee = null;
        Object earliestStartEmployee = null;
        BigDecimal highestSalary = BigDecimal.ZERO;
        LocalDate earliestStartDate = LocalDate.now();

        for (Object employee : employees) {
            BigDecimal salary = BigDecimal.ZERO;
            LocalDate startDate = LocalDate.now();

            if (employee instanceof Chef) {
                Chef chef = (Chef) employee;
                salary = chef.getContract().getSalary();
                startDate = chef.getContract().getStartDate();
            } else if (employee instanceof Waiter) {
                Waiter waiter = (Waiter) employee;
                salary = waiter.getContract().getSalary();
                startDate = waiter.getContract().getStartDate();
            } else if (employee instanceof Deliverer) {
                Deliverer deliverer = (Deliverer) employee;
                salary = deliverer.getContract().getSalary();
                startDate = deliverer.getContract().getStartDate();
            }

            if (salary.compareTo(highestSalary) > 0) {
                highestSalary = salary;
                highestSalaryEmployee = employee;
            }

            if (startDate.isBefore(earliestStartDate)) {
                earliestStartDate = startDate;
                earliestStartEmployee = employee;
            }
        }

        System.out.println("Employee with the biggest salary:");
        printEmployeeInfo(highestSalaryEmployee);

        System.out.println("Employee with the longest contract:");
        printEmployeeInfo(earliestStartEmployee);
    }

    private static void printEmployeeInfo(Object employee) {
        log.info("Method printEmployeeInfo called.");

        if (employee instanceof Chef) {
            Chef chef = (Chef) employee;
            System.out.println("First name: " + chef.getFirstName());
            System.out.println("Last name: " + chef.getLastName());
            System.out.println("Salary: " + chef.getContract().getSalary());
            System.out.println("Start of contract: " + chef.getContract().getStartDate());
        } else if (employee instanceof Waiter) {
            Waiter waiter = (Waiter) employee;
            System.out.println("First name: " + waiter.getFirstName());
            System.out.println("Last name: " + waiter.getLastName());
            System.out.println("Salary: " + waiter.getContract().getSalary());
            System.out.println("Start of contract: " + waiter.getContract().getStartDate());
        } else if (employee instanceof Deliverer) {
            Deliverer deliverer = (Deliverer) employee;
            System.out.println("First name: " + deliverer.getFirstName());
            System.out.println("Last name: " + deliverer.getLastName());
            System.out.println("Salary: " + deliverer.getContract().getSalary());
            System.out.println("Start of contract: " + deliverer.getContract().getStartDate());
        }
    }

    private static void orderInput(RestaurantLabourExchangeOffice<Restaurant> restaurantsGen, Scanner scanner, List<Meal> meals, List<Deliverer> deliverers, List<Order> orders) {
        log.info("Method orderInput called.");

        for (int i = 0; i < 3; i++) {
            log.info("Starting order input for Order {}.", i + 1);

            // Dohvati restorane iz RestaurantLabourExchangeOffice
            List<Restaurant> restaurants = restaurantsGen.getRestaurants();
            int j = 1;
            System.out.println("Restaurant " + (i + 1) + ": ");
            for (Restaurant restaurant : restaurants) {
                System.out.println(j + "." + restaurant.getName());
                j++;
            }

            int selectedRestaurant = -1;
            while (true) {
                try {
                    System.out.print("Selection: ");
                    selectedRestaurant = scanner.nextInt();
                    if (selectedRestaurant > 0 && selectedRestaurant <= restaurants.size()) {
                        log.info("User selected Restaurant {}: {}", selectedRestaurant, restaurants.get(selectedRestaurant - 1).getName());
                        break;
                    } else {
                        log.warn("Invalid restaurant selection. User selected: {}", selectedRestaurant);
                        System.out.println("Invalid choice. Please select a valid restaurant.");
                    }
                } catch (InputMismatchException e) {
                    log.error("InputMismatchException caught while selecting restaurant.", e);
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine();
                }
            }

            // Daljnji kod za izbor jela i dostavljača ostaje isti
            boolean end = false;
            List<Meal> mealsList = new ArrayList<>();
            while (!end) {
                System.out.println("Meal " + (i + 1) + ": ");
                j = 1;
                for (Meal meal : meals) {
                    System.out.println(j + "." + meal.getName());
                    j++;
                }
                System.out.println("0.finish");
                int selectedMeal = -1;
                while (true) {
                    try {
                        System.out.print("Selection: ");
                        selectedMeal = scanner.nextInt();
                        if (selectedMeal >= 0 && selectedMeal <= meals.size()) {
                            if (selectedMeal == 0) {
                                log.info("User finished meal selection.");
                                end = true;
                                break;
                            } else {
                                mealsList.add(meals.get(selectedMeal - 1));
                                log.info("User selected Meal {}: {}", selectedMeal, meals.get(selectedMeal - 1).getName());
                            }
                        } else {
                            log.warn("Invalid meal selection. User selected: {}", selectedMeal);
                            System.out.println("Invalid choice. Please select a valid meal.");
                        }
                    } catch (InputMismatchException e) {
                        log.error("InputMismatchException caught while selecting meal.", e);
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.nextLine();
                    }
                }
            }

            // Deliverer selection
            int selectedDeliverer = -1;
            j = 1;
            System.out.println("Deliverer " + (i + 1) + ": ");
            for (Deliverer deliverer : deliverers) {
                System.out.println(j + "." + deliverer.getFirstName());
                j++;
            }
            while (true) {
                try {
                    System.out.print("Selection: ");
                    selectedDeliverer = scanner.nextInt();
                    if (selectedDeliverer > 0 && selectedDeliverer <= deliverers.size()) {
                        log.info("User selected Deliverer {}: {}", selectedDeliverer, deliverers.get(selectedDeliverer - 1).getFirstName());
                        break;
                    } else {
                        log.warn("Invalid deliverer selection. User selected: {}", selectedDeliverer);
                        System.out.println("Invalid choice. Please select a valid deliverer.");
                    }
                } catch (InputMismatchException e) {
                    log.error("InputMismatchException caught while selecting deliverer.", e);
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine();
                }
            }
            scanner.nextLine(); // Consume the newline character after deliverer selection

            // Create order object
            orders.add(new Order(restaurants.get(selectedRestaurant - 1), mealsList, deliverers.get(selectedDeliverer - 1), LocalDateTime.now()));
            log.info("Order {} created for Restaurant: {}, Deliverer: {}, Meals: {}", i + 1,
                    restaurants.get(selectedRestaurant - 1).getName(), deliverers.get(selectedDeliverer - 1).getFirstName());
        }

        log.info("Method orderInput completed.");
    }



    private static void restaurantInput(Scanner scanner, List<Meal> meals, List<Chef> chefs, List<Waiter> waiters, List<Deliverer> deliverers, List<Restaurant> restaurants) {
        log.info("Method restaurantInput called.");

        for (int i = 0; i < 3; i++) {
            String name = "";
            boolean validName = false;

            while (!validName) {
                System.out.print("Name " + (i + 1) + ": ");
                name = scanner.nextLine();
                log.info("User input for restaurant name {}: {}", i + 1, name);

                try {
                    for (Restaurant restaurant : restaurants) {
                        if (restaurant != null && restaurant.getName().equalsIgnoreCase(name)) {
                            String errorMessage = "The restaurant name \"" + name + "\" already exists. Please enter a unique name.";
                            log.warn(errorMessage);
                            throw new DuplicateInputException(errorMessage);
                        }
                    }
                    validName = true;
                } catch (DuplicateInputException e) {
                    log.error("DuplicateInputException caught: {}", e.getMessage());
                    System.out.println(e.getMessage());
                }
            }

            // Input for address details
            System.out.print("Street " + (i + 1) + ": ");
            String street = scanner.nextLine();
            log.info("User input for street of restaurant {}: {}", i + 1, street);

            System.out.print("House number " + (i + 1) + ": ");
            String houseNumber = scanner.nextLine();
            log.info("User input for house number of restaurant {}: {}", i + 1, houseNumber);

            System.out.print("City " + (i + 1) + ": ");
            String city = scanner.nextLine();
            log.info("User input for city of restaurant {}: {}", i + 1, city);

            System.out.print("Postal code " + (i + 1) + ": ");
            String postalCode = scanner.nextLine();
            log.info("User input for postal code of restaurant {}: {}", i + 1, postalCode);

            // Meal selection
            int j = 1;
            boolean end = false;
            Set<Meal> mealsSet = new HashSet<>();
            while (!end) {
                System.out.println("Meal " + (i + 1) + ": ");
                for (Meal meal : meals) {
                    System.out.println(j + "." + meal.getName());
                    j++;
                }
                System.out.println("0.finish");

                int selectedMeal = -1;
                while (true) {
                    try {
                        System.out.print("Selection: ");
                        selectedMeal = scanner.nextInt();
                        log.info("User input for meal selection of restaurant {}: {}", i + 1, selectedMeal);
                        break;
                    } catch (InputMismatchException e) {
                        log.error("InputMismatchException caught: Invalid input for meal selection.", e);
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.nextLine();
                    }
                }
                scanner.nextLine();
                j = 1;
                if (selectedMeal == 0) {
                    end = true;
                } else {
                    Meal selected = meals.get(selectedMeal - 1);
                    if(!mealsSet.add(selected)){
                        System.out.println("Meal \"" + selected.getName() + "\" is already added to the restaurant.");
                    }else{
                        log.info("Meal added to restaurant {}: {}", name, selected.getName());
                    }
                }
            }

            // Chef selection
            j = 1;
            end = false;
            Set<Chef> chefsSet = new HashSet<>();
            while (!end) {
                System.out.println("Chef " + (i + 1) + ": ");
                for (Chef chef : chefs) {
                    System.out.println(j + "." + chef.getFirstName());
                    j++;
                }
                System.out.println("0.finish");

                int selectedChef = -1;
                while (true) {
                    try {
                        System.out.print("Selection: ");
                        selectedChef = scanner.nextInt();
                        log.info("User input for chef selection of restaurant {}: {}", i + 1, selectedChef);
                        break;
                    } catch (InputMismatchException e) {
                        log.error("InputMismatchException caught: Invalid input for chef selection.", e);
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.nextLine();
                    }
                }
                scanner.nextLine();
                j = 1;
                if (selectedChef == 0) {
                    end = true;
                } else {
                    Chef selected = chefs.get(selectedChef - 1);
                    if(!chefsSet.add(selected)){
                        System.out.println("Chef \"" + selected.getFirstName() + " " + selected.getLastName() +  "\" is already added to the restaurant.");
                    }else{
                        log.info("Chef added to restaurant {}: {} {}", name, selected.getFirstName(), selected.getLastName());
                    }
                }
            }

            // Waiter selection
            j = 1;
            end = false;
            Set<Waiter> waitersSet = new HashSet<>();
            while (!end) {
                System.out.println("Waiter " + (i + 1) + ": ");
                for (Waiter waiter : waiters) {
                    System.out.println(j + "." + waiter.getFirstName());
                    j++;
                }
                System.out.println("0.finish");

                int selectedWaiter = -1;
                while (true) {
                    try {
                        System.out.print("Selection: ");
                        selectedWaiter = scanner.nextInt();
                        log.info("User input for waiter selection of restaurant {}: {}", i + 1, selectedWaiter);
                        break;
                    } catch (InputMismatchException e) {
                        log.error("InputMismatchException caught: Invalid input for waiter selection.", e);
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.nextLine();
                    }
                }
                scanner.nextLine();
                j = 1;
                if (selectedWaiter == 0) {
                    end = true;
                } else {
                    Waiter selected = waiters.get(selectedWaiter - 1);
                    if(!waitersSet.add(selected)){
                        System.out.println("Waiter \"" + selected.getFirstName() + " " + selected.getLastName() +  "\" is already added to the restaurant.");
                    }else{
                        log.info("Waiter added to restaurant {}: {} {}", name, selected.getFirstName(), selected.getLastName());
                    }
                }
            }

            // Deliverer selection
            j = 1;
            end = false;
            Set<Deliverer> deliverersSet = new HashSet<>();
            while (!end) {
                System.out.println("Deliverer " + (i + 1) + ": ");
                for (Deliverer deliverer : deliverers) {
                    System.out.println(j + "." + deliverer.getFirstName());
                    j++;
                }
                System.out.println("0.finish");

                int selectedDeliverer = -1;
                while (true) {
                    try {
                        System.out.print("Selection: ");
                        selectedDeliverer = scanner.nextInt();
                        log.info("User input for deliverer selection of restaurant {}: {}", i + 1, selectedDeliverer);
                        break;
                    } catch (InputMismatchException e) {
                        log.error("InputMismatchException caught: Invalid input for deliverer selection.", e);
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.nextLine();
                    }
                }
                scanner.nextLine();
                j = 1;
                if (selectedDeliverer == 0) {
                    end = true;
                } else {
                    Deliverer selected = deliverers.get(selectedDeliverer - 1);
                    if(!deliverersSet.add(selected)){
                        System.out.println("Deliverer \"" + selected.getFirstName() + " " + selected.getLastName() +  "\" is already added to the restaurant.");
                    }else{
                        log.info("Deliverer added to restaurant {}: {} {}", name, selected.getFirstName(), selected.getLastName());
                    }
                }
            }

            // Create restaurant object
            restaurants.add(new Restaurant(name, new Address.Builder()
                    .street(street)
                    .houseNumber(houseNumber)
                    .city(city)
                    .postalCode(postalCode)
                    .build(), mealsSet, chefsSet, waitersSet, deliverersSet));
            log.info("Restaurant created with name: {}, street: {}, house number: {}, city: {}, postal code: {}, meals: {}, chefs: {}, waiters: {}, deliverers: {}",
                    name, street, houseNumber, city, postalCode, mealsSet.size(), chefsSet.size(), waitersSet.size(), deliverersSet.size());
        }

        log.info("Method restorantInput execution completed.");
    }

    private static void delivererInput(Scanner scanner, List<Deliverer> deliverers) {
        log.info("Method delivererInput called.");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (int i = 0; i < 3; i++) {
            String firstName = "";
            String lastName = "";
            boolean validName = false;

            while (!validName) {
                System.out.print("First name " + (i + 1) + ": ");
                firstName = scanner.nextLine();
                log.info("User input for deliverer's first name {}: {}", i + 1, firstName);

                System.out.print("Last name " + (i + 1) + ": ");
                lastName = scanner.nextLine();
                log.info("User input for deliverer's last name {}: {}", i + 1, lastName);

                try {
                    for (Deliverer deliverer : deliverers) {
                        if (deliverer != null && deliverer.getFirstName().equalsIgnoreCase(firstName) && deliverer.getLastName().equalsIgnoreCase(lastName)) {
                            String errorMessage = "A deliverer with the name " + firstName + " " + lastName + " already exists.";
                            log.warn(errorMessage);
                            throw new DuplicateInputException(errorMessage);
                        }
                    }
                    validName = true;
                } catch (DuplicateInputException e) {
                    log.error("DuplicateInputException caught: {}", e.getMessage());
                    System.out.println(e.getMessage());
                }
            }

            BigDecimal salary = null;
            boolean validSalary = false;
            while (!validSalary) {
                try {
                    System.out.print("Salary " + (i + 1) + ": ");
                    salary = scanner.nextBigDecimal();
                    log.info("User input for salary of deliverer {}: {}", i + 1, salary);

                    if (salary.compareTo(BigDecimal.valueOf(600)) < 0) {
                        String errorMessage = "Salary cannot be less than 600.";
                        log.warn(errorMessage);
                        throw new InvalidValueException(errorMessage);
                    }
                    validSalary = true;
                } catch (InputMismatchException e) {
                    log.error("InputMismatchException caught: Invalid input for salary.", e);
                    System.out.println("Invalid input. Please enter a valid number for salary.");
                    scanner.nextLine();
                } catch (InvalidValueException e) {
                    log.error("InvalidValueException caught: {}", e.getMessage());
                    System.out.println(e.getMessage());
                    scanner.nextLine();
                }
            }
            scanner.nextLine();

            BigDecimal bonusAmount = null;
            while (true) {
                try {
                    System.out.print("Bonus " + (i + 1) + ": ");
                    bonusAmount = scanner.nextBigDecimal();
                    log.info("User input for bonus amount of deliverer {}: {}", i + 1, bonusAmount);
                    break;
                } catch (InputMismatchException e) {
                    log.error("InputMismatchException caught: Invalid input for bonus.", e);
                    System.out.println("Invalid input. Please enter a valid decimal number for bonus.");
                    scanner.nextLine();
                }
            }
            scanner.nextLine();

            Bonus bonus = new Bonus(bonusAmount);

            System.out.print("Start date (dd-MM-yyyy) " + (i + 1) + ": ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine(), dateFormatter);
            log.info("User input for start date of deliverer {}: {}", i + 1, startDate);

            System.out.print("End date (dd-MM-yyyy) " + (i + 1) + ": ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine(), dateFormatter);
            log.info("User input for end date of deliverer {}: {}", i + 1, endDate);

            System.out.print("Contract type (FULL_TIME/PART_TIME) " + (i + 1) + ": ");
            ContractType contractType = ContractType.valueOf(scanner.nextLine().toUpperCase());
            log.info("User input for contract type of deliverer {}: {}", i + 1, contractType);

            Contract contract = new Contract(salary, startDate, endDate, contractType);

            deliverers.add(new Deliverer.Builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .contract(contract)
                    .bonus(bonus)
                    .build());
            log.info("Deliverer created with first name: {}, last name: {}, salary: {}, start date: {}, end date: {}, contract type: {}, bonus amount: {}",
                    firstName, lastName, salary, startDate, endDate, contractType, bonusAmount);
        }

        log.info("Method delivererInput execution completed.");
    }

    private static void waiterInput(Scanner scanner, List<Waiter> waiters) {
        log.info("Method waiterInput called.");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (int i = 0; i < 3; i++) {
            String firstName = "";
            String lastName = "";
            boolean validName = false;

            while (!validName) {
                System.out.print("First name " + (i + 1) + ": ");
                firstName = scanner.nextLine();
                log.info("User input for waiter's first name {}: {}", i + 1, firstName);

                System.out.print("Last name " + (i + 1) + ": ");
                lastName = scanner.nextLine();
                log.info("User input for waiter's last name {}: {}", i + 1, lastName);

                try {
                    for (Waiter waiter : waiters) {
                        if (waiter != null && waiter.getFirstName().equalsIgnoreCase(firstName) && waiter.getLastName().equalsIgnoreCase(lastName)) {
                            String errorMessage = "A waiter with the name " + firstName + " " + lastName + " already exists.";
                            log.warn(errorMessage);
                            throw new DuplicateInputException(errorMessage);
                        }
                    }
                    validName = true;
                } catch (DuplicateInputException e) {
                    log.error("DuplicateInputException caught: {}", e.getMessage());
                    System.out.println(e.getMessage());
                }
            }

            BigDecimal salary = null;
            boolean validSalary = false;
            while (!validSalary) {
                try {
                    System.out.print("Salary " + (i + 1) + ": ");
                    salary = scanner.nextBigDecimal();
                    log.info("User input for salary of waiter {}: {}", i + 1, salary);

                    if (salary.compareTo(BigDecimal.valueOf(600)) < 0) {
                        String errorMessage = "Salary cannot be less than 600.";
                        log.warn(errorMessage);
                        throw new InvalidValueException(errorMessage);
                    }
                    validSalary = true;
                } catch (InputMismatchException e) {
                    log.error("InputMismatchException caught: Invalid input for salary.", e);
                    System.out.println("Invalid input. Please enter a valid number for salary.");
                    scanner.nextLine();
                } catch (InvalidValueException e) {
                    log.error("InvalidValueException caught: {}", e.getMessage());
                    System.out.println(e.getMessage());
                    scanner.nextLine();
                }
            }
            scanner.nextLine();

            BigDecimal bonusAmount = null;
            while (true) {
                try {
                    System.out.print("Bonus " + (i + 1) + ": ");
                    bonusAmount = scanner.nextBigDecimal();
                    log.info("User input for bonus amount of waiter {}: {}", i + 1, bonusAmount);
                    break;
                } catch (InputMismatchException e) {
                    log.error("InputMismatchException caught: Invalid input for bonus.", e);
                    System.out.println("Invalid input. Please enter a valid decimal number for bonus.");
                    scanner.nextLine();
                }
            }
            scanner.nextLine();

            Bonus bonus = new Bonus(bonusAmount);

            System.out.print("Start date (dd-MM-yyyy) " + (i + 1) + ": ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine(), dateFormatter);
            log.info("User input for start date of waiter {}: {}", i + 1, startDate);

            System.out.print("End date (dd-MM-yyyy) " + (i + 1) + ": ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine(), dateFormatter);
            log.info("User input for end date of waiter {}: {}", i + 1, endDate);

            System.out.print("Contract type (FULL_TIME/PART_TIME) " + (i + 1) + ": ");
            ContractType contractType = ContractType.valueOf(scanner.nextLine().toUpperCase());
            log.info("User input for contract type of waiter {}: {}", i + 1, contractType);

            Contract contract = new Contract(salary, startDate, endDate, contractType);

            waiters.add(new Waiter.Builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .contract(contract)
                    .bonus(bonus)
                    .build());
            log.info("Waiter created with first name: {}, last name: {}, salary: {}, start date: {}, end date: {}, contract type: {}, bonus amount: {}",
                    firstName, lastName, salary, startDate, endDate, contractType, bonusAmount);
        }

        log.info("Method waiterInput execution completed.");
    }

    private static void chefInput(Scanner scanner, List<Chef> chefs) {
        log.info("Method chefInput called.");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (int i = 0; i < 3; i++) {
            String firstName = "";
            String lastName = "";
            boolean validName = false;

            while (!validName) {
                System.out.print("First name " + (i + 1) + ": ");
                firstName = scanner.nextLine();
                log.info("User input for chef's first name {}: {}", i + 1, firstName);

                System.out.print("Last name " + (i + 1) + ": ");
                lastName = scanner.nextLine();
                log.info("User input for chef's last name {}: {}", i + 1, lastName);

                try {
                    for (Chef chef : chefs) {
                        if (chef != null && chef.getFirstName().equalsIgnoreCase(firstName) && chef.getLastName().equalsIgnoreCase(lastName)) {
                            String errorMessage = "A chef with the name " + firstName + " " + lastName + " already exists.";
                            log.warn(errorMessage);
                            throw new DuplicateInputException(errorMessage);
                        }
                    }
                    validName = true;
                } catch (DuplicateInputException e) {
                    log.error("DuplicateInputException caught: {}", e.getMessage());
                    System.out.println(e.getMessage());
                }
            }

            BigDecimal salary = null;
            boolean validSalary = false;
            while (!validSalary) {
                try {
                    System.out.print("Salary " + (i + 1) + ": ");
                    salary = scanner.nextBigDecimal();
                    log.info("User input for salary of chef {}: {}", i + 1, salary);

                    if (salary.compareTo(BigDecimal.valueOf(600)) < 0) {
                        String errorMessage = "Salary cannot be less than 600.";
                        log.warn(errorMessage);
                        throw new InvalidValueException(errorMessage);
                    }
                    validSalary = true;
                } catch (InputMismatchException e) {
                    log.error("InputMismatchException caught: Invalid input for salary.", e);
                    System.out.println("Invalid input. Please enter a valid number for salary.");
                    scanner.nextLine();
                } catch (InvalidValueException e) {
                    log.error("InvalidValueException caught: {}", e.getMessage());
                    System.out.println(e.getMessage());
                    scanner.nextLine();
                }
            }
            scanner.nextLine();

            BigDecimal bonusAmount = null;
            while (true) {
                try {
                    System.out.print("Bonus " + (i + 1) + ": ");
                    bonusAmount = scanner.nextBigDecimal();
                    log.info("User input for bonus amount of chef {}: {}", i + 1, bonusAmount);
                    break;
                } catch (InputMismatchException e) {
                    log.error("InputMismatchException caught: Invalid input for bonus.", e);
                    System.out.println("Invalid input. Please enter a valid decimal number for bonus.");
                    scanner.nextLine();
                }
            }
            scanner.nextLine();

            Bonus bonus = new Bonus(bonusAmount);

            System.out.print("Start date (dd-MM-yyyy) " + (i + 1) + ": ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine(), dateFormatter);
            log.info("User input for start date of chef {}: {}", i + 1, startDate);

            System.out.print("End date (dd-MM-yyyy) " + (i + 1) + ": ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine(), dateFormatter);
            log.info("User input for end date of chef {}: {}", i + 1, endDate);

            System.out.print("Contract type (FULL_TIME/PART_TIME) " + (i + 1) + ": ");
            ContractType contractType = ContractType.valueOf(scanner.nextLine().toUpperCase());
            log.info("User input for contract type of chef {}: {}", i + 1, contractType);

            Contract contract = new Contract(salary, startDate, endDate, contractType);

            chefs.add(new Chef.Builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .contract(contract)
                    .bonus(bonus)
                    .build());
            log.info("Chef created with first name: {}, last name: {}, salary: {}, start date: {}, end date: {}, contract type: {}, bonus amount: {}", firstName, lastName, salary, startDate, endDate, contractType, bonusAmount);
        }

        log.info("Method chefInput execution completed.");
    }

    private static void mealInput(Scanner scanner, List<Category> categories, List<Ingredient> ingredients, List<Meal> meals) {
        log.info("Method mealInput called.");

        for (int i = 0; i < 3; i++) {
            String name = "";
            boolean validName = false;

            while (!validName) {
                System.out.print("Name " + (i + 1) + ": ");
                name = scanner.nextLine();
                log.info("User input for meal name {}: {}", i + 1, name);

                try {
                    for (Meal meal : meals) {
                        if (meal != null && meal.getName().equalsIgnoreCase(name)) {
                            String errorMessage = "The meal name \"" + name + "\" already exists. Please enter a unique name.";
                            log.warn(errorMessage);
                            throw new DuplicateInputException(errorMessage);
                        }
                    }
                    validName = true;
                } catch (DuplicateInputException e) {
                    log.error("DuplicateInputException caught: {}", e.getMessage());
                    System.out.println(e.getMessage());
                }
            }

            int selectedCategory = -1;
            while (true) {
                try {
                    int j = 1;
                    System.out.println("Category " + (i + 1) + ": ");
                    for (Category category : categories) {
                        System.out.println(j + "." + category.getName());
                        j++;
                    }
                    System.out.print("Selection: ");
                    selectedCategory = scanner.nextInt();
                    log.info("User selected category {} for meal {}: {}", selectedCategory, i + 1, name);

                    if (selectedCategory < 1 || selectedCategory > categories.size()) {
                        log.warn("Invalid category selection: {}. Must be between 1 and {}", selectedCategory, categories.size());
                        System.out.println("Invalid selection. Please choose a number between 1 and " + categories.size() + ".");
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    log.error("InputMismatchException caught: Invalid input for category selection.", e);
                    System.out.println("Invalid input. Please enter a valid number for the category.");
                    scanner.nextLine();
                }
            }
            scanner.nextLine();

            boolean end = false;
            Set<Ingredient> ingredientSet = new HashSet<>();
            while (!end) {
                int selectedIngredient = -1;
                while (true) {
                    try {
                        int j = 1;
                        System.out.println("Ingredient selection for meal " + (i + 1) + ": ");
                        for (Ingredient ingredient : ingredients) {
                            System.out.println(j + "." + ingredient.getName());
                            j++;
                        }
                        System.out.println("0. Finish");
                        System.out.print("Selection: ");
                        selectedIngredient = scanner.nextInt();
                        log.info("User selected ingredient {} for meal {}: {}", selectedIngredient, i + 1, name);

                        if (selectedIngredient < 0 || selectedIngredient > ingredients.size()) {
                            log.warn("Invalid ingredient selection: {}. Must be between 0 and {}", selectedIngredient, ingredients.size());
                            System.out.println("Invalid selection. Please choose a valid ingredient number or 0 to finish.");
                            continue;
                        }
                        break;
                    } catch (InputMismatchException e) {
                        log.error("InputMismatchException caught: Invalid input for ingredient selection.", e);
                        System.out.println("Invalid input. Please enter a valid number for the ingredient.");
                        scanner.nextLine();
                    }
                }
                scanner.nextLine();

                if (selectedIngredient == 0) {
                    end = true;
                } else {
                    Ingredient selected = ingredients.get(selectedIngredient - 1);
                    if (!ingredientSet.add(selected)) {
                        System.out.println("Ingredient \"" + selected.getName() + "\" is already added to the meal.");
                    } else {
                        log.info("Ingredient added to meal {}: {}", name, selected.getName());
                    }
                }
            }

            BigDecimal price = null;
            while (true) {
                try {
                    System.out.print("Price " + (i + 1) + ": ");
                    price = scanner.nextBigDecimal();
                    log.info("User input for price of meal {}: {}", i + 1, price);

                    if (price.compareTo(BigDecimal.ZERO) <= 0) {
                        System.out.println("Price must be greater than zero. Please enter a valid price.");
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    log.error("InputMismatchException caught: Invalid input for price.", e);
                    System.out.println("Invalid input. Please enter a valid decimal number for price.");
                    scanner.nextLine();
                }
            }
            scanner.nextLine();

            meals.add(new Meal(name, categories.get(selectedCategory - 1), ingredientSet, price));
            log.info("Meal created with name: {}, category: {}, ingredients count: {}, price: {}",
                    name, categories.get(selectedCategory - 1).getName(), ingredientSet.size(), price);
        }

        log.info("Method mealInput execution completed.");
    }

    private static void ingredientInput(Scanner scanner, List<Category> categories, List<Ingredient> ingredients) {
        log.info("Method ingredientInput called.");

        System.out.println("Ingredients:");
        for (int i = 0; i < 5; i++) {
            String name = "";
            boolean validName = false;

            while (!validName) {
                System.out.print("Name " + (i + 1) + ": ");
                name = scanner.nextLine();
                log.info("User input for ingredient name {}: {}", i + 1, name);

                try {
                    for (Ingredient ingredient : ingredients) {
                        if (ingredient != null && ingredient.getName().equalsIgnoreCase(name)) {
                            String errorMessage = "The ingredient name \"" + name + "\" already exists. Please enter a unique name.";
                            log.warn(errorMessage);
                            throw new DuplicateInputException(errorMessage);
                        }
                    }
                    validName = true;
                } catch (DuplicateInputException e) {
                    log.error("DuplicateInputException caught: {}", e.getMessage());
                    System.out.println(e.getMessage());
                }
            }

            int selectedCategory = -1;
            while (true) {
                try {
                    int j = 1;
                    System.out.println("Category " + (i + 1) + ": ");
                    for (Category category : categories) {
                        System.out.println(j + "." + category.getName());
                        j++;
                    }
                    System.out.print("Selection: ");
                    selectedCategory = scanner.nextInt();
                    log.info("User selected category {} for ingredient {}: {}", selectedCategory, i + 1, name);

                    if (selectedCategory < 1 || selectedCategory > categories.size()) {
                        System.out.println("Invalid selection. Please choose a number between 1 and " + categories.size() + ".");
                        log.warn("Invalid category selection: {}. Must be between 1 and {}", selectedCategory, categories.size());
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    log.error("InputMismatchException caught: Invalid input for category selection.", e);
                    System.out.println("Invalid input. Please enter a valid number for the category.");
                    scanner.nextLine();
                }
            }
            scanner.nextLine();

            BigDecimal kcal = null;
            while (true) {
                try {
                    System.out.print("kcal " + (i + 1) + ": ");
                    kcal = scanner.nextBigDecimal();
                    log.info("User input for kcal {} for ingredient {}: {}", i + 1, name, kcal);
                    break;
                } catch (InputMismatchException e) {
                    log.error("InputMismatchException caught: Invalid input for kcal.", e);
                    System.out.println("Invalid input. Please enter a valid decimal number for kcal.");
                    scanner.nextLine();
                }
            }
            scanner.nextLine();

            System.out.print("Preparation method " + (i + 1) + ": ");
            String preparationMethod = scanner.nextLine();
            log.info("User input for preparation method {} for ingredient {}: {}", i + 1, name, preparationMethod);

            ingredients.add(new Ingredient(name, categories.get(selectedCategory - 1), kcal, preparationMethod));
            log.info("Ingredient created with name: {}, category: {}, kcal: {}, preparation method: {}",
                    name, categories.get(selectedCategory - 1).getName(), kcal, preparationMethod);
        }

        log.info("Method ingredientInput execution completed.");
    }

    private static void categoryInput(Scanner scanner, List<Category> categories) {
        log.info("Method categoryInput called.");

        for (int i = 0; i < 3; i++) {
            String name = "";
            boolean validName = false;

            while (!validName) {
                System.out.print("Name " + (i + 1) + ": ");
                name = scanner.nextLine();
                log.info("User input for category name {}: {}", i + 1, name);

                try {
                    for (Category category : categories) {
                        if (category != null && category.getName().equalsIgnoreCase(name)) {
                            String errorMessage = "The category name \"" + name + "\" already exists. Please enter a unique name.";
                            log.warn(errorMessage);
                            throw new DuplicateInputException(errorMessage);
                        }
                    }
                    validName = true;
                } catch (DuplicateInputException e) {
                    log.error("DuplicateInputException caught: {}", e.getMessage());
                    System.out.println(e.getMessage());
                }
            }

            System.out.print("Description " + (i + 1) + ": ");
            String description = scanner.nextLine();
            log.info("User input for category description {}: {}", i + 1, description);

            categories.add(new Category.Builder()

                    .name(name)
                    .description(description)
                    .build());
            log.info("Category created with name: {} and description: {}", name, description);
        }

        log.info("Method categoryInput execution completed.");
    }

}