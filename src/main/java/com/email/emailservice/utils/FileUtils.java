package com.email.emailservice.utils;

import com.email.emailservice.constant.AppConstant;
import com.email.emailservice.exceptions.FileReadWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
    }

    public static List<String> getFileNames(List<?> files) {
        List<String> fileNames = new ArrayList<>();
        if(files != null) {
            files.forEach(file -> {
                String fileName = StringUtils.cleanPath(((MultipartFile) file).getOriginalFilename());
                fileNames.add(fileName);
            });
        }
        return fileNames;
    }

    public static Multipart buildMultipart(List<MultipartFile> files, String FILE_ATTACHMENT_PATH){
        Multipart multipart = new MimeMultipart();
        File dir = new File(FILE_ATTACHMENT_PATH + File.separator);

        if(files != null) {
            files.forEach(file -> {
                try {
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    Path filePath = Paths.get(dir.getAbsolutePath() + File.separator + fileName);
                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(filePath.toFile());
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(fileName);
                    multipart.addBodyPart(messageBodyPart);
                } catch (MessagingException e) {
                    LOGGER.info(AppConstant.MAIL_EXCEPTION + e.getLocalizedMessage());
                    throw new UnsupportedMediaTypeStatusException(e.getLocalizedMessage());
                }
            });
        }
        return multipart;
    }

    public static void saveFiles(List<?> files, String FILE_ATTACHMENT_PATH) throws Exception {
        List<String> fileNames = new ArrayList<>();
        File dir = new File(FILE_ATTACHMENT_PATH + File.separator);

        Files.createDirectories(dir.toPath());

        if (files != null) {
            files.forEach(file -> {
                try {
                    byte[] bytes = new byte[file.toString().length()];
                    String fileName = "";
                    if (file instanceof BodyPart) {
                        fileName = StringUtils.cleanPath(((BodyPart) file).getFileName());
                        bytes = ((BodyPart) file).getContent().toString().getBytes();

                    } else if (file instanceof MultipartFile) {
                        bytes = ((MultipartFile) file).getBytes();
                        fileName = StringUtils.cleanPath(((MultipartFile) file).getOriginalFilename());
                    }

                    fileNames.add(fileName);
                    File serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();
                } catch (IOException e) {
                    LOGGER.info(AppConstant.MAIL_EXCEPTION + e.getLocalizedMessage());
                    throw new FileReadWriteException(e.getLocalizedMessage());
                } catch (MessagingException e) {
                    LOGGER.info(AppConstant.MAIL_EXCEPTION + e.getLocalizedMessage());
                    throw new UnsupportedMediaTypeStatusException(e.getLocalizedMessage());
                }
            });
            LOGGER.info(AppConstant.MAIL_FILE_SAVED_SUCCESS);
        }
    }

    public static void deleteFiles(List<String> fileNames, String FILE_ATTACHMENT_PATH) {
        fileNames.forEach(fileName -> {
            File file = new File(FILE_ATTACHMENT_PATH + File.separator + fileName);
            file.delete();
        });
    }
}
