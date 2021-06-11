package com.email.emailservice.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class NotFoundException extends AbstractThrowableProblem {

    private static final URI TYPE
            = URI.create("https://locahost");

    public NotFoundException(String message) {
        super(
                TYPE,
                Status.NOT_FOUND.name(),
                Status.NOT_FOUND,
                message);
    }
}
