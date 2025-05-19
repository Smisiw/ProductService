package ru.projects.product_service.exception;

public class AttributeAlreadyExistException extends RuntimeException {
    public AttributeAlreadyExistException(String message) {
        super(message);
    }
}
