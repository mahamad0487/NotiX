package com.notix.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.notix.model.Employee;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonUtil {
    private static final String FILE_PATH = "employees.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveEmployees(List<Employee> employees) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(employees, writer);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des employés : " + e.getMessage());
        }
    }

    public static List<Employee> loadEmployees() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Employee[] employees = gson.fromJson(reader, Employee[].class);
            return employees != null ? new ArrayList<>(Arrays.asList(employees)) : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Aucune donnée d'employé existante trouvée, démarrage avec une liste vide.");
            return new ArrayList<>();
        }
    }
}