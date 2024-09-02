package com.example.kalban_greenbag.service;

import com.example.kalban_greenbag.dto.response.role.RoleResponse;
import com.example.kalban_greenbag.exception.BaseException;

public interface IRoleService {
    RoleResponse findByName(String name) throws BaseException;
}
