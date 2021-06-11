package com.email.emailservice.test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.email.emailservice.constant.AppConstant;
import com.email.emailservice.model.MailContent;
import com.email.emailservice.service.connection.MailConnectionService;
import com.email.emailservice.service.dtos.MailMetaData;
import com.email.emailservice.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;


@Service
public class TestServiceImpl implements TestService {

    @Value("${itc.email-service.file.attachment.path}")
    private String FILE_ATTACHMENT_PATH;

    private final MailConnectionService mailConnectionService;

    public TestServiceImpl(MailConnectionService mailConnectionService) {
        this.mailConnectionService = mailConnectionService;
    }

    @Override
    public MailContent sendMail(List<MultipartFile> files) throws Exception {
        MailContent mailContent = new MailContent();
        Session session = mailConnectionService.connectToSMTPMailServer();
        if (session == null) {
            throw new AuthenticationFailedException(AppConstant.INCORRECT_USER_NAME_PASSWORD);
        }
        FileUtils.saveFiles(files, FILE_ATTACHMENT_PATH);
        Transport transport = session.getTransport();
        MimeMessage message = buildMessage(session, mailContent, files);
        transport.connect();
        Transport.send(message);
        transport.close();
        return mailContent;
    }

    private MimeMessage buildMessage(Session session, MailContent mailContent, List<MultipartFile> files) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailContent.getSenderEmail()));
        message.setSubject(mailContent.getSubject());

        MailMetaData mailMetaData = (MailMetaData) mailContent.getMessageObject();
        message.addRecipients(Message.RecipientType.TO, mailMetaData.getTo().toArray(new Address[mailMetaData.getTo().size()]));
        message.addRecipients(Message.RecipientType.CC, mailMetaData.getCc().toArray(new Address[mailMetaData.getCc().size()]));
        message.addRecipients(Message.RecipientType.BCC, mailMetaData.getBcc().toArray(new Address[mailMetaData.getBcc().size()]));

        message.setSentDate(new Date());

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(mailContent.getContent());
        message.setContent(FileUtils.buildMultipart(files, FILE_ATTACHMENT_PATH));

        return message;
    }

    private static Claim getClaim(String claimKey) {
        try {
            String token = generateToken();
            System.out.println(token);
            DecodedJWT jwt = JWT.decode(token);

            System.out.println("jwt:: "+ jwt);
            System.out.println("getClaim:: "+ jwt.getClaim(claimKey));
            System.out.println("getClaim:: "+ jwt.getClaims());
            return jwt.getClaim(claimKey);
        } catch (JWTVerificationException ex) {
            throw new RuntimeException(ex);
        }
    }
    public static void main(String[] args){
//        getClaim("company-id");
        autobox();
    }

    private static void autobox(){
        int a = new Integer(9);
        Integer b = a;

        String str = " JD ";
        System.out.print("Start");
        System.out.print(str.strip());
        System.out.println("End");

        System.out.print("Start");
        System.out.print(str.stripLeading());
        System.out.println("End");

        System.out.print("Start");
        System.out.print(str.stripTrailing());
        System.out.println("End");
    }

    private static String issuer = "auth0";
    private static String passphrase = "secret";

    public static DecodedJWT decodeToken(String token) {
        DecodedJWT jwt = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(passphrase);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            jwt = verifier.verify(token);
        } catch (TokenExpiredException e) {
            System.out.println("The Token has expired");
        } catch (SignatureVerificationException e) {
            System.out.println("The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA256");
        } catch (JWTVerificationException e){
            //Invalid signature/claims
            e.printStackTrace();
        }

        return jwt;
    }

    public static String generateToken() {
        // 10 minute
        int expireTime = 10 * 60 * 1000;

        Date exp = new Date(System.currentTimeMillis() + expireTime);
        String token = null;
        try {
            Algorithm algorithmHS = Algorithm.HMAC256(passphrase);
            token = JWT.create()
                    .withIssuer(issuer)
                    .withExpiresAt(exp)
                    .withClaim("userId", 6)
                    .withClaim("companyId", 40)
                    .withClaim("str", "str")
                    .sign(algorithmHS);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("token:: " + token);
        return token;
    }

}
