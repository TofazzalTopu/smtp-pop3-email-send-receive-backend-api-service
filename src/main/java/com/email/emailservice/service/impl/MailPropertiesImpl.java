package com.email.emailservice.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailPropertiesImpl{

    @Value("${itc.email-service.transport.host}")
    private String SMTP_HOST;
    @Value("${itc.email-service.transport.port}")
    private int SMTP_PORT;
    @Value("${itc.email-service.transport.protocol}")
    private String SMTP_PROTOCOL;

    @Value("${itc.email-service.transport.ssl}")
    private boolean SMTP_SSL;

    @Value("${itc.email-service.transport.socket.factory.port}")
    private int SMTP_SOCKET_FACTORY_PORT;

    @Value("${itc.email-service.transport.socket.factory.fallback}")
    private boolean SMTP_SOCKET_FACTORY_FALLBACK;

    @Value("${itc.email-service.transport.starttls.enable}")
    private boolean SMTP_STARTTLS_ENABLE;

    @Value("${itc.email-service.transport.auth}")
    private boolean SMTP_AUTH;

    @Value("${itc.email-service.transport.debug}")
    private boolean SMTP_DEBUG;

    @Value("${itc.email-service.fetch.starttls.enable}")
    private boolean POP_STARTTLS_ENABLE;

    @Value("${itc.email-service.fetch.starttls.required}")
    private boolean POP_STARTTLS_REQUIRED;

    @Value("${itc.email-service.fetch.port}")
    private int POP_PORT;

    @Value("${itc.email-service.fetch.socket.factory.port}")
    private int POP_SOCKET_FACTORY_PORT;

    @Value("${itc.email-service.fetch.socket.factory.fallback}")
    private boolean POP_SOCKET_FACTORY_FALLBACK;

    @Value("${itc.email-service.fetch.protocol}")
    private String POP_PROTOCOL;

    @Value("${itc.email-service.fetch.host}")
    private String POP_HOST;


    public Properties getPOP3Properties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", POP_PROTOCOL);
        properties.put("mail.pop3.host", POP_HOST);
        properties.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.pop3.socketFactory.fallback", POP_SOCKET_FACTORY_FALLBACK);
        properties.put("mail.pop3.socketFactory.port", POP_SOCKET_FACTORY_PORT);
        properties.put("mail.pop3.port", POP_PORT);
        return properties;
    }

    public Properties getSMTPMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", SMTP_PROTOCOL);
        properties.put("mail.smtp.auth", SMTP_DEBUG);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.setProperty("mail.smtp.host", SMTP_HOST);
        properties.put("mail.debug", SMTP_AUTH);
        properties.put("mail.smtp.socketFactory.port", SMTP_SOCKET_FACTORY_PORT);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", SMTP_SOCKET_FACTORY_FALLBACK);
        properties.put("mail.smtp.starttls.enable", SMTP_STARTTLS_ENABLE);
        return properties;
    }

    public Properties getMailProperties(String protocol, String port, String server) {
        Properties properties = new Properties();
        if (protocol.equalsIgnoreCase("pop3")) {
            properties.setProperty("mail.transport.protocol", POP_PROTOCOL);
            properties.put("mail.pop3.host", POP_HOST);
            properties.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.pop3.socketFactory.fallback", POP_SOCKET_FACTORY_FALLBACK);
            properties.put("mail.pop3.socketFactory.port", POP_SOCKET_FACTORY_PORT);
            properties.put("mail.pop3.port", POP_PORT);

        } else if (protocol.equalsIgnoreCase("smtp")) {
            properties.setProperty("mail.transport.protocol", protocol.toLowerCase());
            properties.put("mail.smtp.auth", SMTP_DEBUG);
            properties.put("mail.smtp.port", port);
            properties.setProperty("mail.smtp.host", server);
            properties.put("mail.debug", SMTP_AUTH);
            properties.put("mail.smtp.socketFactory.port", SMTP_SOCKET_FACTORY_PORT);
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.socketFactory.fallback", SMTP_SOCKET_FACTORY_FALLBACK);
            properties.put("mail.smtp.starttls.enable", SMTP_STARTTLS_ENABLE);
        }
        return properties;
    }
}
