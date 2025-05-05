package ru.projects.product_service.exception;

public class AttributeNotFoundException extends RuntimeException {
    public AttributeNotFoundException(String message) {
        super(message);
    }
}
