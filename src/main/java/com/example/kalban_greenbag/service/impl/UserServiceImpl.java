package com.example.kalban_greenbag.service.impl;

import com.example.kalban_greenbag.constant.ConstError;
import com.example.kalban_greenbag.constant.ConstStatus;
import com.example.kalban_greenbag.dto.request.user.LoginRequest;
import com.example.kalban_greenbag.dto.response.JwtAuthenticationResponse;
import com.example.kalban_greenbag.dto.response.user.UserResponse;
import com.example.kalban_greenbag.entity.User;
import com.example.kalban_greenbag.enums.ErrorCode;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.repository.UserRepository;
import com.example.kalban_greenbag.service.IJWTService;
import com.example.kalban_greenbag.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IJWTService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse findById(int id) throws BaseException {
        return null;
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) throws BaseException {
        return null;
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        return null;
    }

    @Override
    public JwtAuthenticationResponse login(LoginRequest loginRequest) throws BaseException {
        User user = findByEmailOrUserName(loginRequest.getEmailOrUsername());
        // Check if the account is disabled
        if (user.getStatus().equals(ConstStatus.INACTIVE_STATUS)) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), "User is disabled", ErrorCode.ERROR_500.getMessage());
        }
//        if (!user.getRole().getName().equals(loginRequest.getLoginWithRole())) {
//            throw new BaseException(ErrorCode.ERROR_500.getCode(), "Role is not match", ErrorCode.ERROR_500.getMessage());
//        }

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.getPassword()));

        // Set the authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return MappingjwtAuthenticationRespone(user);
    }

    private JwtAuthenticationResponse MappingjwtAuthenticationRespone(User user) throws BaseException {
        JwtAuthenticationResponse jwtAuthenticationRespone = new JwtAuthenticationResponse();

        jwtAuthenticationRespone.setId(user.getId());



        jwtAuthenticationRespone.setUsername(user.getUsername());

        jwtAuthenticationRespone.setStatus(user.getStatus());
        jwtAuthenticationRespone.setEmail(user.getEmail());
        jwtAuthenticationRespone.setRole(user.getRole());

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        jwtAuthenticationRespone.setToken(jwt);
        jwtAuthenticationRespone.setRefreshToken(refreshToken);
        return jwtAuthenticationRespone;
    }
    private User findByEmailOrUserName(String emailOrUserName) throws BaseException {
        try {

            logger.info("Find user by email or username {}", emailOrUserName);

            Optional<User> userById;
            userById = userRepository.findByEmail(emailOrUserName);
            if (!userById.isPresent()) {
                userById = userRepository.findByUsername(emailOrUserName);
            }

            boolean isAccountExist = userById.isPresent();

            if (!isAccountExist) {
                logger.warn("Account with email or username {} is not found", emailOrUserName);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.User.USER_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }



            return userById.get();
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }
}
