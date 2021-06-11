package com.email.emailservice.service;

import com.email.emailservice.model.MailContent;
import com.email.emailservice.service.dtos.MailContentRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MailSendingService {
    MailContent sendMail(Long companyId, Long userId, MailContentRequest request, List<MultipartFile> files) throws Exception;
}
