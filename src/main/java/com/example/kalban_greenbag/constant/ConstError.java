package com.example.kalban_greenbag.constant;

public class ConstError {
    public static class Input{
        public static final String NO_INPUT = "No input";
    }
    public static class User {
        public static final String USER_NOT_FOUND = "User not found";
        public static final String USERNAME_EXISTED = "Username existed";
        public static final String EMAIL_EXISTED = "Email existed";
        public static final String INVALID_AUTHORIZATION_HEADER = "Invalid authorization header";
    }

    public static class Category {
        public static final String CATEGORY_NOT_FOUND = "Category not found";
        public static final String CATEGORY_EXISTED = "Category existed";
        public static final String CATEGORY_NAME_EXISTED = "Category name existed";
        public static final String CATEGORY_DESCRIPTION_EXISTED = "Category description existed";
        public static final String CATEGORY_NAME_NOT_FOUND = "Category name not found";
        public static final String CATEGORY_DESCRIPTION_NOT_FOUND = "Category description not found";
        public static final String CATEGORY_NAME_NOT_EXISTED = "Category name not existed";

    }

    public static class Role {
        public static final String ROLE_NOT_FOUND = "Role not found";
    }
}
