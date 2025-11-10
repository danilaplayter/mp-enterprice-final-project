/* @MENTEE_POWER (C)2025 */
package ru.mentee.power.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.mentee.power.api.generated.controller.UsersApi;
import ru.mentee.power.api.generated.dto.UserPageResponse;
import ru.mentee.power.api.generated.dto.UserRegistrationRequest;
import ru.mentee.power.api.generated.dto.UserResponse;
import ru.mentee.power.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponse> registerUser(
            UserRegistrationRequest userRegistrationRequest) {
        log.info(
                "Received request to register new user with email: {}",
                userRegistrationRequest.getEmail());

        UserResponse userResponse = userService.registrateUser(userRegistrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @Override
    public ResponseEntity<UserPageResponse> getAllUsers(Integer page, Integer size) {
        log.info("Received request to get all users - page: {}, size: {}", page, size);

        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        UserPageResponse userPageResponse = userService.findAllUsers(pageNumber, pageSize);

        return ResponseEntity.ok(userPageResponse);
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(UUID userId) {
        log.info("Received request to get user by ID: {}", userId);

        UserResponse userResponse = userService.getUserById(userId);

        return ResponseEntity.ok(userResponse);
    }
}
