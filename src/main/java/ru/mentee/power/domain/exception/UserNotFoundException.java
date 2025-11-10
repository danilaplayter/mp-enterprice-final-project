/* @MENTEE_POWER (C)2025 */
package ru.mentee.power.domain.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
