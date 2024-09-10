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
        public static final String USER_NOT_AUTHENTICATED = "User not authenticated";
    }

    public static class Role {
        public static final String ROLE_NOT_FOUND = "Role not found";
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
    public static class Product {
        public static final String PRODUCT_NOT_FOUND = "Product not found";
        public static final String PRODUCT_EXISTED = "Product existed";
        public static final String PRODUCT_NAME_EXISTED = "Product name existed";
        public static final String PRODUCT_DESCRIPTION_EXISTED = "Product description existed";
        public static final String PRODUCT_NAME_NOT_FOUND = "Product name not found";
        public static final String PRODUCT_DESCRIPTION_NOT_FOUND = "Product description not found";
        public static final String PRODUCT_NAME_NOT_EXISTED = "Product name not existed";
    }

    public static class BaseModel {
        public static final String BASE_MODEL_NOT_FOUND = "Base model not found";
    }


    public static class Review {
        public static final String REVIEW_NOT_FOUND = "Review not found";
        public static final String REVIEW_EXISTED = "Review existed";
        public static final String REVIEW_TEXT_EMPTY = "Review text is empty";
        public static final String REVIEW_RATING_INVALID = "Review rating is invalid";
        public static final String REVIEW_USER_NOT_FOUND = "Review user not found";
        public static final String REVIEW_PRODUCT_NOT_FOUND = "Review product not found";
        public static final String REVIEW_ALREADY_SUBMITTED = "Review already submitted";
    }

    public static class Order {
        public static final String ORDER_NOT_FOUND = "Order not found";
        public static final String ORDER_ALREADY_EXISTED = "Order already existed";
        public static final String ORDER_STATUS_INVALID = "Order status is invalid";
        public static final String ORDER_TOTAL_INVALID = "Order total is invalid";
        public static final String ORDER_PAYMENT_FAILED = "Order payment failed";
        public static final String ORDER_USER_NOT_FOUND = "Order user not found";
        public static final String ORDER_PRODUCT_NOT_FOUND = "Order product not found";
        public static final String ORDER_SHIPPING_ADDRESS_INVALID = "Order shipping address is invalid";
        public static final String ORDER_CANCELLATION_FAILED = "Order cancellation failed";
        public static final String NO_COMPLETED_ORDERS = "No completed orders";
    }
        public static class CustomizationOption {
            public static final String CUSTOMIZATION_OPTION_NOT_FOUND = "Customization option not found";
            public static final String CUSTOMIZATION_OPTION_EXISTED = "Customization option existed";
            public static final String CUSTOMIZATION_OPTION_NAME_EXISTED = "Customization option name existed";
            public static final String CUSTOMIZATION_OPTION_TYPE_EXISTED = "Customization option type existed";
            public static final String CUSTOMIZATION_OPTION_ADDITIONAL_PRICE_EXISTED = "Customization option additional price existed";
            public static final String CUSTOMIZATION_OPTION_NAME_NOT_FOUND = "Customization option name not found";
            public static final String CUSTOMIZATION_OPTION_TYPE_NOT_FOUND = "Customization option type not found";
            public static final String CUSTOMIZATION_OPTION_ADDITIONAL_PRICE_NOT_FOUND = "Customization option additional price not found";
            public static final String CUSTOMIZATION_OPTION_NAME_NOT_EXISTED = "Customization option name not existed";

        }

}
