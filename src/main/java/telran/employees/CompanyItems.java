package telran.employees;

import telran.view.*;
import java.util.*;
import java.util.function.*;

public class CompanyItems {
    public static final long MIN_ID = 1;
    public static final long MAX_ID = 9999;
    public static final int MIN_BASIC_SALARY = 1000;
    public static final int MAX_BASIC_SALARY = 100000;
    public static final String[] DEPARTMENTS = { "QA", "Development", "Audit", "Marketing" };
    public static final int MIN_WAGE = 1;
    public static final int MAX_WAGE = 100;
    public static final int MIN_HOURS = 1;
    public static final int MAX_HOURS = 160;
    public static final float MIN_PERCENT = 1;
    public static final float MAX_PERCENT = 100;
    public static final long MIN_SALES = 1000;
    public static final long MAX_SALES = 1000000;
    public static final float MIN_FACTOR = 1.0f;
    public static final float MAX_FACTOR = 10.0f;

    public static Item[] getItems(Company company) {
        List<Item> items = new ArrayList<>();

        items.add(Item.of("Add Employee", io -> {
            Item[] hireMenu = getHireMenu(company);
            int choice;
            do {
                io.writeLine("Select an option:");
                for (int i = 0; i < hireMenu.length; i++) {
                    io.writeLine((i + 1) + ". " + hireMenu[i].displayName());
                }
                io.writeLine("0. Go back");
    
                choice = validator(io, "Enter your choice:", "Invalid choice", 0, hireMenu.length, Integer::parseInt);
    
                if (choice > 0 && choice <= hireMenu.length) {
                    hireMenu[choice - 1].perform(io); 
                }
            } while (choice != 0); 
        }));
    

        items.add(Item.of("Display Employee Data", io -> {
            long id = io.readLong("Enter employee ID", "ID not found");
            Employee employee = company.getEmployee(id);
            if (employee != null) {
                io.writeLine(employee.toString());
            } else {
                io.writeLine("Employee not found.");
            }
        }));

        items.add(Item.of("Fire Employee", io -> {
            long id = io.readLong("Enter employee ID to fire", "ID not found");
            company.removeEmployee(id);
            io.writeLine("Employee " + id + " fired.");
        }));

        items.add(Item.of("Department Salary Budget", io -> {
            String department = io.readString("Enter department");
            double budget = company.getDepartmentBudget(department);
            io.writeLine("Salary budget for department " + department + ": " + budget);
        }));

        items.add(Item.of("List of Departments", io -> {
            String[] departments = company.getDepartments();
            io.writeLine("Departments: " + String.join(", ", departments));
        }));

        items.add(Item.of("Display Managers with Most Factor", io -> {
            Manager[] managers = company.getManagersWithMostFactor();
            for (Manager manager : managers) {
                io.writeLine("Manager with ID " + manager.getId() +" ,factor: " + manager.getFactor());
            }
        }));

        items.add(Item.ofExit());

        return items.toArray(new Item[0]);
    }

    private static Item[] getHireMenu(Company company) {
        List<Item> items = new ArrayList<>();
            items.add(Item.of("Hire Employee", io -> {
                    long id = validator(io, "Enter ID:", "Wrong ID", MIN_ID, MAX_ID, Long::parseLong);
                    int basicSalary = validator(io, "Enter Salary:", "Wrong value", MIN_BASIC_SALARY, MAX_BASIC_SALARY,
                            Integer::parseInt);
                    String department = departmentValidator(io, "Enter Department:", "Wrong department", DEPARTMENTS);
                    company.addEmployee(new Employee(id, basicSalary, department));
                    io.writeLine("Employee hired.");
                }));

                items.add(Item.of("Hire Wage Employee", io -> {
                    long id = validator(io, "Enter ID:", "Wrong ID", MIN_ID, MAX_ID, Long::parseLong);
                    int basicSalary = validator(io, "Enter Salary:", "Wrong value", MIN_BASIC_SALARY, MAX_BASIC_SALARY,
                            Integer::parseInt);
                    int wage = validator(io, "Enter Wage:", "Wrong Wage", MIN_WAGE, MAX_WAGE, Integer::parseInt);
                    int hours = validator(io, "Enter Hours:", "Wrong Hours", MIN_HOURS, MAX_HOURS, Integer::parseInt);
                    String department = departmentValidator(io, "Enter Department:", "Wrong department", DEPARTMENTS);
                    company.addEmployee(new WageEmployee(id, basicSalary, department, wage, hours));
                    io.writeLine("Wage employee hired.");
                }));

                items.add(Item.of("Hire Sales Person", io -> {
                    long id = validator(io, "Enter ID:", "Wrong ID", MIN_ID, MAX_ID, Long::parseLong);
                    int basicSalary = validator(io, "Enter Salary:", "Wrong value", MIN_BASIC_SALARY, MAX_BASIC_SALARY,
                            Integer::parseInt);
                    long sales = validator(io, "Enter Sales:", "Wrong Sales", MIN_SALES, MAX_SALES, Long::parseLong);
                    float percent = validator(io, "Enter Percent:", "Wrong Percent", MIN_PERCENT, MAX_PERCENT,
                            Float::parseFloat);
                    String department = departmentValidator(io, "Enter Department:", "Wrong department", DEPARTMENTS);
                    int wage = validator(io, "Enter Wage:", "Wrong Wage", MIN_WAGE, MAX_WAGE, Integer::parseInt);
                    int hours = validator(io, "Enter Hours:", "Wrong Hours", MIN_HOURS, MAX_HOURS, Integer::parseInt);
                    company.addEmployee(new SalesPerson(id, basicSalary, department, wage, hours, percent, sales));
                    io.writeLine("Sales person hired.");
                }));

                items.add(Item.of("Hire Manager", io -> {
                    long id = validator(io, "Enter ID:", "Wrong ID", MIN_ID, MAX_ID, Long::parseLong);
                    int basicSalary = validator(io, "Enter Salary:", "Wrong value", MIN_BASIC_SALARY, MAX_BASIC_SALARY,
                            Integer::parseInt);
                    String department = departmentValidator(io, "Enter Department:", "Wrong department", DEPARTMENTS);
                    float factor = validator(io, "Enter Factor:", "Wrong Factor", MIN_FACTOR, MAX_FACTOR,
                            Float::parseFloat);
                    company.addEmployee(new Manager(id, basicSalary, department, factor));
                    io.writeLine("Manager hired.");
                }));

        return items.toArray(new Item[0]);
    }

    private static <T extends Number & Comparable<T>> T validator(
            InputOutput io, String prompt, String errorPrompt, T min, T max, Function<String, T> parseFunction) {

        T value;
        do {
            value = io.readObject(prompt, errorPrompt, parseFunction);
            if (value.compareTo(min) < 0 || value.compareTo(max) > 0) {
                io.writeLine("Value must be between " + min + " and " + max);
            }
        } while (value.compareTo(min) < 0 || value.compareTo(max) > 0);

        return value;
    }

    private static String departmentValidator(
            InputOutput io, String prompt, String errorPrompt, String[] DEPARTMENTS) {

        String department;
        boolean isValid;
        do {
            department = io.readString(prompt);
            isValid = Arrays.asList(DEPARTMENTS).contains(department);
            if (!isValid) {
                io.writeLine("Choose one: QA, Development, Audit, Marketing");
            }
        } while (!isValid);

        return department;
    }
}