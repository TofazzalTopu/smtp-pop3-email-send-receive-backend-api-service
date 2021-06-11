package com.email.emailservice.utils;

import com.email.emailservice.service.dtos.MailContentStore;
import org.springframework.util.StringUtils;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

final public class AppUtils {

    private AppUtils() {
        throw new IllegalStateException("AppUtils is a Utility class. Instantiation is not allowed");
    }

    /**
     * Return the primary text content of the message.
     */
    final public static String getText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            if (!p.isMimeType("text/html")) {
                String s = (String) p.getContent();
                return s;
            }
        }

        if (p.isMimeType("multipart/alternative")) {
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }
        return "";
    }

    final public static InternetAddress[] buildInternetAddress(List<String> addresses) throws Exception {
        return addresses.stream().map(address -> {
            try {
                return new InternetAddress(address);
            } catch (AddressException e) {
                throw Problem.builder().withDetail("Invailid Address: " + e.getLocalizedMessage())
                        .withStatus(Status.BAD_REQUEST).withTitle(Status.BAD_REQUEST.name()).build();
            }
        }).collect(Collectors.toList()).stream().toArray(InternetAddress[]::new);
    }

    public static MailContentStore getMailContent(Message message) throws MessagingException, IOException {
        List<String> fileNames = new ArrayList<>();
        List<BodyPart> bodyPartList = new ArrayList<>();

        String text = "";
        if (message.isMimeType("text/plain")) {
            text = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            for (int x = 0; x < mimeMultipart.getCount(); x++) {
                BodyPart bodyPart = mimeMultipart.getBodyPart(x);
                String disposition = bodyPart.getDisposition();
                if (x == 0) {
                    text = AppUtils.getText(bodyPart);
                }
                if (disposition != null && (disposition.equals(BodyPart.ATTACHMENT))) {
                    String fileName = StringUtils.cleanPath(bodyPart.getFileName());
                    fileNames.add(fileName);
                    bodyPartList.add(bodyPart);
                }
            }
        }
        return new MailContentStore(text, fileNames, bodyPartList);
    }
}
