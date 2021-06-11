package com.email.emailservice.model.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Tofazzal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleSuccessResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;
    private Integer statusCode;
    private T data;
}
