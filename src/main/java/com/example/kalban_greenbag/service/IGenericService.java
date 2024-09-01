package com.example.kalban_greenbag.service;

import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;

public interface IGenericService<T> {
    T findById(int id) throws BaseException;

    PagingModel getAll(Integer page, Integer limit) throws BaseException;

    PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException;
}
