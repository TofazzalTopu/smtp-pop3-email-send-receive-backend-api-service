package com.email.emailservice.service.impl;

import com.email.emailservice.constant.AppConstant;
import com.email.emailservice.exceptions.NotFoundException;
import com.email.emailservice.model.MailContent;
import com.email.emailservice.repository.MailContentRepository;
import com.email.emailservice.service.LabelService;
import com.email.emailservice.service.MailUpdateService;
import com.email.emailservice.service.dtos.MailUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public class MailUpdateServiceImpl implements MailUpdateService {

    private final MailContentRepository mailContentRepository;
    private final LabelService labelService;

    public MailUpdateServiceImpl(MailContentRepository mailContentRepository, LabelService labelService) {
        this.mailContentRepository = mailContentRepository;
        this.labelService = labelService;
    }

    @Override
    public MailContent updateMailLabel(Long companyId, Long userId, MailUpdateRequest request) {
        MailContent mailContent = mailContentRepository
                .findByIdAndCompanyIdAndUserId(request.getMailId(), companyId, userId)
                .orElseThrow(() -> new NotFoundException(AppConstant.MAIL_NOT_FOUND));
        mailContent.setLabel(labelService.getLabelValueByName(companyId, userId, request.getLabelNames()));
        mailContent.setLabels(request.getLabelNames());
        return mailContentRepository.save(mailContent);
    }
}
