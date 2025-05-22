package ru.projects.product_service.exception;

public class NotRelevantProductInfoException extends RuntimeException {
    public NotRelevantProductInfoException(String message) {
        super(message);
    }
}
