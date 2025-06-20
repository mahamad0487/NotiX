package com.notix.model;

import com.google.gson.annotations.Expose;
import com.notix.service.NotificationService;
import com.notix.service.Subscriber;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Employee implements Subscriber {
    @Expose
    private String matricule;
    @Expose
    private String nomcomplet;
    @Expose
    private String email;
    @Expose
    private boolean abonnee;
    @Expose
    private List<Notification> receivedNotifications;
    @Expose
    private List<String> subscribedServices;

    public Employee(String nomcomplet, String email) {
        this.matricule = UUID.randomUUID().toString();
        this.nomcomplet = nomcomplet;
        this.email = email;
        this.abonnee = false;
        this.receivedNotifications = new ArrayList<>();
        this.subscribedServices = new ArrayList<>();
    }

    @Override
    public void receiveNotification(Notification notification) {
        receivedNotifications.add(notification);
    }

    @Override
    public String getName() {
        return nomcomplet;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public String getMatricule() {
        return matricule;
    }

    public List<Notification> getNotifications() {
        return new ArrayList<>(receivedNotifications);
    }

    public List<String> getSubscribedServiceNames() {
        return new ArrayList<>(subscribedServices);
    }

    public void subscribeToService(NotificationService service) {
        if (!subscribedServices.contains(service.getServiceName())) {
            subscribedServices.add(service.getServiceName());
            abonnee = true;
        }
    }

    public void unsubscribeFromService(NotificationService service) {
        subscribedServices.remove(service.getServiceName());
        abonnee = !subscribedServices.isEmpty();
    }

    public List<NotificationService> getSubscribedServices() {
        throw new UnsupportedOperationException("Use NotificationManager.getSubscribedServicesForEmployee instead");
    }
}