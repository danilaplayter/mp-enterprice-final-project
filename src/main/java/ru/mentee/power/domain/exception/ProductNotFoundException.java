/* @MENTEE_POWER (C)2025 */
package ru.mentee.power.domain.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}
