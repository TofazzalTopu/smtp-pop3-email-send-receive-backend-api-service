package com.email.emailservice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class FetchEmailConfiguration {

    @Value("${itc.email-service.fetch.host}")
    private String popHost;

    @Value("${itc.email-service.fetch.port}")
    private int popPort;

    @Value("${itc.email-service.fetch.protocol}")
    private String popProtocol;

    @Value("${itc.email-service.fetch.socket.factory.port}")
    private int popSocketFactoryPort;

    @Value("${itc.email-service.fetch.socket.factory.fallback}")
    private boolean popSocketFactoryFallback;

    @Value("${itc.email-service.fetch.starttls.enable}")
    private boolean popTlsEnabled;

    @Value("${itc.email-service.fetch.starttls.required}")
    private boolean popTlsRequired;

}
