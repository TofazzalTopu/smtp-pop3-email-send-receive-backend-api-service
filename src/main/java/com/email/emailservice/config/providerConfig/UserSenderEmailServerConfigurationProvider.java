package com.email.emailservice.config.providerConfig;

import java.util.Properties;

public interface UserSenderEmailServerConfigurationProvider {
    Properties populateSenderServerConfiguration(String protocol, String port);
}
