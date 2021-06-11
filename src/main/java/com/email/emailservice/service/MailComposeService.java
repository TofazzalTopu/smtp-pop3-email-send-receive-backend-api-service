package com.email.emailservice.service;

import com.email.emailservice.model.MailContent;
import com.email.emailservice.service.dtos.MailContentRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MailComposeService {
    MailContent saveNewMailContentMetaData(Long companyId, Long userId, MailContentRequest mailContentRequest, List<MultipartFile> files) throws Exception;
}
