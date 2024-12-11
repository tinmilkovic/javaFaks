package hr.java.production.main;

import hr.java.restaurant.generics.RestaurantLabourExchangeOffice;
import hr.java.restaurant.model.*;
import hr.java.restaurant.sort.EmployeeContractDurationComparator;
import hr.java.restaurant.sort.EmployeeSalaryComparator;
import hr.java.restaurant.utils.DataInputUtils;
import hr.java.restaurant.utils.InputValidatorUtils;
import hr.java.restaurant.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        log.info("The application is started...");

        Scanner scanner = new Scanner(System.in);

        List<Category> categories = new ArrayList<>();
        categoryInput(scanner, categories);

        List<Ingredient> ingredients = new ArrayList<>();
        ingredientInput(scanner, categories, ingredients);

        List<Meal> meals = new ArrayList<>();
        mealInput(scanner, categories, ingredients, meals);

        List<Chef> chefs = new ArrayList<>();
        DataInputUtils.employeeInput(scanner, chefs, Chef.class);

        List<Waiter> waiters = new ArrayList<>();
        DataInputUtils.employeeInput(scanner, waiters, Waiter.class);

        List<Deliverer> deliverers = new ArrayList<>();
        DataInputUtils.employeeInput(scanner, deliverers, Deliverer.class);

        List<Restaurant> restaurants = new ArrayList<>();
        restaurantInput(scanner, meals, chefs, waiters, deliverers, restaurants);
        RestaurantLabourExchangeOffice<Restaurant> restaurantLabourExchangeOffice =
                new RestaurantLabourExchangeOffice<>(restaurants);

        List<Order> orders = new ArrayList<>();
        orderInput(restaurantLabourExchangeOffice, scanner, meals, deliverers, orders);

        List<VeganMeal> veganMeals = new ArrayList<>();
        veganMealInput(scanner, categories, ingredients, veganMeals);

        List<VegeterianMeal> vegeterianMeals = new ArrayList<>();
        vegeterianMealInput(scanner, categories, ingredients, vegeterianMeals);

        List<MeatMeal> meatMeals = new ArrayList<>();
        meatMealInput(scanner, categories, ingredients, meatMeals);


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
                        .mapToDouble(meal -> meal.getPrice().doubleValue())
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
            System.out.println("Meat meal " + (i + 1) + ".");

            String name = InputValidatorUtils.validateStringDuplicate(
                    scanner, "Name: ", meatMeals, MeatMeal::getName);

            int selectedCategory = InputValidatorUtils.validateObjectSelection(
                    scanner, "Category: ", categories, Category::getName);

            boolean end = false;
            Set<Ingredient> ingredientsSet = InputValidatorUtils.validateMultipleObjectSelectionSet(scanner, "Ingredient: ", ingredients, Ingredient::getName);

            BigDecimal price = InputValidatorUtils.validatePositiveNumber(scanner, "price: ",
                    "User input for price for ingredient {}: {}",
                    Messages.INVALID_BIGDECIMAL_INPUT_AND_NEGATIVE_ERROR,
                    BigDecimal::new,
                    BigDecimal.ZERO);

            String meatType = InputValidatorUtils.validateString(scanner, "Meat type: ");

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
            System.out.println("Vegetarian meal " + (i + 1) + ".");

            String name = InputValidatorUtils.validateStringDuplicate(
                    scanner, "Name: ", vegeterianMeals, VegeterianMeal::getName);

            int selectedCategory = InputValidatorUtils.validateObjectSelection(
                    scanner, "Category: ", categories, Category::getName);

            Set<Ingredient> ingredientsSet = InputValidatorUtils.validateMultipleObjectSelectionSet(scanner, "Ingredient: ", ingredients, Ingredient::getName);

            BigDecimal price = InputValidatorUtils.validatePositiveNumber(scanner, "price: ",
                    "User input for price for ingredient {}: {}",
                    Messages.INVALID_BIGDECIMAL_INPUT_AND_NEGATIVE_ERROR,
                    BigDecimal::new,
                    BigDecimal.ZERO);

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

            vegeterianMeals.add(new VegeterianMeal(name, categories.get(selectedCategory - 1), ingredientsSet, price, eggs));
            log.info("Vegeterian Meal {} created with name: {}, category: {}, price: {}, eggs: {}",
                    i + 1, name, categories.get(selectedCategory - 1).getName(), price, eggs);
        }

        log.info("Method vegeterianMealInput completed.");
    }

    private static void veganMealInput(Scanner scanner, List<Category> categories, List<Ingredient> ingredients, List<VeganMeal> veganMeals) {
        log.info("Method veganMealInput called.");

        for (int i = 0; i < 3; i++) {
            System.out.println("Vegan meal " + (i + 1) + ".");

            // Name input with validation
            String name = InputValidatorUtils.validateStringDuplicate(
                    scanner, "Name: ", veganMeals, VeganMeal::getName);

            int selectedCategory = InputValidatorUtils.validateObjectSelection(
                    scanner, "Category: ", categories, Category::getName);

            Set<Ingredient> ingredientsSet = InputValidatorUtils.validateMultipleObjectSelectionSet(scanner, "Ingredient: ", ingredients, Ingredient::getName);

            // Price input with validation
            BigDecimal price = InputValidatorUtils.validatePositiveNumber(scanner, "price: ",
                    "User input for price for ingredient {}: {}",
                    Messages.INVALID_BIGDECIMAL_INPUT_AND_NEGATIVE_ERROR,
                    BigDecimal::new,
                    BigDecimal.ZERO);

            String portionSize = InputValidatorUtils.validateString(scanner, "Portion size: ");

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
            System.out.println("Order " + (i + 1) + ".");

            List<Restaurant> restaurants = restaurantsGen.getRestaurants();

            int selectedRestaurant = InputValidatorUtils.validateObjectSelection(
                    scanner, "Restaurants:" ,restaurants, Restaurant::getName);

            List<Meal> mealsList = InputValidatorUtils.validateMultipleObjectSelectionList(
                    scanner, "Meals", meals, Meal::getName);

            int selectedDeliverer = InputValidatorUtils.validateObjectSelection(
                    scanner, "Deliverer", deliverers, Deliverer::getFirstAndLastName);

            orders.add(new Order(restaurants.get(selectedRestaurant - 1), mealsList, deliverers.get(selectedDeliverer - 1), LocalDateTime.now()));
            log.info("Order {} created for Restaurant: {}, Deliverer: {}, Meals: {}", i + 1,
                    restaurants.get(selectedRestaurant - 1).getName(), deliverers.get(selectedDeliverer - 1).getFirstName());
        }

        log.info("Method orderInput completed.");
    }

    private static void restaurantInput(Scanner scanner, List<Meal> meals, List<Chef> chefs, List<Waiter> waiters, List<Deliverer> deliverers, List<Restaurant> restaurants) {
        log.info("Method restaurantInput called.");

        for (int i = 0; i < 3; i++) {
            System.out.println("Restaurant " + (i + 1) + ".");
            String name = InputValidatorUtils.validateStringDuplicate(
                    scanner, "Name: ", restaurants, Restaurant::getName);

            Address address = DataInputUtils.getAddress(scanner);

            Set<Meal> mealsSet = InputValidatorUtils.validateMultipleObjectSelectionSet(
                    scanner, "Meal:", meals, Meal::getName);

            Set<Chef> chefsSet = InputValidatorUtils.validateMultipleObjectSelectionSet(
                    scanner, "Chef:", chefs, Chef::getFirstAndLastName);

            Set<Waiter> waitersSet = InputValidatorUtils.validateMultipleObjectSelectionSet(
                    scanner, "Waiter:", waiters, Waiter::getFirstAndLastName);

            Set<Deliverer> deliverersSet = InputValidatorUtils.validateMultipleObjectSelectionSet(
                    scanner, "Deliverer:", deliverers, Deliverer::getFirstAndLastName);

            restaurants.add(new Restaurant(name,address, mealsSet, chefsSet, waitersSet, deliverersSet));
            //log.info("Restaurant created with name: {}, street: {}, house number: {}, city: {}, postal code: {}, meals: {}, chefs: {}, waiters: {}, deliverers: {}",name, street, houseNumber, city, postalCode, mealsSet.size(), chefsSet.size(), waitersSet.size(), deliverersSet.size());
        }

        log.info("Method restorantInput execution completed.");
    }

    private static void mealInput(Scanner scanner, List<Category> categories, List<Ingredient> ingredients, List<Meal> meals) {
        log.info("Method mealInput called.");

        for (int i = 0; i < 3; i++) {
            System.out.println("Meal " + (i + 1) + ".");
            String name = InputValidatorUtils.validateStringDuplicate(
                    scanner, "Name: ", meals, Meal::getName);

            int selectedCategory = InputValidatorUtils.validateObjectSelection(
                    scanner, "Category: ", categories, Category::getName);

            Set<Ingredient> ingredientSet = InputValidatorUtils.validateMultipleObjectSelectionSet(
                    scanner, "Ingredients: ", ingredients, Ingredient::getName);

            BigDecimal price = InputValidatorUtils.validatePositiveNumber(scanner, "price: ",
                    "User input for price for ingredient {}: {}",
                    Messages.INVALID_BIGDECIMAL_INPUT_AND_NEGATIVE_ERROR,
                    BigDecimal::new,
                    BigDecimal.ZERO);

            meals.add(new Meal(name, categories.get(selectedCategory - 1), ingredientSet, price));
            log.info("Meal created with name: {}, category: {}, ingredients count: {}, price: {}",
                    name, categories.get(selectedCategory - 1).getName(), ingredientSet.size(), price);
        }

        log.info("Method mealInput execution completed.");
    }

    private static void ingredientInput(Scanner scanner, List<Category> categories, List<Ingredient> ingredients) {
        log.info("Method ingredientInput called.");

        for (int i = 0; i < 5; i++) {
            System.out.println("Ingredient " + (i + 1) + ".");
            String name = InputValidatorUtils.validateStringDuplicate(
                    scanner, "Name: ", ingredients, Ingredient::getName);

            int selectedCategory = InputValidatorUtils.validateObjectSelection(
                    scanner, "Category: ", categories, Category::getName);

            BigDecimal kcal = InputValidatorUtils.validatePositiveNumber(scanner, "kcal: ",
                    "User input for kcal for ingredient {}: {}",
                    Messages.INVALID_BIGDECIMAL_INPUT_AND_NEGATIVE_ERROR,
                    BigDecimal::new,
                    BigDecimal.ZERO);

            String preparationMethod = InputValidatorUtils.validateString(scanner, "Preparation method: ");

            ingredients.add(new Ingredient(name, categories.get(selectedCategory - 1), kcal, preparationMethod));
            log.info("Ingredient created with name: {}, category: {}, kcal: {}, preparation method: {}",
                    name, categories.get(selectedCategory - 1).getName(), kcal, preparationMethod);
        }
        log.info("Method ingredientInput execution completed.");
    }

    private static void categoryInput(Scanner scanner, List<Category> categories) {
        log.info("Method categoryInput called.");

        for (int i = 0; i < 3; i++) {
            System.out.println("Category " + (i + 1) + ".");
            String name = InputValidatorUtils.validateStringDuplicate(
                    scanner, "Name: ", categories, Category::getName);

            String description = InputValidatorUtils.validateString(scanner, "Description: ");

            categories.add(new Category.Builder()
                    .name(name)
                    .description(description)
                    .build());
            log.info("Category created with name: {} and description: {}", name, description);
        }
        log.info("Method categoryInput execution completed.");
    }
}