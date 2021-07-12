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
    public String sendMail(String email) {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        String random = String.format("%06d", number);
        String from = "tringuyen11122000";
        String pass = "Minhtri11122000";
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

            message.setSubject("User Email Verification");
            message.setText("Registered successfully.Please verify your account using this code: " + random);
            Transport transport = sesionMail.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ex) {
            ex.printStackTrace();
//            BasicConfigurator.configure();
//            LOGGER.error("CreateRecordServlet_AddressException: " + ex.getMessage());
        } catch (MessagingException ex) {
            ex.printStackTrace();
//            BasicConfigurator.configure();
//            LOGGER.error("CreateRecordServlet_MessagingException: " + ex.getMessage());
        }
        return random;
    }
}
