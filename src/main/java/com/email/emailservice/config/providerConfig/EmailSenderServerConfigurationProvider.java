package com.email.emailservice.config.providerConfig;

import java.util.Properties;

/**
 * @author Tofazzal
 * @apiNote since 1.0.0
 *
 * @desc Sender email server configuration provider
 */
public interface EmailSenderServerConfigurationProvider {
  Properties populateSenderServerConfiguration();

  String getSenderUsername();

  String getSenderPassword();

  boolean isSendServerConnected();
}
