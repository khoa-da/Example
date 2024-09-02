package com.example.kalban_greenbag.service.impl;

import com.example.kalban_greenbag.constant.ConstError;
import com.example.kalban_greenbag.converter.RoleConverter;
import com.example.kalban_greenbag.dto.response.role.RoleResponse;
import com.example.kalban_greenbag.entity.Role;
import com.example.kalban_greenbag.enums.ErrorCode;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.repository.RoleRepository;
import com.example.kalban_greenbag.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public RoleResponse findByName(String name) throws BaseException {
        Optional<Role> roleById = roleRepository.findByRoleName(name);
        boolean isExist = roleById.isPresent();

        if (!isExist) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Role.ROLE_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
        }

        return RoleConverter.entityToResponse(roleById.get());
    }
}
