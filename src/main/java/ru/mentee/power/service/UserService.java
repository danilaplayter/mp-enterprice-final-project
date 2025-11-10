/* @MENTEE_POWER (C)2025 */
package ru.mentee.power.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.api.generated.dto.UserPageResponse;
import ru.mentee.power.api.generated.dto.UserRegistrationRequest;
import ru.mentee.power.api.generated.dto.UserResponse;
import ru.mentee.power.domain.exception.UserNotFoundException;
import ru.mentee.power.domain.mapper.UserMapper;
import ru.mentee.power.domain.model.User;
import ru.mentee.power.repository.UserRepository;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository userRepository;
    public final UserMapper userMapper;

    public UserResponse registrateUser(UserRegistrationRequest registrationRequest) {
        log.info("Registering new user with email: {}", registrationRequest.getEmail());

        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new IllegalArgumentException();
        }

        User user = userMapper.toUser(registrationRequest);
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        return userMapper.toUserResponse(savedUser);
    }

    public void deleteUserById(UUID id) {
        log.info("Deleting user with ID: {}", id);

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    public UserResponse getUserById(UUID id) {
        log.info("Fetching user with ID: {}", id);

        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new UserNotFoundException(
                                                "User with ID " + id + " not found"));

        return userMapper.toUserResponse(user);
    }

    public UserPageResponse findAllUsers(int page, int size) {
        log.info("Fetching all users - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> userPage = userRepository.findAll(pageable);

        return userMapper.toUserPageResponse(userPage);
    }
}
