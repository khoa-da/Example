package com.example.kalban_greenbag.service;

import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;

import java.util.UUID;

public interface IGenericService<T> {
    T findById(UUID id) throws BaseException;

    PagingModel<T> getAll(Integer page, Integer limit) throws BaseException;

    PagingModel<T> findAllByStatusTrue(Integer page, Integer limit) throws BaseException;
}
