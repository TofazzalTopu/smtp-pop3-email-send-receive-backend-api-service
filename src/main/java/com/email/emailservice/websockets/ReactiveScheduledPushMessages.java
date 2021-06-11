package com.email.emailservice.websockets;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import reactor.core.publisher.Flux;

import java.time.Duration;

//@Service
public class ReactiveScheduledPushMessages implements InitializingBean {

    @Value("${itc.email-service.fetch.fetch-frequency:600}")
    private long fetchFrequency;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public ReactiveScheduledPushMessages(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

//    @Override
    public void afterPropertiesSet() {
        Flux.interval(Duration.ofSeconds(fetchFrequency))
                .map((n) -> {
                    try {
                        return null;
//                        return mailReceiveService.receiveMailList(5L, 6L);
                    } catch (Exception e) {
                        throw e;
                    }
                })
                .subscribe(message -> simpMessagingTemplate.convertAndSend("/topic/push-messages", message));
    }
}
