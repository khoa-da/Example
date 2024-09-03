package com.example.kalban_greenbag.converter;

import com.example.kalban_greenbag.constant.ConstError;
import com.example.kalban_greenbag.dto.response.user.UserResponse;
import com.example.kalban_greenbag.entity.Role;
import com.example.kalban_greenbag.entity.User;
import com.example.kalban_greenbag.enums.ErrorCode;
import com.example.kalban_greenbag.exception.BaseException;

public class UserConverter {
    public static UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setCreatedDate(user.getCreatedDate());
        userResponse.setModifiedDate(user.getModifiedDate());
        userResponse.setCreatedBy(user.getCreatedBy());
        userResponse.setModifiedBy(user.getModifiedBy());
        userResponse.setStatus(user.getStatus());
        userResponse.setEmail(user.getEmail());
        userResponse.setRoleName(RoleConverter.entityToResponse(user.getRole()).getRoleName());

        return userResponse;
    }

    public static User toEntity(UserResponse userResponse) throws BaseException {
        if (userResponse == null) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Input.NO_INPUT, ErrorCode.ERROR_500.getMessage());
        }

        User user = new User();
        user.setId(userResponse.getId());
        user.setUsername(userResponse.getUsername());
        user.setCreatedDate(userResponse.getCreatedDate());
        user.setModifiedDate(userResponse.getModifiedDate());
        user.setCreatedBy(userResponse.getCreatedBy());
        user.setModifiedBy(userResponse.getModifiedBy());
        user.setStatus(userResponse.getStatus());
        user.setEmail(userResponse.getEmail());

//        // Convert role from UserResponse to Role entity
//        Role role = RoleConverter.responseToEntity(userResponse.getRole());
//        user.setRole(role);
        //thieu role, su dung thi phai call ham getRoleByName, neu k dung thi se bi null

        return user;
    }

}
