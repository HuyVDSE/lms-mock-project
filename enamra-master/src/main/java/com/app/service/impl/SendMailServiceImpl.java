package com.app.service.impl;

import com.app.service.SendMailService;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class SendMailServiceImpl implements SendMailService {
    @Override
    public void sendMail(String email, String random, String msg) {
        String from = "huyv46";
        String pass = "thehuan1204";
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session sesionMail = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(sesionMail);
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            if (msg.equals("VERIFY")) {
                message.setSubject("User Email Verification");
                message.setText("Registered successfully.Please verify your account using this link: http://localhost:8088/user/settings/verify?email=" + email + "&code=" + random);
            } else if (msg.equals("RESET_PASSWORD")) {
                message.setSubject("Reset password");
                message.setText("Please using this link: http://localhost:8088/user/reset_password?email=" + email + "&code=" + random + " to reset your password.");
            }
            Transport transport = sesionMail.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ex) {
            ex.printStackTrace();
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
}
