package com.notix.model;

import com.google.gson.annotations.Expose;

public class Notification {
    @Expose
    private String senderName;
    @Expose
    private String senderEmail;
    @Expose
    private String content;
    @Expose
    private String method;

    public Notification(String senderName, String senderEmail, String content, String method) {
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.content = content;
        this.method = method;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getContent() {
        return content;
    }

    public String getMethod() {
        return method;
    }

    public String toGmailFormat() {
        return String.format("From: %s <%s>\nTo: %s\nSubject: Notification\n\n%s\n\n[Sent via %s]",
                senderName, senderEmail, senderEmail, content, method);
    }
}