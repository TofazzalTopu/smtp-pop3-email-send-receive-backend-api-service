package com.email.emailservice.service.impl;

import com.email.emailservice.model.MailContent;
import com.email.emailservice.repository.MailContentRepository;
import com.email.emailservice.service.LabelService;
import com.email.emailservice.service.MailComposeService;
import com.email.emailservice.service.dtos.MailContentRequest;
import com.email.emailservice.service.dtos.MailMetaData;
import com.email.emailservice.utils.AppUtils;
import com.email.emailservice.utils.FileUtils;
import com.email.emailservice.constant.AppConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Address;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service("mailComposeService")
public class MailComposeServiceImpl implements MailComposeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailComposeServiceImpl.class);

    @Value("${itc.email-service.file.attachment.path}")
    private String FILE_ATTACHMENT_PATH;

    private final MailContentRepository mailContentRepository;
    private final LabelService labelService;

    MailComposeServiceImpl(MailContentRepository mailContentRepository, LabelService labelService) {
        this.mailContentRepository = mailContentRepository;
        this.labelService = labelService;
    }

    // Compose new mail
    @Override
    public MailContent saveNewMailContentMetaData(Long companyId, Long userId, MailContentRequest mailContentRequest, List<MultipartFile> files) throws Exception {
        MailContent mailContent = buildMailContent(companyId, userId, mailContentRequest, files);
        FileUtils.saveFiles(files, FILE_ATTACHMENT_PATH + File.separator + companyId + File.separator + userId);
        mailContent = mailContentRepository.save(mailContent);

        return mailContent;
    }

    private MailContent buildMailContent(Long companyId, Long userId, MailContentRequest mailContentRequest, List<MultipartFile> files) throws Exception {
        MailContent mailContent = new MailContent();
        mailContent.setSenderEmail(mailContentRequest.getSenderEmail());
        mailContent.setSenderName(mailContentRequest.getSenderName());
        mailContent.setSubject(mailContentRequest.getSubject());
        mailContent.setContent(mailContentRequest.getContent());
        mailContent.setReceivedDate(new Date());
        mailContent.setSentDate(new Date());
        mailContent.setForward(mailContentRequest.isForward());
        mailContent.setDraft(mailContentRequest.isDraft());
        mailContent.setReply(mailContentRequest.isReply());
        mailContent.setSent(mailContentRequest.isSent());
        mailContent.setLabel(labelService.getLabelValueByName(companyId, userId, mailContentRequest.getLabels()));
        mailContent.setLabels(mailContentRequest.getLabels());
        mailContent.setCompanyId(companyId);
        mailContent.setUserId(userId);

        mailContent.setMessageObject(buildMailMetaData(companyId, userId, mailContentRequest, files));
        return mailContent;
    }

    private MailMetaData buildMailMetaData(Long companyId, Long userId, MailContentRequest mailContentRequest, List<MultipartFile> files) throws Exception {
        MailMetaData mailMetaData = new MailMetaData();
        mailMetaData.setSenderEmail(Arrays.asList(AppUtils.buildInternetAddress(Arrays.asList(mailContentRequest.getSenderEmail()))));
        mailMetaData.setTo(Arrays.asList(AppUtils.buildInternetAddress(mailContentRequest.getTo())));
        mailMetaData.setCc(Arrays.asList(mailContentRequest.getCc() != null ? AppUtils.buildInternetAddress(mailContentRequest.getCc()) : new Address[0]));
        mailMetaData.setBcc(Arrays.asList(mailContentRequest.getBcc() != null ? AppUtils.buildInternetAddress(mailContentRequest.getBcc()) : new Address[0]));
        mailMetaData.setReplyTo(Arrays.asList(mailContentRequest.getReplyTo() != null ? AppUtils.buildInternetAddress(mailContentRequest.getReplyTo()) : new Address[0]));

        mailMetaData.setFileNames(FileUtils.getFileNames(files));
        mailMetaData.setAttachmentPath(FILE_ATTACHMENT_PATH + File.separator + companyId + File.separator + userId);
        mailMetaData.setLineCount(mailContentRequest.getLineCount());
        mailMetaData.setMessageNumber(mailContentRequest.getMessageNumber());
        mailMetaData.setDescription(mailContentRequest.getDescription());
        mailMetaData.setDisposition(mailContentRequest.getDisposition());
        LOGGER.info(AppConstant.MAIL_META_DATA_BUILD_SUCCESS);
        return mailMetaData;
    }

}
