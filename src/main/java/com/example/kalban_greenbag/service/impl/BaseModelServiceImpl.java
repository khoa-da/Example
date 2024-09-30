package com.example.kalban_greenbag.service.impl;

import com.example.kalban_greenbag.constant.ConstError;
import com.example.kalban_greenbag.constant.ConstHashKeyPrefix;
import com.example.kalban_greenbag.constant.ConstStatus;
import com.example.kalban_greenbag.dto.request.base_model.AddBaseModelRequest;
import com.example.kalban_greenbag.dto.request.base_model.UpdateBaseModelRequest;
import com.example.kalban_greenbag.dto.response.base_model.BaseModelResponse;
import com.example.kalban_greenbag.entity.BaseModel;
import com.example.kalban_greenbag.entity.Category;
import com.example.kalban_greenbag.enums.ErrorCode;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.repository.BaseModelRepository;
import com.example.kalban_greenbag.repository.CategoryRepository;
import com.example.kalban_greenbag.service.IBaseModelService;
import com.example.kalban_greenbag.utils.SecurityUtil;
import com.example.kalban_greenbag.utils.ValidateUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public int totalItem() {
        return (int) baseModelRepository.count();
    }


    @Override
    public BaseModelResponse create(AddBaseModelRequest addBaseModelRequest) throws BaseException {
        try {
            String username = SecurityUtil.getCurrentUsername();
            BaseModel newBaseModel = new BaseModel();
            newBaseModel.setModelName(addBaseModelRequest.getModelName());
            newBaseModel.setDescription(addBaseModelRequest.getDescription());
            newBaseModel.setBasePrice(addBaseModelRequest.getBasePrice());
            Category category = categoryRepository.findById(addBaseModelRequest.getCategoryID())
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_404.getCode(),
                            ConstError.BaseModel.BASE_MODEL_NOT_FOUND,
                            ErrorCode.ERROR_404.getMessage()
                    ));

            newBaseModel.setCategory(category);
            newBaseModel.setCreatedBy(username);
            newBaseModel.setImageURL(addBaseModelRequest.getImageURL());
            newBaseModel.setStatus(addBaseModelRequest.getStatus());
            BaseModel savedBaseModel = baseModelRepository.save(newBaseModel);
            BaseModelResponse savedBaseModelResponse = modelMapper.map(savedBaseModel, BaseModelResponse.class);

            String pattern = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL + "*";
            Set<String> keysToDelete = redisTemplate.keys(pattern);
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

            return savedBaseModelResponse;

        } catch (Exception exception) {
            if (exception instanceof BaseException) {
                throw exception;
            }
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    exception.getMessage(),
                    ErrorCode.ERROR_500.getMessage()
            );
        }
    }

    @Override
    public BaseModelResponse update(UpdateBaseModelRequest updateBaseModelRequest) throws BaseException {
        try {
            String username = SecurityUtil.getCurrentUsername();

            BaseModel baseModel = baseModelRepository.findById(updateBaseModelRequest.getId())
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_500.getCode(),
                            ConstError.BaseModel.BASE_MODEL_NOT_FOUND,
                            ErrorCode.ERROR_500.getMessage()
                    ));

            if (updateBaseModelRequest.getModelName() != null) {
                baseModel.setModelName(updateBaseModelRequest.getModelName());
            }
            if (updateBaseModelRequest.getDescription() != null) {
                baseModel.setDescription(updateBaseModelRequest.getDescription());
            }
            if (updateBaseModelRequest.getBasePrice() != null) {
                baseModel.setBasePrice(updateBaseModelRequest.getBasePrice());
            }
            if (updateBaseModelRequest.getImageURL() != null) {
                baseModel.setImageURL(updateBaseModelRequest.getImageURL());
            }
            if (updateBaseModelRequest.getCategoryID() != null) {
                Category category = categoryRepository.findById(updateBaseModelRequest.getCategoryID())
                        .orElseThrow(() -> new BaseException(
                                ErrorCode.ERROR_500.getCode(),
                                ConstError.Category.CATEGORY_NOT_FOUND,
                                ErrorCode.ERROR_500.getMessage()
                        ));
                baseModel.setCategory(category);
            }

            baseModel.setModifiedBy(username);

            BaseModel updatedBaseModel = baseModelRepository.save(baseModel);

            BaseModelResponse updatedBaseModelResponse = modelMapper.map(updatedBaseModel, BaseModelResponse.class);

            String pattern = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL + "*";
            Set<String> keysToDelete = redisTemplate.keys(pattern);
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

            return updatedBaseModelResponse;

        } catch (Exception exception) {
            if (exception instanceof BaseException) {
                throw exception;
            }
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    exception.getMessage(),
                    ErrorCode.ERROR_500.getMessage()
            );
        }
    }


    @Override
    public Boolean changeStatus(UUID id) throws BaseException {
        try {
            String modifiedBy = SecurityUtil.getCurrentUsername();
            BaseModel baseModel = baseModelRepository.findById(id)
                    .orElseThrow(() -> new BaseException(ErrorCode.ERROR_500.getCode(),
                            ConstError.BaseModel.BASE_MODEL_NOT_FOUND,
                            ErrorCode.ERROR_500.getMessage()));

            baseModel.setStatus(ConstStatus.INACTIVE_STATUS);
            baseModel.setModifiedBy(modifiedBy);
            baseModelRepository.save(baseModel);

            String pattern = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL + "*";
            Set<String> keysToDelete = redisTemplate.keys(pattern);
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

            return true;

        } catch (Exception e) {
            if (e instanceof BaseException) {
                throw e;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(),
                    e.getMessage(),
                    ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public BaseModelResponse findById(UUID id) throws BaseException {
        try {
            String hashKeyForModel = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL + id.toString();

            BaseModelResponse baseModelResponseByRedis = (BaseModelResponse) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL, hashKeyForModel);
            if (baseModelResponseByRedis != null) {
                return baseModelResponseByRedis;
            }
            Optional<BaseModel> modelOptional = baseModelRepository.findById(id);

            if (!modelOptional.isPresent()) {
                throw new BaseException(ErrorCode.ERROR_404.getCode(),
                        ConstError.BaseModel.BASE_MODEL_NOT_FOUND,
                        ErrorCode.ERROR_404.getMessage());
            }

            BaseModel model = modelOptional.get();
            BaseModelResponse baseModelResponse = modelMapper.map(model, BaseModelResponse.class);
            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL, hashKeyForModel, baseModelResponse);

            return baseModelResponse;
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
    public PagingModel<BaseModelResponse> getAll(Integer page, Integer limit) throws BaseException {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null || limit < 1) {
                limit = 10;
            }
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForBaseModel = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL + "all:" + page + ":" + limit;

            List<BaseModelResponse>  baseModelResponsesList = null;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL, hashKeyForBaseModel)) {
                baseModelResponsesList = (List<BaseModelResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL, hashKeyForBaseModel);
            } else {
                List<BaseModel> baseModels = baseModelRepository.findAllByOrderByCreatedDate(pageable);
                baseModelResponsesList = baseModels.stream()
                        .map(baseModel -> modelMapper.map(baseModel, BaseModelResponse.class))
                        .collect(Collectors.toList());
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL, hashKeyForBaseModel, baseModelResponsesList);
            }

            result.setListResult(baseModelResponsesList);

            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel<BaseModelResponse> findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null || limit < 1) {
                limit = 10;
            }
            PagingModel<BaseModelResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForBaseModel = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL + "all:" + "active:" + page + ":" + limit;

            List<BaseModelResponse> baseModelResponseList;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL, hashKeyForBaseModel)) {
                baseModelResponseList = (List<BaseModelResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL, hashKeyForBaseModel);
            } else {
                List<BaseModel> baseModels = baseModelRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
                baseModelResponseList = baseModels.stream()
                        .map(baseModel -> modelMapper.map(baseModel, BaseModelResponse.class))
                        .collect(Collectors.toList());
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_BASE_MODEL, hashKeyForBaseModel, baseModelResponseList);
            }

            result.setListResult(baseModelResponseList);

            result.setTotalPage((int) Math.ceil((double) totalItem() / limit));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

}
