package com.frogcrew.frogcrew_backend.invite;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void send(String to, String subject, String content){
        // For development/testing, just log to console
        System.out.println("----- EMAIL SENT -----");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Content:\n" + content);
        System.out.println("----------------------");
    }
}
