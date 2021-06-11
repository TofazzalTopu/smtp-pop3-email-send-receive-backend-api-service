package com.email.emailservice.service;

import com.email.emailservice.model.MailContent;

import javax.naming.AuthenticationException;
import java.util.List;

public interface MailListService {

    List<MailContent> findAll(Long companyId, Long userId) throws AuthenticationException;
    boolean isEmailExistByEmailCompanyIdAndUserId(String senderEmail, Long companyId, Long userId);
    MailContent findById(Long companyId, Long userId, String id) throws Exception;
    List<MailContent> findByLabelName(Long companyId, Long userId, String name) throws Exception;
}
