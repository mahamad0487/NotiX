package com.notix.service;

import com.notix.model.Notification;

public interface NotificationService {
    void sendNotification(Subscriber subscriber, Notification notification);
    String getServiceName();
}