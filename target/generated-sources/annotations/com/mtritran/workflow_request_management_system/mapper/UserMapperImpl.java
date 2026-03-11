package com.mtritran.workflow_request_management_system.mapper;

import com.mtritran.workflow_request_management_system.dto.request.UserCreationRequest;
import com.mtritran.workflow_request_management_system.dto.request.UserUpdateRequest;
import com.mtritran.workflow_request_management_system.dto.response.UserResponse;
import com.mtritran.workflow_request_management_system.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-11T22:34:59+0700",
    comments = "version: 1.6.0, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.dob( request.getDob() );
        user.firstname( request.getFirstname() );
        user.lastname( request.getLastname() );
        user.password( request.getPassword() );
        user.username( request.getUsername() );

        return user.build();
    }

    @Override
    public void updateUser(User user, UserUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        if ( request.getDob() != null ) {
            user.setDob( request.getDob() );
        }
        if ( request.getFirstname() != null ) {
            user.setFirstname( request.getFirstname() );
        }
        if ( request.getLastname() != null ) {
            user.setLastname( request.getLastname() );
        }
        if ( request.getPassword() != null ) {
            user.setPassword( request.getPassword() );
        }
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( user.getId() );
        userResponse.username( user.getUsername() );
        userResponse.firstname( user.getFirstname() );
        userResponse.lastname( user.getLastname() );
        userResponse.dob( user.getDob() );
        userResponse.roles( mapRoles( user.getRoles() ) );

        return userResponse.build();
    }
}
