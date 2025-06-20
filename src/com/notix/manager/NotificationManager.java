package com.notix.manager;

import com.notix.model.Employee;
import com.notix.model.Notification;
import com.notix.service.ConsoleNotificationService;
import com.notix.service.EmailNotificationService;
import com.notix.service.NotificationService;
import com.notix.service.Subscriber;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationManager {
    private List<Subscriber> subscribers;
    private List<NotificationService> services;
    private Map<String, NotificationService> serviceMap;

    public NotificationManager() {
        this.subscribers = new ArrayList<>();
        this.services = new ArrayList<>();
        this.serviceMap = new HashMap<>();
        NotificationService consoleService = new ConsoleNotificationService();
        NotificationService emailService = new EmailNotificationService();
        services.add(consoleService);
        services.add(emailService);
        serviceMap.put(consoleService.getServiceName(), consoleService);
        serviceMap.put(emailService.getServiceName(), emailService);
    }

    public void initializeSubscribers(List<Employee> employees) {
        subscribers.clear();
        for (Employee employee : employees) {
            if (!subscribers.contains(employee)) {
                subscribers.add(employee);
            }
        }
    }

    public void subscribe(Subscriber subscriber, NotificationService service) {
        if (!(subscriber instanceof Employee)) {
            System.out.println("Erreur : Seuls les employés peuvent s'abonner.");
            return;
        }
        Employee employee = (Employee) subscriber;
        if (!subscribers.contains(employee)) {
            subscribers.add(employee);
        }
        employee.subscribeToService(service);
        System.out.printf("%s a été inscrit au service %s.\n", employee.getName(), service.getServiceName());
    }

    public void unsubscribe(Subscriber subscriber, NotificationService service) {
        if (!(subscriber instanceof Employee)) {
            System.out.println("Erreur : Seuls les employés peuvent se désabonner.");
            return;
        }
        Employee employee = (Employee) subscriber;
        employee.unsubscribeFromService(service);
        if (employee.getSubscribedServiceNames().isEmpty()) {
            subscribers.remove(employee);
        }
        System.out.printf("%s a été désinscrit du service %s.\n", employee.getName(), service.getServiceName());
    }

    public void notifySubscribers(String message, String senderEmail) {
        for (Subscriber subscriber : new ArrayList<>(subscribers)) {
            if (!subscriber.getEmail().equals(senderEmail)) {
                Employee employee = (Employee) subscriber;
                for (NotificationService service : getSubscribedServicesForEmployee(employee)) {
                    Notification notification = new Notification(
                            getSubscriberNameByEmail(senderEmail),
                            senderEmail,
                            message,
                            service.getServiceName()
                    );
                    service.sendNotification(subscriber, notification);
                    subscriber.receiveNotification(notification);
                }
            }
        }
    }

    public boolean isSubscribedToService(Subscriber subscriber, NotificationService service) {
        if (!(subscriber instanceof Employee)) {
            return false;
        }
        Employee employee = (Employee) subscriber;
        return subscribers.contains(employee) && getSubscribedServicesForEmployee(employee).contains(service);
    }

    public List<Subscriber> getSubscribers() {
        return new ArrayList<>(subscribers);
    }

    public List<NotificationService> getServices() {
        return new ArrayList<>(services);
    }

    public String getSubscriberNameByEmail(String email) {
        for (Subscriber subscriber : subscribers) {
            if (subscriber.getEmail().equals(email)) {
                return subscriber.getName();
            }
        }
        return "Unknown";
    }

    public List<NotificationService> getSubscribedServicesForEmployee(Employee employee) {
        List<NotificationService> subscribedServices = new ArrayList<>();
        for (String serviceName : employee.getSubscribedServiceNames()) {
            NotificationService service = serviceMap.get(serviceName);
            if (service != null) {
                subscribedServices.add(service);
            }
        }
        return subscribedServices;
    }
}