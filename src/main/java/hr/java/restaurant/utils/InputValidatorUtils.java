package hr.java.restaurant.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

import hr.java.production.main.Main;
import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.exception.DuplicateInputException;
import hr.java.restaurant.model.Bonus;
import hr.java.restaurant.model.Chef;
import hr.java.restaurant.model.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InputValidatorUtils {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static <T extends Number & Comparable<T>> T validatePositiveNumber(
            Scanner scanner,
            String message,
            String logMessage,
            String errorMessage,
            Function<String, T> parseFunction,
            T zeroValue) {
        T positiveNumber = null;
        while (true) {
            try {
                System.out.print(message);
                String input = scanner.nextLine().trim();
                positiveNumber = parseFunction.apply(input); // Parse the input using the provided function
                log.info(logMessage, positiveNumber);

                if (positiveNumber.compareTo(zeroValue) > 0) { // Ensure the number is positive
                    break;
                }
                System.out.println(errorMessage);
            } catch (NumberFormatException | InputMismatchException e) {
                log.error("Invalid input: {}", e.getMessage());
                System.out.println(errorMessage);
            }
        }
        return positiveNumber;
    }

    public static String validateString(Scanner scanner, String message) {
        System.out.print(message);
        String text = scanner.nextLine();
        log.info("User input for category description: {}",text);
        return text;
    }

    public static <T> String validateStringDuplicate(
            Scanner scanner, String message, List<T> objects, Function<T, String> displayFunction) {
        if (objects == null) {
            throw new IllegalArgumentException(Messages.NULL_OBJECTS_LIST_ERROR);
        }

        String name = "";
        boolean validName = false;

        while (!validName) {
            System.out.print(message);
            name = scanner.nextLine().trim();
            log.info("User input for category name: {}", name);

            try {
                String finalName = name;
                boolean isDuplicate = objects.stream()
                        .map(displayFunction)
                        .anyMatch(existingName -> existingName.equalsIgnoreCase(finalName));

                if (isDuplicate) {
                    String errorMessage = "Name \"" + name + "\" already exists. Please enter a unique name.";
                    log.warn(errorMessage);
                    throw new DuplicateInputException(errorMessage);
                }

                validName = true;
            } catch (DuplicateInputException e) {
                log.error("DuplicateInputException caught: {}", e.getMessage());
                System.out.println(e.getMessage());
            }
        }
        return name;
    }

    public static <T> int validateObjectSelection(
            Scanner scanner, String message, List<T> objects, Function<T, String> displayFunction) {

        if (objects == null || objects.isEmpty()) {
            throw new IllegalArgumentException(Messages.EMPTY_LIST_ERROR);
        }

        int selectedObject = -1;
        while (true) {
            try {
                System.out.println(message);
                for (int i = 0; i < objects.size(); i++) {
                    System.out.println((i + 1) + ". " + displayFunction.apply(objects.get(i)));
                }
                System.out.print("Selection: ");
                selectedObject = scanner.nextInt();

                if (selectedObject < 1 || selectedObject > objects.size()) {
                    System.out.println("Invalid selection. Please choose a number between 1 and " + objects.size() + ".");
                    log.warn("Invalid category selection: {}. Must be between 1 and {}", selectedObject, objects.size());
                    continue;
                }
                log.info("User selected {}", objects.getClass().getSimpleName());
                break;
            } catch (InputMismatchException e) {
                log.error("InputMismatchException caught: Invalid input for {} selection.",
                        objects.getClass().getSimpleName(), e);
                System.out.println("Invalid input. Please enter a valid number for the category.");
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        return selectedObject;
    }

    public static <T> Set<T> validateMultipleObjectSelectionSet(
            Scanner scanner, String message, List<T> objects,  Function<T, String> displayFunction) {
        if (objects == null || objects.isEmpty()) {
            throw new IllegalArgumentException(Messages.EMPTY_LIST_ERROR);
        }

        boolean end = false;
        Set<T> selectionSet = new HashSet<>();

        while (!end) {
            int selectedIndex = -1;
            while (true) {
                try {
                    System.out.println(message);
                    for (int i = 0; i < objects.size(); i++) {
                        System.out.println((i + 1) + ". " + displayFunction.apply(objects.get(i)));
                    }
                    System.out.println("0. Finish");
                    System.out.print("Your choice: ");
                    selectedIndex = scanner.nextInt();

                    log.info("User selected {} for meal", selectedIndex);

                    if (selectedIndex < 0 || selectedIndex > objects.size()) {
                        log.warn("Invalid selection: {}. Must be between 0 and {}", selectedIndex, objects.size());
                        System.out.println("Invalid selection. Please choose a valid number or 0 to finish.");
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    log.error("InputMismatchException caught: Invalid input for selection.", e);
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine();
                }
            }
            scanner.nextLine();

            if (selectedIndex == 0) {
                end = true;
            } else {
                T selected = objects.get(selectedIndex - 1);
                if (!selectionSet.add(selected)) {
                    System.out.println("Item \"" + displayFunction.apply(selected) + "\" is already added.");
                } else {
                    log.info("Item added to meal: {}", displayFunction.apply(selected));
                }
            }
        }
        return selectionSet;
    }

    public static <T> List<T> validateMultipleObjectSelectionList(
            Scanner scanner, String message, List<T> objects,  Function<T, String> displayFunction) {
        if (objects == null || objects.isEmpty()) {
            throw new IllegalArgumentException(Messages.EMPTY_LIST_ERROR);
        }

        boolean end = false;
        List<T> selectionList = new ArrayList<>();

        while (!end) {
            int selectedIndex = -1;
            while (true) {
                try {
                    System.out.println(message);
                    for (int i = 0; i < objects.size(); i++) {
                        System.out.println((i + 1) + ". " + displayFunction.apply(objects.get(i)));
                    }
                    System.out.println("0. Finish");
                    System.out.print("Your choice: ");
                    selectedIndex = scanner.nextInt();

                    log.info("User selected {} for meal", selectedIndex);

                    if (selectedIndex < 0 || selectedIndex > objects.size()) {
                        log.warn("Invalid selection: {}. Must be between 0 and {}", selectedIndex, objects.size());
                        System.out.println("Invalid selection. Please choose a valid number or 0 to finish.");
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    log.error("InputMismatchException caught: Invalid input for selection.", e);
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine();
                }
            }
            scanner.nextLine();

            if (selectedIndex == 0) {
                end = true;
            } else {
                T selected = objects.get(selectedIndex - 1);
                if (!selectionList.add(selected)) {
                    System.out.println("Item \"" + displayFunction.apply(selected) + "\" is already added.");
                } else {
                    log.info("Item added to meal: {}", displayFunction.apply(selected));
                }
            }
        }
        return selectionList;
    }

    public static LocalDate validateLocalDate(Scanner scanner, String message) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        System.out.print(message + " (dd-MM-yyyy): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine(), dateFormatter);
        log.info("User input for date: {}",startDate);
        return startDate;
    }

    public static ContractType getContractType(Scanner scanner) {
        ContractType contractType = null;
        while (true) {
            try {
                System.out.print("Contract type (FULL_TIME/PART_TIME): ");
                String input = scanner.nextLine().trim().toUpperCase();
                contractType = ContractType.valueOf(input.toUpperCase());
                log.info("User input for contract type of chef: {}", contractType);
                break;
            }catch (IllegalArgumentException e){
                log.error("Invalid contract type input: {}. Valid options are FULL_TIME or PART_TIME.", e.getMessage());
                System.out.println("Invalid input. Please enter a valid contract type (FULL_TIME or PART_TIME).");
            }
        }
        return contractType;
    }

    public static <T> void employeeInput(Scanner scanner, List<T> objects) {
        log.info("Method chefInput called.");

        for (int i = 0; i < 3; i++) {
            System.out.println("Chef " + (i + 1) + ".");

            // Validate first and last name
            String[] fullNameTmp = DataInputUtils.validateFirstAndLastName(
                    scanner, objects, (object, fullName) -> {
                        try {
                            // Use reflection to get the first and last name dynamically
                            Method getFirstName = object.getClass().getMethod("getFirstName");
                            Method getLastName = object.getClass().getMethod("getLastName");
                            String objectFullName = getFirstName.invoke(object) + " " + getLastName.invoke(object);
                            return objectFullName.equalsIgnoreCase(fullName);
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException("Error checking duplicate names: " + e.getMessage(), e);
                        }
                    });

            // Get contract details
            Contract contract = DataInputUtils.getContract(scanner);

            // Get bonus details
            Bonus bonus = new Bonus(InputValidatorUtils.validatePositiveNumber(
                    scanner,
                    "Bonus: ",
                    "User input for waiters bonus {}: {}",
                    Messages.INVALID_BIGDECIMAL_INPUT_AND_NEGATIVE_ERROR,
                    BigDecimal::new,
                    BigDecimal.ZERO
            ));

            // Add the Chef object to the list
            T object = (T) new Chef.Builder()
                    .firstName(fullNameTmp[0])
                    .lastName(fullNameTmp[1])
                    .contract(contract)
                    .bonus(bonus)
                    .build();

            objects.add(object);

            log.info("Chef created and added: {}", object);
        }

        log.info("Method chefInput execution completed.");
    }


}
