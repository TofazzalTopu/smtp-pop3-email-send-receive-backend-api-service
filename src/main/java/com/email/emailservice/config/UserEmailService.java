package com.email.emailservice.config;

import com.email.emailservice.config.providerConfig.EmailFetchProtocol;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.Serializable;
import java.util.Properties;

/**
 * @Author Tofazzal
 */
@Getter
@Setter
@Service
public class UserEmailService implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserEmailServerConfigurationProvider configurationProvider;

    public boolean isConnectedToUserFetchEmailServer() throws NoSuchProviderException {
        Session session = createFetchEmailSession();
        return session.getTransport().isConnected();
    }

    private Session createFetchEmailSession() {
        Properties properties = configurationProvider.populateFetchServerConfiguration(EmailFetchProtocol.POP3);
        Session aSession = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(configurationProvider.getFetchUsername(), configurationProvider.getFetchPassword());
            }
        });
        return aSession;
    }
}
