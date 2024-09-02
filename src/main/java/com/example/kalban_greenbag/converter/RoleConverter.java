package com.example.kalban_greenbag.converter;

import com.example.kalban_greenbag.dto.request.role.RoleRequest;
import com.example.kalban_greenbag.dto.response.role.RoleResponse;
import com.example.kalban_greenbag.entity.Role;

public class RoleConverter {
    public static RoleResponse entityToResponse(Role role) {

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(role.getId());
        roleResponse.setRoleName(role.getRoleName());
        roleResponse.setDescription(role.getDescription());
        roleResponse.setStatus(role.getStatus());
        roleResponse.setCreatedDate(role.getCreatedDate());
        roleResponse.setModifiedDate(role.getModifiedDate());
        roleResponse.setModifiedBy(role.getModifiedBy());
        roleResponse.setCreatedBy(role.getCreatedBy());

        return roleResponse;

    }

    public static Role responseToEntity(RoleResponse roleResponse) {

        Role role = new Role();
        role.setId(roleResponse.getId());
        role.setRoleName(roleResponse.getRoleName());
        role.setDescription(roleResponse.getDescription());
        role.setStatus(roleResponse.getStatus());
        role.setCreatedDate(roleResponse.getCreatedDate());
        role.setModifiedDate(roleResponse.getModifiedDate());
        role.setModifiedBy(roleResponse.getModifiedBy());
        role.setCreatedBy(roleResponse.getCreatedBy());

        return role;
    }
}
