package com.email.emailservice.model.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tofazzal
 * @apiNote Tofazzal bhai please add Paginated response when it is ready.
 * properties are already there.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionSuccessResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;
    private Integer statusCode;
    private List<T> data = new ArrayList<>();

    // Integer page;
    // Integer size;
    // Integer totalPages;
    // Long totalRecord;
}
