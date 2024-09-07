package com.example.kalban_greenbag.service.impl;

import com.example.kalban_greenbag.constant.ConstHashKeyPrefix;
import com.example.kalban_greenbag.dto.request.base_model.AddBaseModelRequest;
import com.example.kalban_greenbag.dto.request.base_model.UpdateBaseModelRequest;
import com.example.kalban_greenbag.dto.response.base_model.BaseModelResponse;
import com.example.kalban_greenbag.dto.response.category.CategoryResponse;
import com.example.kalban_greenbag.entity.BaseModel;
import com.example.kalban_greenbag.entity.Category;
import com.example.kalban_greenbag.enums.ErrorCode;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.repository.BaseModelRepository;
import com.example.kalban_greenbag.repository.CategoryRepository;
import com.example.kalban_greenbag.service.IBaseModelService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BaseModelServiceImpl implements IBaseModelService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BaseModelRepository baseModelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public BaseModelResponse create(AddBaseModelRequest addBaseModelRequest) throws BaseException {
        try {

            BaseModel newBaseModel = new BaseModel();
            newBaseModel.setModelName(addBaseModelRequest.getModelName());
            newBaseModel.setDescription(addBaseModelRequest.getDescription());
            newBaseModel.setBasePrice(addBaseModelRequest.getBasePrice());

            Category category = categoryRepository.findById(addBaseModelRequest.getCategoryID())
                    .orElseThrow(() -> new BaseException(ErrorCode.ERROR_404.getCode(),
                            ErrorCode.ERROR_404.getMessage(),
                            ErrorCode.ERROR_404.getMessage()));

            newBaseModel.setCategory(category);

            BaseModel savedBaseModel = baseModelRepository.save(newBaseModel);

            BaseModelResponse savedBaseModelResponse = modelMapper.map(savedBaseModel, BaseModelResponse.class);

            String hashKeyForNewBaseModel = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL + savedBaseModel.getId().toString();
            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL, hashKeyForNewBaseModel, savedBaseModelResponse);

            return savedBaseModelResponse;

        } catch (Exception exception) {
            if (exception instanceof BaseException) {
                throw exception;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(),
                    exception.getMessage(),
                    ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public BaseModelResponse update(UpdateBaseModelRequest updateBaseModelRequest) throws BaseException {
        return null;
    }

    @Override
    public Boolean changeStatus(UUID id) throws BaseException {
        return null;
    }

    @Override
    public BaseModelResponse findById(UUID id) throws BaseException {
        return null;
    }

    @Override
    public PagingModel<BaseModelResponse> getAll(Integer page, Integer limit) throws BaseException {
        return null;
    }

    @Override
    public PagingModel<BaseModelResponse> findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        return null;
    }
}
