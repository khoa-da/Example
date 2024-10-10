//package com.example.kalban_greenbag.controller;
//
////import com.example.kalban_greenbag.config.RedisConfig;
//import com.example.kalban_greenbag.constant.ConstAPI;
//import com.example.kalban_greenbag.exception.BaseException;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@CrossOrigin
//@RestController
//@Slf4j
//@Tag(name = "Cache Controller")
//public class CacheController {
//
//    @Autowired
//    private RedisConfig redisConfig;
//
//    @Operation(summary = "Clear Cache", description = "API to clear cache by name")
//    @DeleteMapping(value = ConstAPI.CacheAPI.CLEAR_CACHE)
//    public String clearAllCaches() throws BaseException {
//        try {
//            redisConfig.clearAllCaches();
//            return "All caches cleared successfully.";
//        } catch (Exception e) {
//            throw new BaseException(500, e.getMessage(), "Failed to clear caches");
//        }
//    }
//}
