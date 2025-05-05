package ru.projects.product_service.exception;

public class RolePermissionExceprion extends RuntimeException {
    public RolePermissionExceprion(String message) {
        super(message);
    }
}
