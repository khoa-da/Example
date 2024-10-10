package com.example.kalban_greenbag.service.impl;

import com.example.kalban_greenbag.constant.ConstError;
//import com.example.kalban_greenbag.constant.ConstHashKeyPrefix;
import com.example.kalban_greenbag.constant.ConstStatus;
import com.example.kalban_greenbag.converter.CategoryConverter;
import com.example.kalban_greenbag.dto.request.customization_option.AddCustomizationOptionRequest;
import com.example.kalban_greenbag.dto.request.customization_option.UpdateCustomizationOptionRequest;
import com.example.kalban_greenbag.dto.response.category.CategoryResponse;
import com.example.kalban_greenbag.dto.response.customization_option.CustomizationOptionResponse;
import com.example.kalban_greenbag.entity.BaseModel;
import com.example.kalban_greenbag.entity.Category;
import com.example.kalban_greenbag.entity.CustomizationOption;
import com.example.kalban_greenbag.enums.ErrorCode;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.repository.BaseModelRepository;
import com.example.kalban_greenbag.repository.CustomizationOptionRepository;
import com.example.kalban_greenbag.service.ICustomizationOptionService;
import com.example.kalban_greenbag.utils.SecurityUtil;
import com.example.kalban_greenbag.utils.ValidateUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomizationOptionServiceImpl implements ICustomizationOptionService {

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BaseModelRepository baseModelRepository;

    @Autowired
    private CustomizationOptionRepository customizationOptionRepository;

    public int totalItem() {
        return (int) customizationOptionRepository.count();
    }


    @Override
    public CustomizationOptionResponse create(AddCustomizationOptionRequest addCustomizationOptionRequest) throws BaseException {
        try {
            String username = SecurityUtil.getCurrentUsername();
            CustomizationOption newCustomization = new CustomizationOption();

            newCustomization.setOptionName(addCustomizationOptionRequest.getOptionName());
            newCustomization.setOptionType(addCustomizationOptionRequest.getOptionType());
            newCustomization.setAdditionalPrice(addCustomizationOptionRequest.getAdditionalPrice());
            newCustomization.setStatus(addCustomizationOptionRequest.getStatus());

            BaseModel baseModel = baseModelRepository.findById(addCustomizationOptionRequest.getBaseModelID())
                    .orElseThrow(() -> new BaseException(ErrorCode.ERROR_500.getCode(),
                            ConstError.BaseModel.BASE_MODEL_NOT_FOUND,
                            ErrorCode.ERROR_500.getMessage()));

            newCustomization.setBaseModelID(baseModel);
            newCustomization.setCreatedBy(username);

            CustomizationOption savedCustomization = customizationOptionRepository.save(newCustomization);

            CustomizationOptionResponse savedCustomizationResponse = modelMapper.map(savedCustomization, CustomizationOptionResponse.class);

//            Set<String> keysToDelete = redisTemplate.keys(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CUSTOMIZATION_OPTION + "*");
//            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
//                redisTemplate.delete(keysToDelete);
//            }

            return savedCustomizationResponse;

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
    public CustomizationOptionResponse update(UpdateCustomizationOptionRequest updateCustomizationOptionRequest) throws BaseException {
        try {
            String modifier = SecurityUtil.getCurrentUsername();

            CustomizationOption existingCustomization = customizationOptionRepository.findById(updateCustomizationOptionRequest.getId())
                    .orElseThrow(() -> new BaseException(ErrorCode.ERROR_500.getCode(),
                            ConstError.CustomizationOption.CUSTOMIZATION_OPTION_NOT_FOUND,
                            ErrorCode.ERROR_500.getMessage()));

            if (updateCustomizationOptionRequest.getOptionName() != null) {
                existingCustomization.setOptionName(updateCustomizationOptionRequest.getOptionName());
            }
            if (updateCustomizationOptionRequest.getOptionType() != null) {
                existingCustomization.setOptionType(updateCustomizationOptionRequest.getOptionType());
            }
            if (updateCustomizationOptionRequest.getAdditionalPrice() != null) {
                existingCustomization.setAdditionalPrice(updateCustomizationOptionRequest.getAdditionalPrice());
            }
            if (updateCustomizationOptionRequest.getBaseModelID() != null) {
                BaseModel baseModel = baseModelRepository.findById(updateCustomizationOptionRequest.getBaseModelID())
                        .orElseThrow(() -> new BaseException(ErrorCode.ERROR_500.getCode(),
                                ConstError.BaseModel.BASE_MODEL_NOT_FOUND,
                                ErrorCode.ERROR_500.getMessage()));
                existingCustomization.setBaseModelID(baseModel);
            }
            existingCustomization.setModifiedBy(modifier);
            CustomizationOption updatedCustomization = customizationOptionRepository.save(existingCustomization);

//            Set<String> keysToDelete = redisTemplate.keys(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CUSTOMIZATION_OPTION + "*");
//            if (keysToDelete != null && !keysToDelete.isEmpty()) {
//                redisTemplate.delete(keysToDelete);
//            }

            return modelMapper.map(updatedCustomization, CustomizationOptionResponse.class);

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
    public Boolean changeStatus(UUID id) throws BaseException {
        try {
            String modifier = SecurityUtil.getCurrentUsername();

            CustomizationOption customizationOption = customizationOptionRepository.findById(id)
                    .orElseThrow(() -> new BaseException(ErrorCode.ERROR_500.getCode(),
                            ConstError.CustomizationOption.CUSTOMIZATION_OPTION_NOT_FOUND,
                            ErrorCode.ERROR_500.getMessage()));

            customizationOption.setStatus(ConstStatus.ACTIVE_STATUS);
            customizationOption.setModifiedBy(modifier);

            customizationOptionRepository.save(customizationOption);

//            Set<String> keysToDelete = redisTemplate.keys(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CUSTOMIZATION_OPTION + "*");
//            if (keysToDelete != null && !keysToDelete.isEmpty()) {
//                redisTemplate.delete(keysToDelete);
//            }

            return true;
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
    public CustomizationOptionResponse findById(UUID id) throws BaseException {
        try {
//            String hashKeyForCustomizationOption = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CUSTOMIZATION_OPTION + id.toString();
//            CustomizationOptionResponse customizationOptionByRedis = (CustomizationOptionResponse) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CUSTOMIZATION_OPTION, hashKeyForCustomizationOption);
//
//            if (!Objects.isNull(customizationOptionByRedis)) {
//                return customizationOptionByRedis;
//            }

            Optional<CustomizationOption> customizationOptionById = customizationOptionRepository.findById(id);
            if (!customizationOptionById.isPresent()) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.CustomizationOption.CUSTOMIZATION_OPTION_NOT_FOUND , ErrorCode.ERROR_500.getMessage());
            }

            CustomizationOptionResponse customizationOptionResponse = modelMapper.map(customizationOptionById.get(), CustomizationOptionResponse.class);
//            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CUSTOMIZATION_OPTION, hashKeyForCustomizationOption, customizationOptionResponse);

            return customizationOptionResponse;
        } catch (Exception exception) {
            if (exception instanceof BaseException) {
                throw exception;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel<CustomizationOptionResponse> getAll(Integer page, Integer limit) throws BaseException {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null || limit < 1) {
                limit = 10;
            }
            PagingModel<CustomizationOptionResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = (Pageable) PageRequest.of(page - 1, limit);

//            String hashKeyForAllCustomizationOptions = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CUSTOMIZATION_OPTION + "all:" + page + ":" + limit;

            List<CustomizationOptionResponse> customizationOptionList;

//            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CUSTOMIZATION_OPTION, hashKeyForAllCustomizationOptions)) {
//                customizationOptionList = (List<CustomizationOptionResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CUSTOMIZATION_OPTION, hashKeyForAllCustomizationOptions);
//            } else {
                List<CustomizationOption> customizationOptions = customizationOptionRepository.findAllByOrderByCreatedDate(pageable);
                customizationOptionList = customizationOptions.stream()
                        .map(option -> modelMapper.map(option, CustomizationOptionResponse.class))
                        .toList();
//                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CUSTOMIZATION_OPTION, hashKeyForAllCustomizationOptions, customizationOptionList);
//            }

            result.setListResult(customizationOptionList);
            long totalItems = customizationOptionRepository.count();
            result.setTotalPage((int) Math.ceil((double) totalItems / limit));
            result.setLimit(limit);

            return result;
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }



    @Override
    public PagingModel<CustomizationOptionResponse> findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null || limit < 1) {
                limit = 10;
            }
            PagingModel<CustomizationOptionResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = (Pageable) PageRequest.of(page - 1, limit);

//            String hashKeyForActiveCustomizationOptions = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CUSTOMIZATION_OPTION + "all:active:" + page + ":" + limit;

            List<CustomizationOptionResponse> customizationOptionList;

//            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CUSTOMIZATION_OPTION, hashKeyForActiveCustomizationOptions)) {
//                customizationOptionList = (List<CustomizationOptionResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CUSTOMIZATION_OPTION, hashKeyForActiveCustomizationOptions);
//            } else {
                List<CustomizationOption> customizationOptions = customizationOptionRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
                customizationOptionList = customizationOptions.stream()
                        .map(option -> modelMapper.map(option, CustomizationOptionResponse.class))
                        .toList();

//                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CUSTOMIZATION_OPTION, hashKeyForActiveCustomizationOptions, customizationOptionList);
//            }

            result.setListResult(customizationOptionList);

            result.setTotalPage((int) Math.ceil((double) totalItem() / limit));
            result.setLimit(limit);

            return result;
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }


}
