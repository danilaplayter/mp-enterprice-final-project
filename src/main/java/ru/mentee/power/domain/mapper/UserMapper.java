/* @MENTEE_POWER (C)2025 */
package ru.mentee.power.domain.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.mentee.power.api.generated.dto.UserPageResponse;
import ru.mentee.power.api.generated.dto.UserRegistrationRequest;
import ru.mentee.power.api.generated.dto.UserResponse;
import ru.mentee.power.domain.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "createdAt", expression = "java(java.time.OffsetDateTime.now())")
    User toUser(UserRegistrationRequest registrationRequest);

    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);

    default UserPageResponse toUserPageResponse(Page<User> userPage) {
        UserPageResponse response = new UserPageResponse();
        response.setContent(toUserResponseList(userPage.getContent()));
        response.setPage(userPage.getNumber());
        response.setSize(userPage.getSize());
        response.setTotalElements(userPage.getTotalElements());
        response.setTotalPages(userPage.getTotalPages());
        return response;
    }
}
