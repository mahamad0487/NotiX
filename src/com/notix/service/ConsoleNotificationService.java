package com.notix.service;

import com.notix.model.Notification;

public class ConsoleNotificationService implements NotificationService {
    @Override
    public void sendNotification(Subscriber subscriber, Notification notification) {
        System.out.printf("Console Notification for %s: %s\n", subscriber.getName(), notification.toGmailFormat());
    }

    @Override
    public String getServiceName() {
        return "Console";
    }
}