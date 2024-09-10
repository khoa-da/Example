package com.example.kalban_greenbag.constant;

public class ConstAPI {
    public static class AuthenticationAPI {
        public static final String LOGIN_WITH_PASSWORD_USERNAME = "api/v1/auth/login";
        public static final String LOGIN_WITH_GOOGLE = "api/v1/auth/login-google";
    }

    public static class UserAPI {

        public static final String CREATE_ACCOUNT = "api/v1/account/create";
        public static final String GET_ACCOUNT_BY_ID = "api/v1/account/";
        public static final String GET_ALL_ACCOUNT = "api/v1/account";
        public static  final String GET_ALL_ACCOUNT_ACTIVE = "api/v1/account-active";


    }
    public static class ProductAPI {
        public static final String CREATE_PRODUCT = "api/v1/product/create";
        public static final String UPDATE_PRODUCT = "api/v1/product/update";
        public static final String DELETE_PRODUCT = "api/v1/product/delete/";
        public static final String GET_PRODUCT_BY_ID = "api/v1/product/";
        public static final String GET_ALL_PRODUCT = "api/v1/product";
        public static final String GET_ALL_PRODUCT_STATUS_TRUE = "api/v1/product/product-status-active";
    }

    public static class CategoryAPI {

        public static final String  CREATE_CATEGORY = "api/v1/category/create";
        public static final String  UPDATE_CATEGORY = "api/v1/category/update";
        public static final String  DELETE_CATEGORY = "api/v1/category/delete/";
        public static final String  GET_CATEGORY_BY_ID = "api/v1/category/";
        public static final String  GET_ALL_CATEGORY = "api/v1/category";
        public static final String  GET_ALL_CATEGORY_STATUS_TRUE = "api/v1/category/category-status-active";

    }

    public static class BaseModelAPI {

        public static final String CREATE_BASE_MODEL = "api/v1/base-model/create";
        public static final String UPDATE_BASE_MODEL = "api/v1/base-model/update";
        public static final String DELETE_BASE_MODEL = "api/v1/base-model/delete/";
        public static final String GET_BASE_MODEL_BY_ID = "api/v1/base-model/";
        public static final String GET_ALL_BASE_MODELS = "api/v1/base-model";
        public static final String GET_ALL_BASE_MODELS_BY_CATEGORY = "api/v1/base-model/category/";
        public static final String GET_ALL_BASE_MODELS_STATUS_ACTIVE = "api/v1/base-model/base-model-status-active";

    }


    public static class ReviewAPI{
        public static final String CREATE_REVIEW = "api/v1/review/create";
        public static final String UPDATE_REVIEW = "api/v1/review/update";
        public static final String DELETE_REVIEW = "api/v1/review/delete/";
        public static final String GET_REVIEW_BY_ID = "api/v1/review/";
        public static final String GET_ALL_REVIEWS = "api/v1/review";
        public static final String GET_REVIEWS_BY_PRODUCT_ID = "api/v1/review/product/";
        public static final String GET_ALL_REVIEWS_STATUS_ACTIVE = "api/v1/review/review-status-active";

    public static class CustomizationOptionAPI {
        public static final String CREATE_CUSTOMIZATION_OPTION = "api/v1/customization-option/create";
        public static final String UPDATE_CUSTOMIZATION_OPTION = "api/v1/customization-option/update";
        public static final String DELETE_CUSTOMIZATION_OPTION = "api/v1/customization-option/delete/";
        public static final String GET_CUSTOMIZATION_OPTION_BY_ID = "api/v1/customization-option/";
        public static final String GET_ALL_CUSTOMIZATION_OPTION = "api/v1/customization-option";
        public static final String GET_ALL_CUSTOMIZATION_OPTION_STATUS_TRUE = "api/v1/customization-option/customization-option-status-active";
    }

}
