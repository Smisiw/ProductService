package ru.projects.product_service.exception;

public class ProductReservationException extends RuntimeException {
    public ProductReservationException(String message) {
        super(message);
    }
}
