package hr.java.restaurant.utils;

import hr.java.production.main.Main;
import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.exception.DuplicateInputException;
import hr.java.restaurant.exception.InvalidValueException;
import hr.java.restaurant.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;

public class DataInputUtils {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static BigDecimal getSalary(Scanner scanner) {
        boolean validSalary = false;
        BigDecimal salary = new BigDecimal(0);
        while (!validSalary) {
            try {
                System.out.print("Salary: ");
                salary = scanner.nextBigDecimal();
                log.info("User input for salary of chef {}: {}", salary);

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
        return salary;
    }

    public static <T> String[] validateFirstAndLastName(
            Scanner scanner, List<T> objects, BiFunction<T, String, Boolean> duplicateCheckFunction) {

        String firstName = "";
        String lastName = "";
        boolean validName = false;

        while (!validName) {
            System.out.print("First name: ");
            firstName = scanner.nextLine().trim();
            log.info("User input for first name: {}", firstName);

            System.out.print("Last name: ");
            lastName = scanner.nextLine().trim();
            log.info("User input for last name: {}", lastName);

            try {
                String finalFirstName = firstName;
                String finalLastName = lastName;
                boolean isDuplicate = objects.stream()
                        .anyMatch(obj -> duplicateCheckFunction.apply(obj, finalFirstName + " " + finalLastName));

                if (isDuplicate) {
                    String errorMessage = "A name with \"" + firstName + " " + lastName + "\" already exists. Please enter a unique name.";
                    log.warn(errorMessage);
                    throw new DuplicateInputException(errorMessage);
                }

                validName = true;
            } catch (DuplicateInputException e) {
                log.error("DuplicateInputException caught: {}", e.getMessage());
                System.out.println(e.getMessage());
            }
        }
        return new String[] { firstName, lastName };
    }

    public static Contract getContract(Scanner scanner){

        BigDecimal salary = getSalary(scanner);
        LocalDate startDate = InputValidatorUtils.validateLocalDate(scanner, "Start date");
        LocalDate endDate = InputValidatorUtils.validateLocalDate(scanner, "End date");
        ContractType contractType = InputValidatorUtils.getContractType(scanner);

        return new Contract(salary, startDate, endDate, contractType);
    }

    public static Address getAddress(Scanner scanner){

        String street = InputValidatorUtils.validateString(scanner, "Street: ");
        String houseNumber = InputValidatorUtils.validateString(scanner, "House number: ");
        String city = InputValidatorUtils.validateString(scanner, "City: ");
        String postalCode = InputValidatorUtils.validateString(scanner, "Postal code: ");

        return new Address.Builder()
                .street(street)
                .houseNumber(houseNumber)
                .city(city)
                .postalCode(postalCode)
                .build();
    }

    public static <T extends Person> void employeeInput(Scanner scanner, List<T> objects, Class<T> type) {
        log.info("Method employeeInput called.");

        for (int i = 0; i < 3; i++) {
            System.out.println("Employee " + (i + 1) + ".");

            // Validate first and last name
            String[] fullNameTmp = DataInputUtils.validateFirstAndLastName(
                    scanner, objects, (object, fullName) -> {
                        try {
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
                    "User input for employee bonus {}: {}",
                    Messages.INVALID_BIGDECIMAL_INPUT_AND_NEGATIVE_ERROR,
                    BigDecimal::new,
                    BigDecimal.ZERO
            ));

            // Dynamically build the employee object based on its class type
            T employee = createEmployee(type, fullNameTmp, contract, bonus);

            objects.add(employee);

            log.info("Employee created and added: {}", employee);
        }

        log.info("Method employeeInput execution completed.");
    }

    private static <T extends Person> T createEmployee(Class<T> type, String[] fullNameTmp, Contract contract, Bonus bonus) {
        try {
            // Use reflection to create the appropriate employee class (Chef, Waiter, Deliverer, etc.)
            if (type.equals(Chef.class)) {
                return (T) new Chef.Builder()
                        .firstName(fullNameTmp[0])
                        .lastName(fullNameTmp[1])
                        .contract(contract)
                        .bonus(bonus)
                        .build();
            } else if (type.equals(Waiter.class)) {
                return (T) new Waiter.Builder()
                        .firstName(fullNameTmp[0])
                        .lastName(fullNameTmp[1])
                        .contract(contract)
                        .bonus(bonus)
                        .build();
            } else if (type.equals(Deliverer.class)) {
                return (T) new Deliverer.Builder()
                        .firstName(fullNameTmp[0])
                        .lastName(fullNameTmp[1])
                        .contract(contract)
                        .bonus(bonus)
                        .build();
            } else {
                throw new IllegalArgumentException("Unsupported employee type: " + type);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating employee: " + e.getMessage(), e);
        }
    }



}
