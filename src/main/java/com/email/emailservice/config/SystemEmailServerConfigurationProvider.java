package com.email.emailservice.config;

import com.email.emailservice.config.providerConfig.EmailSenderServerConfigurationProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Tofazzal
 * @apiNote since 1.0.0
 * @implNote impliments EmailSenderServerConfigurationProvider
 *
 * @desc Sends system generated emails
 */
@Getter
@Setter
@Component
public class SystemEmailServerConfigurationProvider implements EmailSenderServerConfigurationProvider {

    @Value("${itc.email-service.application.host}")
    public String mailApiServiceHost;

    @Value("${itc.email-service.transport.username}")
    private String smtpUsername;

    @Value("${itc.email-service.transport.password}")
    private String smtpPassword;

    @Value("${itc.email-service.transport.host}")
    private String smtpHost;

    @Value("${itc.email-service.transport.port}")
    private int smtpPort;

    @Value("${itc.email-service.transport.secruity-protocol}")
    private String smtpSecurityProtocol;

    @Value("${itc.email-service.transport.socket.factory.port}")
    private int smtpSocketFactoryPort;

    @Value("${itc.email-service.transport.socket.factory.fallback}")
    private boolean smtpSocketfactoryFallback;

    @Value("${itc.email-service.transport.starttls.enable}")
    private boolean smtpTlsEnabled;

    @Value("${itc.email-service.transport.auth:true}")
    private boolean smtpAuthentication;

    @Value("${itc.email-service.transport.debug:false}")
    private boolean smtpDebug;

    @Override
    public Properties populateSenderServerConfiguration() {
        // Rony: please add necessary email security properties @Tofazzal bhai
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", smtpAuthentication);
        properties.put("mail.smtp.port", smtpPort);
        properties.setProperty("mail.smtp.host", smtpHost);
        properties.put("mail.debug", smtpDebug);
        properties.put("mail.smtp.socketFactory.port", smtpSocketFactoryPort);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", smtpSocketfactoryFallback);
        properties.put("mail.smtp.starttls.enable", smtpTlsEnabled);
        return properties;
    }

    @Override
    public String getSenderUsername() {
        return smtpUsername;
    }

    @Override
    public String getSenderPassword() {
        return smtpPassword;
    }

    @Override
    public boolean isSendServerConnected() {
        return true;
    }

}
