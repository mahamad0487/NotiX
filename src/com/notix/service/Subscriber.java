package com.notix.service;

import com.notix.model.Notification;

public interface Subscriber {
    void receiveNotification(Notification notification);
    String getName();
    String getEmail();
}