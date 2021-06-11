package com.email.emailservice.controller;

import com.email.emailservice.constant.AppConstant;
import com.email.emailservice.model.Label;
import com.email.emailservice.model.responses.CollectionSuccessResponse;
import com.email.emailservice.model.responses.SingleSuccessResponse;
import com.email.emailservice.service.LabelService;
import com.email.emailservice.service.dtos.LabelUpdateRequest;
import com.email.emailservice.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/" + AppConstant.API_VERSION
        + "/companies/{companyId}/users/{userId}/labels", produces = "application/json")
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @ApiOperation(value = "Find list of all labels")
    @GetMapping
    public ResponseEntity<CollectionSuccessResponse<Label>> findAll(@PathVariable Long companyId,
            @PathVariable Long userId, @RequestHeader("Authorization") String token) {
        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        return ResponseEntity.ok().body(new CollectionSuccessResponse<>(AppConstant.LABEL_FETCH_SUCCESS,
                HttpStatus.OK.value(), labelService.findAll(companyId, userId)));
    }

    @ApiOperation(value = "Save label")
    @PostMapping(value = "/{name}")
    public ResponseEntity<SingleSuccessResponse<Label>> save(@PathVariable Long companyId, @PathVariable Long userId,
            @PathVariable String name, @RequestHeader("Authorization") String token) throws Exception {
        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        return ResponseEntity.ok().body(new SingleSuccessResponse<>(AppConstant.LABEL_SAVE_SUCCESS,
                HttpStatus.OK.value(), labelService.save(companyId, userId, name)));
    }

    @ApiOperation(value = "Update label")
    @PatchMapping
    public ResponseEntity<SingleSuccessResponse<Label>> update(@PathVariable Long companyId, @PathVariable Long userId,
            @Valid @RequestBody LabelUpdateRequest request, @RequestHeader("Authorization") String token)
            throws Exception {
        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        return ResponseEntity.ok().body(new SingleSuccessResponse<>(AppConstant.LABEL_UPDATE_SUCCESS,
                HttpStatus.OK.value(), labelService.update(companyId, userId, request)));
    }

    @ApiOperation(value = "Delete label")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long companyId, @PathVariable Long userId,
            @PathVariable String id, @RequestHeader("Authorization") String token) {
        if (!SecurityUtils.isAccessAllowd(token, companyId, userId)) {
            throw Problem.builder().withDetail(AppConstant.UNAUTHORIZED_RESOURCES).withStatus(Status.FORBIDDEN)
                    .withTitle(Status.FORBIDDEN.name()).build();
        }
        labelService.delete(companyId, userId, id);
        return ResponseEntity.ok().body(AppConstant.LABEL_DELETE_SUCCESS);
    }
}
