package com.email.emailservice.config;

import com.email.emailservice.config.providerConfig.EmailFetchProtocol;
import com.email.emailservice.config.providerConfig.EmailFetchServerConfigurationProvider;
import com.email.emailservice.config.providerConfig.EmailSenderServerConfigurationProvider;
import com.email.emailservice.service.dtos.FetchProtocolCredential;
import com.email.emailservice.service.dtos.SmtpCredential;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.Authenticator;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

/**
 * @author Tofazzal
 * @apiNote since 1.0.0
 * @implNote implements EmailFetchServerConfigurationProvider and
 *           EmailSenderServerConfigurationProvider
 * @description provides email server configuration for user email account
 */

@Getter
@Setter
public class UserEmailServerConfigurationProvider
        implements EmailFetchServerConfigurationProvider, EmailSenderServerConfigurationProvider {

    private final FetchProtocolCredential fetchCredential;
    private final SmtpCredential smtpCredential;

    @Autowired
    FetchEmailConfiguration fetchEmailConfiguration;

    public UserEmailServerConfigurationProvider(SmtpCredential smtpCredential,
            FetchProtocolCredential fetchCredential) {
        this.smtpCredential = smtpCredential;
        this.fetchCredential = fetchCredential;
    }

    @Override
    public Properties populateSenderServerConfiguration() {
       return null;
    }

    @Override
    public String getSenderUsername() {
        return null;
    }

    @Override
    public String getSenderPassword() {
        return null;
    }

    @Override
    public Properties populateFetchServerConfiguration(EmailFetchProtocol protocol) {
        Properties properties = new Properties();
        properties.put("mail.pop3.host", fetchEmailConfiguration.getPopHost());
        properties.put("mail.pop3.port", fetchCredential.getPort());
        properties.put("mail.transport.protocol", fetchEmailConfiguration.getPopProtocol());
        properties.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.pop3.socketFactory.fallback", fetchEmailConfiguration.isPopSocketFactoryFallback());
        properties.put("mail.pop3.socketFactory.port", fetchEmailConfiguration.getPopSocketFactoryPort());
        properties.put("mail.pop3.starttls.enable", fetchEmailConfiguration.isPopTlsEnabled());
        return properties;
    }

    @Override
    public String getFetchUsername() {
        return fetchCredential.getUsername();
    }

    @Override
    public String getFetchPassword() {
        return fetchCredential.getPassword();
    }

    @Override
    public boolean isFetchServerConnected() throws NoSuchProviderException {
        Session session = createFetchEmailSession();
        return session.getTransport().isConnected();
    }

    @Override
    public boolean isSendServerConnected() {
        return false;
    }

    private Session createFetchEmailSession() {
        Properties properties = populateFetchServerConfiguration(EmailFetchProtocol.POP3);
        Session aSession = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(getFetchUsername(), getFetchPassword());
            }
        });
        return aSession;
    }
}
