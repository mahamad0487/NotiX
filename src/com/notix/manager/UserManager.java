package com.notix.manager;

import com.notix.model.Employee;
import com.notix.utils.JsonUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserManager {
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private List<Employee> employees;
    private String loggedInUser;
    private NotificationManager notificationManager;

    public UserManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
        this.employees = JsonUtil.loadEmployees();
        this.loggedInUser = null;
        notificationManager.initializeSubscribers(employees);
    }

    public boolean login(String email) {
        if (!isValidEmail(email)) {
            System.out.println("Format d'email invalide.");
            return false;
        }
        if (email.equals(ADMIN_EMAIL) || employees.stream().anyMatch(emp -> emp.getEmail().equals(email))) {
            loggedInUser = email;
            System.out.printf("Connecté en tant que %s\n", email);
            return true;
        }
        System.out.println("Utilisateur non trouvé.");
        return false;
    }

    public void logout() {
        loggedInUser = null;
    }

    public boolean isAdminLoggedIn() {
        return loggedInUser != null && loggedInUser.equals(ADMIN_EMAIL);
    }

    public boolean isEmployeeLoggedIn() {
        return loggedInUser != null && !loggedInUser.equals(ADMIN_EMAIL);
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public Employee addEmployee(String nomcomplet, String email) {
        if (!isValidEmail(email)) {
            System.out.println("Format d'email invalide.");
            return null;
        }
        if (employees.stream().anyMatch(emp -> emp.getEmail().equals(email))) {
            System.out.println("Cet email existe déjà.");
            return null;
        }
        Employee employee = new Employee(nomcomplet, email);
        employees.add(employee);
        JsonUtil.saveEmployees(employees);
        notificationManager.subscribe(employee, notificationManager.getServices().get(0)); // Abonnement par défaut à Console
        return employee;
    }

    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    public Employee getEmployeeByEmail(String email) {
        return employees.stream()
                .filter(emp -> emp.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public void saveEmployees() {
        JsonUtil.saveEmployees(employees);
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
}