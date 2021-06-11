package com.email.emailservice.service;

import com.email.emailservice.model.MailContent;
import com.email.emailservice.service.dtos.MailUpdateRequest;

public interface MailUpdateService {
    MailContent updateMailLabel(Long companyId, Long userId, MailUpdateRequest request) throws Exception;
}
