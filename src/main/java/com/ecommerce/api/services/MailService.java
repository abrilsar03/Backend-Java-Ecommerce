
package com.ecommerce.api.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender sender;

    public MailService(JavaMailSender sender) {
        this.sender = sender;
    }

    public void paymentSuccess(String to, String orderId) {
        send(to, "Payment success", "Your payment for order " + orderId + " was successful.");
    }

    public void paymentFailed(String to, String orderId, int attempts) {
        send(to, "Payment failed",
                "Your payment for order " + orderId + " failed after " + attempts + " attempts.");
    }

    private void send(String to, String subject, String text) {
        try {
            var msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(text);
            sender.send(msg);
        } catch (Exception ignored) {
        }
    }
}
