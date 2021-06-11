package com.email.emailservice.service;

import org.springframework.core.io.Resource;

import java.net.MalformedURLException;

public interface DownloadFileService {
    Resource download(Long companyId, Long userId, String id, String fileName) throws MalformedURLException;
}
