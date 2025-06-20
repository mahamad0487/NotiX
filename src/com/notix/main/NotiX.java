package com.notix.main;

import com.notix.manager.NotificationManager;
import com.notix.manager.UserManager;
import com.notix.model.Employee;
import com.notix.model.Notification;
import com.notix.service.NotificationService;
import com.notix.service.Subscriber;
import java.util.List;
import java.util.Scanner;

public class NotiX {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        NotificationManager notificationManager = new NotificationManager();
        UserManager userManager = new UserManager(notificationManager);

        while (true) {
            System.out.println("\n=== Système de Notification NotiX ===");
            System.out.println("1. Se connecter");
            System.out.println("2. Quitter");
            System.out.print("Choix : ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Entrez votre email pour vous connecter : ");
                    String email = scanner.nextLine();
                    if (!userManager.login(email)) {
                        continue;
                    }
                    break;
                case 2:
                    System.out.println("Au revoir !");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Choix invalide.");
                    continue;
            }

            while (userManager.getLoggedInUser() != null) {
                if (userManager.isAdminLoggedIn()) {
                    displayAdminMenu();
                    handleAdminActions(scanner, userManager, notificationManager);
                } else {
                    displayEmployeeMenu();
                    handleEmployeeActions(scanner, userManager, notificationManager);
                }
            }
        }
    }

    private static void displayAdminMenu() {
        System.out.println("\nMenu Admin :");
        System.out.println("1. Lister les employés");
        System.out.println("2. Ajouter un employé");
        System.out.println("3. Envoyer une notification pour un employé");
        System.out.println("4. Voir les notifications d'un employé");
        System.out.println("5. Vérifier les services d'abonnement d'un employé");
        System.out.println("6. Déconnexion");
        System.out.print("Choix : ");
    }

    private static void displayEmployeeMenu() {
        System.out.println("\nMenu Employé :");
        System.out.println("1. S'abonner à un service");
        System.out.println("2. Se désabonner d'un service");
        System.out.println("3. Voir mes notifications");
        System.out.println("4. Lister les employés par services abonnés");
        System.out.println("5. Déconnexion");
        System.out.print("Choix : ");
    }

    private static void handleAdminActions(Scanner scanner, UserManager userManager, NotificationManager notificationManager) {
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            scanner.nextLine();
            return;
        }

        switch (choice) {
            case 1:
                System.out.println("\nEmployés :");
                for (Employee emp : userManager.getEmployees()) {
                    System.out.printf("- %s (%s, %s)\n", emp.getName(), emp.getEmail(), emp.getMatricule());
                }
                break;

            case 2:
                System.out.print("Entrez le nom complet de l'employé : ");
                String name = scanner.nextLine();
                System.out.print("Entrez l'email de l'employé : ");
                String email = scanner.nextLine();
                userManager.addEmployee(name, email);
                break;

            case 3:
                System.out.print("Entrez l'email de l'employé : ");
                String empEmail = scanner.nextLine();
                Employee emp = userManager.getEmployeeByEmail(empEmail);
                if (emp == null) {
                    System.out.println("Employé non trouvé.");
                    break;
                }
                List<NotificationService> subscribedServices = notificationManager.getSubscribedServicesForEmployee(emp);
                if (!subscribedServices.isEmpty()) {
                    System.out.println("Services disponibles pour " + emp.getName() + " :");
                    for (NotificationService service : subscribedServices) {
                        System.out.printf("- %s\n", service.getServiceName());
                    }
                    System.out.print("Entrez le message : ");
                    String message = scanner.nextLine();
                    notificationManager.notifySubscribers(message, emp.getEmail());
                    userManager.saveEmployees();
                } else {
                    System.out.println("L'employé n'a aucun abonnement actif.");
                }
                break;

            case 4:
                System.out.print("Entrez l'email de l'employé : ");
                String notifEmail = scanner.nextLine();
                Employee notifEmp = userManager.getEmployeeByEmail(notifEmail);
                if (notifEmp == null) {
                    System.out.println("Employé non trouvé.");
                    break;
                }
                System.out.printf("\nNotifications pour %s :\n", notifEmp.getName());
                for (Notification notif : notifEmp.getNotifications()) {
                    System.out.println(notif.toGmailFormat());
                }
                break;

            case 5:
                System.out.print("Entrez l'email de l'employé : ");
                String checkEmail = scanner.nextLine();
                Employee checkEmp = userManager.getEmployeeByEmail(checkEmail);
                if (checkEmp == null) {
                    System.out.println("Employé non trouvé.");
                    break;
                }
                List<NotificationService> services = notificationManager.getSubscribedServicesForEmployee(checkEmp);
                if (services.isEmpty()) {
                    System.out.println("L'employé n'est abonné à aucun service.");
                } else {
                    System.out.printf("%s est abonné à :\n", checkEmp.getName());
                    for (NotificationService service : services) {
                        System.out.printf("- %s\n", service.getServiceName());
                    }
                }
                break;

            case 6:
                userManager.logout();
                System.out.println("Déconnecté.");
                break;

            default:
                System.out.println("Choix invalide.");
        }
    }

    private static void handleEmployeeActions(Scanner scanner, UserManager userManager, NotificationManager notificationManager) {
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            scanner.nextLine();
            return;
        }

        Employee emp = userManager.getEmployeeByEmail(userManager.getLoggedInUser());
        if (emp == null) {
            System.out.println("Erreur : Employé non trouvé.");
            return;
        }

        switch (choice) {
            case 1:
                System.out.println("Services disponibles :");
                for (NotificationService service : notificationManager.getServices()) {
                    System.out.printf("%d. %s\n", notificationManager.getServices().indexOf(service) + 1, service.getServiceName());
                }
                System.out.print("Sélectionnez le service auquel vous abonner : ");
                int serviceChoice;
                try {
                    serviceChoice = scanner.nextInt();
                    scanner.nextLine();
                } catch (Exception e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                    scanner.nextLine();
                    break;
                }
                if (serviceChoice > 0 && serviceChoice <= notificationManager.getServices().size()) {
                    notificationManager.subscribe(emp, notificationManager.getServices().get(serviceChoice - 1));
                    userManager.saveEmployees();
                } else {
                    System.out.println("Choix invalide.");
                }
                break;

            case 2:
                List<NotificationService> subscribedServices = notificationManager.getSubscribedServicesForEmployee(emp);
                if (subscribedServices.isEmpty()) {
                    System.out.println("Non abonné à aucun service.");
                    break;
                }
                System.out.println("Services abonnés :");
                for (NotificationService service : subscribedServices) {
                    System.out.printf("%d. %s\n", subscribedServices.indexOf(service) + 1, service.getServiceName());
                }
                System.out.print("Sélectionnez le service à désabonner : ");
                int unsubChoice;
                try {
                    unsubChoice = scanner.nextInt();
                    scanner.nextLine();
                } catch (Exception e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                    scanner.nextLine();
                    break;
                }
                if (unsubChoice > 0 && unsubChoice <= subscribedServices.size()) {
                    notificationManager.unsubscribe(emp, subscribedServices.get(unsubChoice - 1));
                    userManager.saveEmployees();
                } else {
                    System.out.println("Choix invalide.");
                }
                break;

            case 3:
                System.out.printf("\nNotifications pour %s :\n", emp.getName());
                for (Notification notif : emp.getNotifications()) {
                    System.out.println(notif.toGmailFormat());
                }
                break;

            case 4:
                for (NotificationService service : notificationManager.getSubscribedServicesForEmployee(emp)) {
                    System.out.printf("\nEmployés abonnés à %s :\n", service.getServiceName());
                    for (Subscriber sub : notificationManager.getSubscribers()) {
                        if (notificationManager.getSubscribedServicesForEmployee((Employee) sub).contains(service)) {
                            System.out.printf("- %s (%s)\n", sub.getName(), sub.getEmail());
                        }
                    }
                }
                break;

            case 5:
                userManager.logout();
                System.out.println("Déconnecté.");
                break;

            default:
                System.out.println("Choix invalide.");
        }
    }
}