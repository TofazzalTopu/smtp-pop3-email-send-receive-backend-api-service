package com.email.emailservice.service.impl;

import com.email.emailservice.constant.AppConstant;
import com.email.emailservice.exceptions.NotFoundException;
import com.email.emailservice.model.MailContent;
import com.email.emailservice.repository.MailContentRepository;
import com.email.emailservice.service.MailDeleteService;
import com.email.emailservice.service.dtos.MailMetaData;
import com.email.emailservice.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class MailDeleteServiceImpl implements MailDeleteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailDeleteServiceImpl.class);

    @Value("${itc.email-service.file.attachment.path}")
    private String FILE_ATTACHMENT_PATH;


    private final MailContentRepository mailContentRepository;

    public MailDeleteServiceImpl(MailContentRepository mailContentRepository) {
        this.mailContentRepository = mailContentRepository;
    }

    @Override
    public void delete(Long companyId, Long userId, String id) {
        MailContent mailContent = mailContentRepository.findByIdAndCompanyIdAndUserId(id, companyId, userId).orElseThrow(()-> new NotFoundException(AppConstant.MAIL_NOT_FOUND));
        MailMetaData mailMetaData = (MailMetaData) mailContent.getMessageObject();
        FileUtils.deleteFiles(mailMetaData.getFileNames(), FILE_ATTACHMENT_PATH + File.separator + companyId + File.separator + userId);
        mailContentRepository.delete(mailContent);
        LOGGER.info(AppConstant.MAIL_DELETE_SUCCESS);
    }
}
