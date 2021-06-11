package com.email.emailservice.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class FileReadWriteException extends AbstractThrowableProblem {

    private static final URI TYPE
            = URI.create("https://example.org/not-found");

    public FileReadWriteException(String message) {
        super(
                TYPE,
                Status.NOT_ACCEPTABLE.name(),
                Status.NOT_ACCEPTABLE,
                message
        );
    }
}
