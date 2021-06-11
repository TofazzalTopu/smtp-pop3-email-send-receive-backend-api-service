package com.email.emailservice.config.providerConfig;

import javax.mail.NoSuchProviderException;
import java.util.Properties;

/**
 * @author Tofazzal
 * @apiNote since 1.0.0
 *
 * @desc Email fetch server configuration provider
 */
public interface EmailFetchServerConfigurationProvider {
  Properties populateFetchServerConfiguration(EmailFetchProtocol protocol);

  String getFetchUsername();

  String getFetchPassword();

  boolean isFetchServerConnected() throws NoSuchProviderException;
}
