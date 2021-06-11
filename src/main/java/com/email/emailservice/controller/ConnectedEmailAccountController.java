package com.email.emailservice.controller;

import com.email.emailservice.constant.AppConstant;
import com.email.emailservice.model.responses.CollectionSuccessResponse;
import com.email.emailservice.model.responses.SingleSuccessResponse;
import com.email.emailservice.service.ConnectedEmailAccountService;
import com.email.emailservice.service.dtos.ConnectedEmailAccountDto;
import com.email.emailservice.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/" + AppConstant.API_VERSION
        + "/companies/{companyId}/users/{userId}/email-accounts", produces = "application/json")
public class ConnectedEmailAccountController {

    private final ConnectedEmailAccountService connectedEmailAccountService;

    public ConnectedEmailAccountController(ConnectedEmailAccountService connectedEmailAccountService) {
        this.connectedEmailAccountService = connectedEmailAccountService;
    }

    @ApiOperation(value = "Save Email Account")
    @PostMapping
    public ResponseEntity<SingleSuccessResponse<ConnectedEmailAccountDto>> save(
            @Valid @RequestBody ConnectedEmailAccountDto emailAccountsDto, @PathVariable Long companyId,
            @PathVariable Long userId, @RequestHeader("Authorization") String token) throws Exception {

        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }

        if (!emailAccountsDto.isVerificationForcedByUser()
                && connectedEmailAccountService.isEmailExists(emailAccountsDto.getEmail(), companyId)) {
            return ResponseEntity.accepted()
                    .body(new SingleSuccessResponse<>(
                            "email '" + emailAccountsDto.getEmail() + "' is already exists!! Do you want to proceed?",
                            202, null));
        }

        ConnectedEmailAccountDto emailAccount = connectedEmailAccountService.save(emailAccountsDto, companyId, userId);
        return ResponseEntity
                .created(URI.create("/api/" + AppConstant.API_VERSION + "/companies/" + companyId + "/users/" + userId
                        + "/email-accounts"))
                .body(new SingleSuccessResponse<>(AppConstant.EMAIL_ACCOUNTS_SAVE_SUCCESS, 201, emailAccount));
    }

    @ApiOperation(value = "Fetch Email Account")
    @GetMapping
    public ResponseEntity<CollectionSuccessResponse<ConnectedEmailAccountDto>> findAll(
            @PathVariable Long companyId, @PathVariable Long userId,
            @RequestHeader("Authorization") String token) {
        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        return ResponseEntity.ok().body(new CollectionSuccessResponse<>(AppConstant.EMAIL_ACCOUNTS_LIST_FETCH_SUCCESS,
                HttpStatus.OK.value(), connectedEmailAccountService.findAll(companyId, userId)));
    }

    @ApiOperation(value = "Find Email Account By Id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<SingleSuccessResponse<ConnectedEmailAccountDto>> findEmailAccount(
            @PathVariable String id, @PathVariable Long companyId, @PathVariable Long userId,
            @RequestHeader("Authorization") String token) throws Exception {

        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        return ResponseEntity.ok().body(new SingleSuccessResponse<>(AppConstant.EMAIL_ACCOUNTS_FETCH_SUCCESS,
                HttpStatus.OK.value(),
                connectedEmailAccountService.findByIdForCompanyAndUserId(id, companyId, userId)));
    }

    @ApiOperation(value = "Update Email Account")
    @PutMapping(value = "/{id}")
    public ResponseEntity<SingleSuccessResponse<ConnectedEmailAccountDto>> update(
            @Valid @RequestBody ConnectedEmailAccountDto emailAccountsDto,
            @PathVariable String id, @PathVariable Long companyId, @PathVariable Long userId,
            @RequestHeader("Authorization") String token) throws Exception {

        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        return ResponseEntity.ok().body(new SingleSuccessResponse<>(AppConstant.EMAIL_ACCOUNTS_UPDATE_SUCCESS,
                HttpStatus.OK.value(), connectedEmailAccountService.update(emailAccountsDto, id, companyId, userId)));
    }

    @ApiOperation(value = "Delete Email Account")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(
            @PathVariable String id, @PathVariable Long companyId, @PathVariable Long userId,
            @RequestHeader("Authorization") String token) {

        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        connectedEmailAccountService.delete(id, companyId, userId);
        return ResponseEntity.ok().body(AppConstant.EMAIL_ACCOUNTS_DELETE_SUCCESS);
    }

    @ApiOperation(value = "Verify Email Account")
    @GetMapping(value = "/verify")
    public ResponseEntity<String> Verify(
            @RequestParam String token) throws Exception {
        connectedEmailAccountService.verify(token);
        return ResponseEntity.ok().body(AppConstant.VERIFY_EMAIL_SUCCESS_MESSAGE);
    }

}
