package com.email.emailservice.config;

import com.email.emailservice.config.providerConfig.EmailSenderServerConfigurationProvider;
import com.email.emailservice.constant.AppConstant;
import com.email.emailservice.service.dtos.SystemEmailInformation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.validation.ValidationException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @Author Tofazzal
 */
@Getter
@Setter
@Service
public class SystemEmailService implements Serializable {

    private static final long serialVersionUID = 1L;

    private EmailSenderServerConfigurationProvider appConfig;

    private String templateName;

    public SystemEmailService(EmailSenderServerConfigurationProvider appConfig) {
        this.appConfig = appConfig;
    }

    public <T> boolean send(SystemEmailInformation<T> emailContent)
            throws MessagingException, UnsupportedEncodingException {
        Session aSession = createEmailSendSession();
        Transport transport = aSession.getTransport();
        try {
            transport.connect();
            MimeMessage message = buildMessage(emailContent, aSession);
            Transport.send(message);
            return true;
        } finally {
            templateName = null;
            transport.close();
        }
    }

    private Session createEmailSendSession() {
        Session aSession = Session.getInstance(appConfig.populateSenderServerConfiguration(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(appConfig.getSenderUsername(), appConfig.getSenderPassword());
            }
        });
        return aSession;
    }

    private <T> MimeMessage buildMessage(SystemEmailInformation<T> emailInfo, Session session)
            throws UnsupportedEncodingException, MessagingException {
        if (templateName == null || templateName.isEmpty()) {
            throw new ValidationException("template name is required");
        }

        // For later:
        // check {templateName} file is exists in resource folder.
        // If not found then send Template file not found exception

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(appConfig.getSenderUsername(), "No-Reply:"));
        message.setSubject(AppConstant.ACCOUNT_VERIFICATION_EMAIL_SUBJECT);
        message.addRecipients(Message.RecipientType.TO, emailInfo.getEmail());
        message.setSentDate(new Date());
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(emailInfo.getContent(), "text/html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
        return message;
    }
}
