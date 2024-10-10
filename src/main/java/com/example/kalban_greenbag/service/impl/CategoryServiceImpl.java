package com.example.kalban_greenbag.service.impl;

import com.example.kalban_greenbag.constant.ConstError;
//import com.example.kalban_greenbag.constant.ConstHashKeyPrefix;
import com.example.kalban_greenbag.converter.CategoryConverter;
import com.example.kalban_greenbag.dto.request.category.AddCategoryRequest;
import com.example.kalban_greenbag.dto.request.category.UpdateCategoryRequest;
import com.example.kalban_greenbag.dto.response.base_model.BaseModelResponse;
import com.example.kalban_greenbag.dto.response.category.CategoryResponse;
import com.example.kalban_greenbag.entity.Category;
import com.example.kalban_greenbag.constant.ConstStatus;
import com.example.kalban_greenbag.enums.ErrorCode;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.repository.CategoryRepository;
import com.example.kalban_greenbag.service.ICategoryService;
import com.example.kalban_greenbag.utils.SecurityUtil;
import com.example.kalban_greenbag.utils.ValidateUtil;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements ICategoryService {

    Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JWTServiceImpl jwtServiceImpl;

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ModelMapper modelMapper;

    public int totalItem() {
        return (int) categoryRepository.count();
    }

    @Override
    public CategoryResponse create(AddCategoryRequest addCategoryRequest) throws BaseException {
        try {
            String username = SecurityUtil.getCurrentUsername();
            Category newCategory = new Category();
            newCategory.setCategoryName(addCategoryRequest.getCategoryName());
            newCategory.setDescription(addCategoryRequest.getDescription());
            newCategory.setStatus(addCategoryRequest.getStatus());
            newCategory.setCreatedBy(username);
            Category savedCategory = categoryRepository.save(newCategory);

            CategoryResponse savedCategoryResponse = modelMapper.map(savedCategory, CategoryResponse.class);

//            Set<String> keysToDelete = redisTemplate.keys(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CATEGORY + "*");
//            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
//                redisTemplate.delete(keysToDelete);
//            }

            return savedCategoryResponse;

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
    public CategoryResponse update(UpdateCategoryRequest updateCategoryRequest) throws BaseException {
        try {
            String modifier = SecurityUtil.getCurrentUsername();

            Category category = categoryRepository.findById(updateCategoryRequest.getId())
                    .orElseThrow(() -> new BaseException(ErrorCode.ERROR_500.getCode(),
                            ConstError.Category.CATEGORY_NOT_FOUND,
                            ErrorCode.ERROR_500.getMessage()));

            if (updateCategoryRequest.getCategoryName() != null) {
                category.setCategoryName(updateCategoryRequest.getCategoryName());
            }
            if (updateCategoryRequest.getDescription() != null) {
                category.setDescription(updateCategoryRequest.getDescription());
            }
            if (updateCategoryRequest.getStatus() != null) {
                category.setStatus(updateCategoryRequest.getStatus());
            }

            category.setModifiedBy(modifier);
            Category updatedCategory = categoryRepository.save(category);

            CategoryResponse updatedCategoryResponse = modelMapper.map(updatedCategory, CategoryResponse.class);

//            Set<String> keysToDelete = redisTemplate.keys(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CATEGORY + "*");
//            if (keysToDelete != null && !keysToDelete.isEmpty()) {
//                redisTemplate.delete(keysToDelete);
//            }

            return updatedCategoryResponse;

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
    public Boolean changeStatus(UUID categoryId) throws BaseException {
        try {
            String modifier = SecurityUtil.getCurrentUsername();

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new BaseException(ErrorCode.ERROR_500.getCode(),
                            ConstError.Category.CATEGORY_NOT_FOUND,
                            ErrorCode.ERROR_500.getMessage()));

            // Update the category's status and save the changes
            category.setStatus(ConstStatus.INACTIVE_STATUS);
            category.setModifiedBy(modifier);
            categoryRepository.save(category);

//            // Delete all related cache entries
//            Set<String> keysToDelete = redisTemplate.keys(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CATEGORY + "*");
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
    public CategoryResponse findById(UUID id) throws BaseException {
        try {
//            String hashKeyForCategory = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CATEGORY + id.toString();
//            CategoryResponse categoryResponseByRedis = (CategoryResponse) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CATEGORY, hashKeyForCategory);
//
//            if (!Objects.isNull(categoryResponseByRedis)) {
//                return categoryResponseByRedis;
//            }

            Optional<Category> categoryById = categoryRepository.findById(id);
            boolean isCategoryExist = categoryById.isPresent();

            if (!isCategoryExist) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Category.CATEGORY_NOT_FOUND, ErrorCode.ERROR_404.getMessage());
            }

            CategoryResponse categoryResponse = modelMapper.map(categoryById.get(), CategoryResponse.class);
//            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CATEGORY, hashKeyForCategory, categoryResponse);
            return categoryResponse;
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
    public PagingModel<CategoryResponse> getAll(Integer page, Integer limit) throws BaseException {
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

//            String hashKeyForSkill = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CATEGORY + "all:" + page + ":" + limit;

            List<CategoryResponse> categoryResponsesList = null;

//            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CATEGORY, hashKeyForSkill)) {
//                categoryResponsesList = (List<CategoryResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CATEGORY, hashKeyForSkill);
//            } else {
                List<Category> categories = categoryRepository.findAllByOrderByCreatedDate(pageable);
                categoryResponsesList = categories.stream().map(CategoryConverter::entityToResponse).toList();
//                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CATEGORY, hashKeyForSkill, categoryResponsesList);
//            }

            result.setListResult(categoryResponsesList);

            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel<CategoryResponse> findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
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

//            String hashKeyForSkill = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CATEGORY + "all:"  + "active:" + page + ":" + limit;

            List<CategoryResponse> categoryResponsesList = null;

//            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CATEGORY, hashKeyForSkill)) {
//                categoryResponsesList = (List<CategoryResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CATEGORY, hashKeyForSkill);
//            } else {
                List<Category> categories = categoryRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
                categoryResponsesList = categories.stream().map(CategoryConverter::entityToResponse).toList();
//                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CATEGORY, hashKeyForSkill, categoryResponsesList);
//            }

            result.setListResult(categoryResponsesList);

            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }
}
