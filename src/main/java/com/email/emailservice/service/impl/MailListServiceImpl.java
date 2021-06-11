package com.email.emailservice.service.impl;


import com.email.emailservice.constant.AppConstant;
import com.email.emailservice.exceptions.NotFoundException;
import com.email.emailservice.model.MailContent;
import com.email.emailservice.repository.MailContentRepository;
import com.email.emailservice.service.LabelService;
import com.email.emailservice.service.MailListService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MailListServiceImpl implements MailListService {

    private final MailContentRepository mailContentRepository;
    private final LabelService labelService;

    MailListServiceImpl(MailContentRepository mailContentRepository, LabelService labelService) {
        this.mailContentRepository = mailContentRepository;
        this.labelService = labelService;
    }

    @Override
    public List<MailContent> findAll(Long companyId, Long userId) {
        List<MailContent> mailContentList = mailContentRepository.findByCompanyIdAndUserIdAndLabelIsGreaterThan( companyId, userId, 0L);
        mailContentList.forEach(mailContent -> mailContent
                .setLabels(labelService.getLabelNamesByValue(companyId, userId, mailContent.getLabel())));
        return mailContentList;
    }

    @Override
    public boolean isEmailExistByEmailCompanyIdAndUserId(String senderEmail, Long companyId, Long userId) {
        return mailContentRepository.existsBySenderEmailAndCompanyIdAndUserId(senderEmail, companyId, userId);
    }

    @Override
    public List<MailContent> findByLabelName(Long companyId, Long userId, String labelName) throws Exception {
        Long labelValue = labelService.findByLabelName(companyId, userId, labelName);
        List<MailContent> mailContentList = mailContentRepository.findByCompanyIdAndUserIdAndLabelIsGreaterThan(companyId, userId, 0L)
                .stream().filter(mailContent -> (mailContent.getLabel() & labelValue) == labelValue).collect(Collectors.toList());
        mailContentList.forEach(mailContent -> mailContent
                .setLabels(labelService.getLabelNamesByValue(companyId, userId, mailContent.getLabel())));
        return mailContentList;
    }

    @Override
    public MailContent findById(Long companyId, Long userId, String id) {
        MailContent mailContent= mailContentRepository.findByIdAndCompanyIdAndUserId(id, companyId, userId).orElseThrow(()-> new NotFoundException(AppConstant.MAIL_NOT_FOUND));
        mailContent.setLabels(labelService.getLabelNamesByValue(companyId, userId, mailContent.getLabel()));
        return mailContent;
    }
}
