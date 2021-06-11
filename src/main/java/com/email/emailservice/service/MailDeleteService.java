package com.email.emailservice.service;

public interface MailDeleteService {
    void delete(Long companyId, Long userId, String id) throws Exception;
}
