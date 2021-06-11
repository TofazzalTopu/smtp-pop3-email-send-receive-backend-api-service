package com.email.emailservice.config.providerConfig;

import java.util.Properties;

public interface UserFetchEmailServerConfigurationProvider {
    Properties populateFetchServerConfiguration(String protocol, String port);
}
