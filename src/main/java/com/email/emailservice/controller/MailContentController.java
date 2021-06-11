package com.email.emailservice.controller;

import com.email.emailservice.constant.AppConstant;
import com.email.emailservice.model.MailContent;
import com.email.emailservice.model.responses.CollectionSuccessResponse;
import com.email.emailservice.model.responses.SingleSuccessResponse;
import com.email.emailservice.service.*;
import com.email.emailservice.service.dtos.MailContentRequest;
import com.email.emailservice.service.dtos.MailUpdateRequest;
import com.email.emailservice.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.naming.AuthenticationException;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.util.List;

/**
 * @author Tofazzal Hossain
 */
// Todo: Need to follow RESTful API Guidelines
// Todo: Need to provide a generic filter apis - Low priority
// Endpoint Can be like filters?filter={json-string-with-field-value}
// or
// Rules of Thumb: use PathVariable for required params and Query Param for
// all others
// GET /api/v1/mails?filedName=sender-email

@CrossOrigin
@RestController
@RequestMapping(value = "/api/" + AppConstant.API_VERSION
        + "/companies/{companyId}/users/{userId}/mails", produces = "application/json")
public class MailContentController {

    private final MailListService mailListService;
    private final MailComposeService mailComposeService;
    private final MailReceiveService mailReceiveService;
    private final MailSendingService mailSendingService;
    private final MailDeleteService mailDeleteService;
    private final MailUpdateService mailUpdateService;
    private final DownloadFileService downloadFileService;

    MailContentController(MailListService mailListService, MailComposeService mailComposeService,
            MailReceiveService mailReceiveService,
            MailSendingService mailSendingService, MailDeleteService mailDeleteService,
            MailUpdateService mailUpdateService, DownloadFileService downloadFileService) {
        this.mailListService = mailListService;
        this.mailComposeService = mailComposeService;
        this.mailReceiveService = mailReceiveService;
        this.mailSendingService = mailSendingService;
        this.mailDeleteService = mailDeleteService;
        this.mailUpdateService = mailUpdateService;
        this.downloadFileService = downloadFileService;
    }

    @ApiOperation(value = "Subscribe List of Mail")
    @GetMapping
    @MessageMapping("/push-message-mapping")
    @SendTo("/topic/push-messages")
    public ResponseEntity<CollectionSuccessResponse<MailContent>> push(@PathVariable Long companyId,
            @PathVariable Long userId, @RequestHeader("Authorization") String token) throws Exception {
        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        return ResponseEntity.ok().body(new CollectionSuccessResponse<>(AppConstant.MAIL_FETCH_SUCCESS,
                HttpStatus.OK.value(), mailReceiveService.receiveMailList(companyId, userId)));
    }

    @ApiOperation(value = "Find List of Mail")
    @GetMapping(value = "/list")
    public ResponseEntity<CollectionSuccessResponse<MailContent>> findAll(@PathVariable Long companyId,
            @PathVariable Long userId, @RequestHeader("Authorization") String token) throws AuthenticationException {
        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        return ResponseEntity.ok().body(new CollectionSuccessResponse<>(AppConstant.MAIL_FETCH_SUCCESS,
                HttpStatus.OK.value(), mailListService.findAll(companyId, userId)));
    }

    @ApiOperation(value = "Find Mail By Id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<SingleSuccessResponse<MailContent>> findById(@PathVariable Long companyId,
            @PathVariable Long userId, @PathVariable String id, @RequestHeader("Authorization") String token)
            throws Exception {
        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        return ResponseEntity.ok().body(new SingleSuccessResponse<>(AppConstant.MAIL_FETCH_SUCCESS,
                HttpStatus.OK.value(), mailListService.findById(companyId, userId, id)));
    }

    @ApiOperation(value = "Find Mail list by label name")
    @GetMapping(value = "/byName/{name}")
    public ResponseEntity<CollectionSuccessResponse<MailContent>> findByLabelName(@PathVariable Long companyId,
            @PathVariable Long userId, @PathVariable String name, @RequestHeader("Authorization") String token) throws Exception {
        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        return ResponseEntity.ok().body(new CollectionSuccessResponse<>(AppConstant.MAIL_FETCH_SUCCESS,
                HttpStatus.OK.value(), mailListService.findByLabelName(companyId, userId, name.toUpperCase())));
    }

    @ApiOperation(value = "Compose Mail")
    @PostMapping
    public ResponseEntity<SingleSuccessResponse<MailContent>> save(@PathVariable Long companyId,
            @PathVariable Long userId, @RequestPart(name = "mailContent") MailContentRequest mailContent,
            @RequestPart(name = "files", required = false) List<MultipartFile> files,
            @RequestHeader("Authorization") String token) throws Exception {
        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        return ResponseEntity.ok()
                .body(new SingleSuccessResponse<>(AppConstant.MAIL_COMPOSED_SUCCESS, HttpStatus.OK.value(),
                        mailComposeService.saveNewMailContentMetaData(companyId, userId, mailContent, files)));
    }

    @ApiOperation(value = "Send Mail")
    @PostMapping("/send")
    public ResponseEntity<SingleSuccessResponse<MailContent>> send(@PathVariable Long companyId,
            @PathVariable Long userId, @RequestPart(name = "mailContent") MailContentRequest mailContent,
            @RequestPart(name = "files", required = false) List<MultipartFile> files,
            @RequestHeader("Authorization") String token) throws Exception {
        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        return ResponseEntity.ok().body(new SingleSuccessResponse<>(AppConstant.MAIL_SENT_SUCCESS,
                HttpStatus.OK.value(), mailSendingService.sendMail(companyId, userId, mailContent, files)));
    }

    @ApiOperation(value = "Update Mail Label")
    @PatchMapping
    public ResponseEntity<SingleSuccessResponse<MailContent>> updateLabel(@PathVariable Long companyId,
            @PathVariable Long userId, @Valid @RequestBody MailUpdateRequest request,
            @RequestHeader("Authorization") String token) throws Exception {
        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        return ResponseEntity.ok().body(new SingleSuccessResponse<>(AppConstant.MAIL_LABEL_UPDATE_SUCCESS,
                HttpStatus.OK.value(), mailUpdateService.updateMailLabel(companyId, userId, request)));
    }

    @ApiOperation(value = "Delete mail")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long companyId, @PathVariable Long userId, @PathVariable String id,
            @RequestHeader("Authorization") String token) throws Exception {
        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        mailDeleteService.delete(companyId, userId, id);
        return ResponseEntity.ok().body(AppConstant.MAIL_DELETE_SUCCESS);
    }

    @ApiOperation(value = "Download File")
    @GetMapping(value = "/download")
    public ResponseEntity<?> download(@PathVariable Long companyId, @PathVariable Long userId, @RequestParam String id,
            @RequestParam String fileName, @RequestHeader("Authorization") String token) throws MalformedURLException {
        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        Resource resource = downloadFileService.download(companyId, userId, id, fileName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
