package com.email.emailservice.config.providerConfig;

import java.util.Properties;

/**
 * @author Tofazzal
 * @apiNote since 1.0.0
 *
 * @desc User Sender email server configuration provider
 */

public interface EmailServerConfigurationProvider {
    Properties populateFetchServerConfiguration(String protocol, String port);
    Properties populateSenderServerConfiguration(String protocol, String port);
}
