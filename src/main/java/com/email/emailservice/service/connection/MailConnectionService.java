package com.email.emailservice.service.connection;

import com.email.emailservice.service.impl.MailPropertiesImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Service
public class MailConnectionService {

    @Value("${itc.email-service.fetch.username}")
    public String USERNAME;

    @Value("${itc.email-service.fetch.password}")
    public String PASSWORD;

    private final MailPropertiesImpl mailProperties;

    public MailConnectionService(MailPropertiesImpl mailProperties) {
        this.mailProperties = mailProperties;
    }

    public Session connectToPOP3MailServer() {
        return Session.getInstance(mailProperties.getPOP3Properties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }

    public Session connectToSMTPMailServer() {
        return Session.getInstance(mailProperties.getSMTPMailProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }

    public Session connectToMailServer(Properties properties, String username, String password) {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }
}
