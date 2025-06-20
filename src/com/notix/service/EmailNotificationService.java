package com.notix.service;

import com.notix.model.Notification;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * Service de notification par email utilisant JavaMail pour envoyer des emails via SMTP (Gmail).
 */
public class EmailNotificationService implements NotificationService {
    private final Properties props;
    private final String fromEmail;
    private final String password;

    /**
     * Constructeur configurant les paramètres SMTP pour Gmail.
     * Remplacer 'votre.email@gmail.com' et 'your-app-password' par vos propres identifiants.
     */
    public EmailNotificationService() {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
         this.fromEmail = "dmbiamahamad0487@gmail.com"; // Remplacer par votre email
        this.password = "abcdefghijklmnop"; // Remplacer par votre mot de passe d'application
    }

    /**
     * Envoie une notification par email à un abonné.
     * @param subscriber L'abonné destinataire.
     * @param notification La notification à envoyer par email
     */
    @Override
    public void sendNotification(Subscriber subscriber, Notification notification) {
        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(subscriber.getEmail()));
            message.setSubject("Notification");
            message.setText(notification.toGmailFormat());

            Transport.send(message);
            System.out.printf("Email envoyé à %s\n", subscriber.getEmail());
        } catch (MessagingException e) {
            System.err.printf("Erreur lors de l'envoi de l'email à %s : %s\n", subscriber.getEmail(), e.getMessage());
        }
    }

    /**
     * Retourne le nom du service.
     * @return Le nom du service ("Email").
     */
    @Override
    public String getServiceName() {
        return "Email";
    }
}