package com.email.emailservice.service;

import com.email.emailservice.model.MailContent;

import java.util.List;

public interface MailReceiveService {

    List<MailContent> receiveMailList(Long companyId, Long userId) throws Exception;
}
