package telran.employees;

import telran.view.*;
import java.util.*;

public class CompanyItems {
    static InputOutput io = new StandardInputOutput();

    public static final long MIN_ID = 1;
    public static final long MAX_ID = 9999;
    public static final int MIN_BASIC_SALARY = 1000;
    public static final int MAX_BASIC_SALARY = 100000;
    public static final String[] DEPARTMENTS = { "QA", "Development", "Audit", "Marketing" };
    public static final int MIN_WAGE = 1;
    public static final int MAX_WAGE = 100;
    public static final int MIN_HOURS = 1;
    public static final int MAX_HOURS = 160;
    public static final float MIN_PERCENT = 1.0f;
    public static final float MAX_PERCENT = 100.0f;
    public static final long MIN_SALES = 1000;
    public static final long MAX_SALES = 1000000;
    public static final float MIN_FACTOR = 1.0f;
    public static final float MAX_FACTOR = 10.0f;

    public static Item[] getItems(Company company) {
        List<Item> items = new ArrayList<>();
        new Menu("Add Employee", hireMenu(company)).perform(io);
        return items.toArray(new Item[0]);
    }

    private static Item[] hireMenu(Company company) {
        List<Item> items = new ArrayList<>();
        items.add(Item.of("Hire Employee", io -> {
            Employee employee = standartEmployee(io);

            company.addEmployee(new Employee(employee.getId(), employee.computeSalary(), employee.getDepartment()));
            io.writeLine("Employee hired.");
        }));

        items.add(Item.of("Hire Wage Employee", io -> {
            Employee employee = standartEmployee(io);

            int wage = io.readNumberRange(
                    String.format("Enter wage value in the range [%d-%d]", MIN_WAGE, MAX_WAGE),
                    "Wrong Salary value", MIN_WAGE, MAX_WAGE).intValue();
            int hours = io.readNumberRange(
                    String.format("Enter hours value in the range [%d-%d]", MIN_HOURS, MAX_HOURS),
                    "Wrong Salary value", MIN_HOURS, MAX_HOURS).intValue();
            company.addEmployee(new WageEmployee(employee.getId(), employee.computeSalary(), employee.getDepartment(),
                    wage, hours));
            io.writeLine("Wage employee hired.");
        }));

        items.add(Item.of("Hire Sales Person", io -> {
            Employee employee = standartEmployee(io);

            long sales = io.readNumberRange(
                    String.format("Enter sales value in the range [%d-%d]", MIN_SALES, MAX_SALES),
                    "Wrong sales value", MIN_SALES, MAX_SALES).longValue();
            float percent = io.readNumberRange(
                    String.format("Enter percent value in the range [%.2f-%.2f]", MIN_PERCENT, MAX_PERCENT),
                    "Wrong percent value", MIN_PERCENT, MAX_PERCENT).floatValue();
            int wage = io.readNumberRange(
                    String.format("Enter wage value in the range [%d-%d]", MIN_WAGE, MAX_WAGE),
                    "Wrong wage value", MIN_WAGE, MAX_WAGE).intValue();
            int hours = io.readNumberRange(
                    String.format("Enter hours value in the range [%d-%d]", MIN_HOURS, MAX_HOURS),
                    "Wrong hours value", MIN_HOURS, MAX_HOURS).intValue();
            company.addEmployee(new SalesPerson(employee.getId(), employee.computeSalary(), employee.getDepartment(),
                    wage, hours, percent, sales));
            io.writeLine("Sales person hired.");
        }));

        items.add(Item.of("Hire Manager", io -> {
            Employee employee = standartEmployee(io);

            float factor = io.readNumberRange(
                    String.format("Enter factor value in the range [%.2f-%.2f]", MIN_FACTOR, MAX_FACTOR),
                    "Wrong factor value", MIN_FACTOR, MAX_FACTOR).floatValue();
            company.addEmployee(
                    new Manager(employee.getId(), employee.computeSalary(), employee.getDepartment(), factor));
            io.writeLine("Manager hired.");
        }));

        items.add(Item.of("Display Employee Data", input -> {
            long id = input.readLong("Enter employee ID", "ID not found");
            Employee employee = company.getEmployee(id);
            if (employee != null) {
                input.writeLine(employee.toString());
            } else {
                input.writeLine("Employee not found.");
            }
        }));

        items.add(Item.of("Fire Employee", input -> {
            long id = input.readLong("Enter employee ID to fire", "ID not found");
            company.removeEmployee(id);
            input.writeLine("Employee " + id + " fired.");
        }));

        items.add(Item.of("Department Salary Budget", input -> {
            String department = input.readString("Enter department");
            double budget = company.getDepartmentBudget(department);
            input.writeLine("Salary budget for department " + department + ": " + budget);
        }));

        items.add(Item.of("List of Departments", input -> {
            String[] departments = company.getDepartments();
            input.writeLine("Departments: " + String.join(", ", departments));
        }));

        items.add(Item.of("Display Managers with Most Factor", input -> {
            Manager[] managers = company.getManagersWithMostFactor();
            for (Manager manager : managers) {
                input.writeLine("Manager with ID " + manager.getId() + " ,factor: " + manager.getFactor());
            }
        }));

        items.add(Item.of("Exit", io -> {
            io.writeLine("Exiting program...");
            System.exit(0);
        }));

        return items.toArray(new Item[0]);
    }

    private static Employee standartEmployee(InputOutput io) {
        HashSet<String> departmentsSet = new HashSet<>(List.of(DEPARTMENTS));

        long id = io.readNumberRange(String.format("Enter ID value in the range [%d-%d]", MIN_ID, MAX_ID),
                "Wrong ID value", MIN_ID, MAX_ID).longValue();
        int basicSalary = io.readNumberRange(
                String.format("Enter Salary value in the range [%d-%d]", MIN_BASIC_SALARY, MAX_BASIC_SALARY),
                "Wrong Salary value", MIN_BASIC_SALARY, MAX_BASIC_SALARY).intValue();

        String department = io.readStringOptions("Enter department from " + departmentsSet,
                "Must be one out from " + departmentsSet,
                departmentsSet);

        return new Employee(id, basicSalary, department);
    }
}