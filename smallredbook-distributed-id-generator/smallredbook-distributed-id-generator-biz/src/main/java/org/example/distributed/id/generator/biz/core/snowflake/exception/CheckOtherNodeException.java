package org.example.distributed.id.generator.biz.core.snowflake.exception;

public class CheckOtherNodeException extends RuntimeException {
    public CheckOtherNodeException(String message) {
        super(message);
    }
}
