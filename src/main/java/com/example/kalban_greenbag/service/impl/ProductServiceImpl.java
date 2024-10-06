package com.example.kalban_greenbag.service.impl;

import com.example.kalban_greenbag.constant.ConstError;
import com.example.kalban_greenbag.constant.ConstStatus;
import com.example.kalban_greenbag.dto.request.product.CreateProductRequest;
import com.example.kalban_greenbag.dto.request.product.UpdateProductRequest;
import com.example.kalban_greenbag.dto.response.product.ProductResponse;
import com.example.kalban_greenbag.entity.Product;
import com.example.kalban_greenbag.enums.ErrorCode;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.repository.ProductRepository;
import com.example.kalban_greenbag.service.IProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements IProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductResponse findById(UUID id) throws BaseException {
        try {
            Optional<Product> product = productRepository.findById(id);
            if (product.isEmpty()) {
                throw new BaseException(ErrorCode.ERROR_404.getCode(), ConstError.Product.PRODUCT_NOT_FOUND, ErrorCode.ERROR_404.getMessage());
            }
            return modelMapper.map(product.get(), ProductResponse.class);
        } catch (Exception exception) {
            if (exception instanceof BaseException) {
                throw exception;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel<ProductResponse> getAll(Integer page, Integer limit) throws BaseException {
        try {
            if(page == null || limit == null){
                page = 1;
                limit = 10;
            }
            PagingModel<ProductResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            List<Product> productList = productRepository.findAllByOrderByCreatedDate(pageable);
            List<ProductResponse> productResponses = productList.stream()
                    .map(product -> modelMapper.map(product, ProductResponse.class))
                    .toList();


            result.setListResult(productResponses);
            result.setTotalPage((int) Math.ceil((double) totalItem() / limit));
            result.setLimit(limit);

            return result;
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    public int totalItem() {
        return (int) productRepository.count();
    }

    @Override
    public PagingModel<ProductResponse> findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {
            if(page == null || limit == null){
                page = 1;
                limit = 10;
            }
            PagingModel<ProductResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            List<Product> productList = productRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
            List<ProductResponse> productResponses = productList.stream()
                    .map(product -> modelMapper.map(product, ProductResponse.class))
                    .toList();

            result.setListResult(productResponses);
            result.setTotalPage((int) Math.ceil((double) totalActiveItems() / limit));
            result.setLimit(limit);

            return result;
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    private int totalActiveItems() {
        return (int) productRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }

    @Override
    public ProductResponse create(CreateProductRequest createProductRequest) throws BaseException {
        try {
            Product newProduct = modelMapper.map(createProductRequest, Product.class);
            newProduct.setStatus(createProductRequest.getStatus());
            Product savedProduct = productRepository.save(newProduct);
            return modelMapper.map(savedProduct, ProductResponse.class);
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public ProductResponse update(UpdateProductRequest updateProductRequest) throws BaseException {
        try {
            Product product = productRepository.findById(updateProductRequest.getId())
                    .orElseThrow(() -> new BaseException(ErrorCode.ERROR_404.getCode(),
                            ConstError.Product.PRODUCT_NOT_FOUND,
                            ErrorCode.ERROR_404.getMessage()));

            modelMapper.map(updateProductRequest, product);
            product.setDesciption(updateProductRequest.getDescription());
            Product updatedProduct = productRepository.save(product);
            return modelMapper.map(updatedProduct, ProductResponse.class);
        } catch (Exception exception) {
            if (exception instanceof BaseException) {
                throw exception;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public Boolean changeStatus(UUID productId) throws BaseException {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new BaseException(ErrorCode.ERROR_404.getCode(),
                            ConstError.Product.PRODUCT_NOT_FOUND,
                            ErrorCode.ERROR_404.getMessage()));

            product.setStatus(ConstStatus.INACTIVE_STATUS);
            productRepository.save(product);

            return true;
        } catch (Exception exception) {
            if (exception instanceof BaseException) {
                throw exception;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public boolean reduceProductStock(UUID productId, Integer stock) throws BaseException {
        try {
            int rowsAffected = productRepository.reduceProductStockById(productId, stock);
            return rowsAffected > 0;
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(),
                    exception.getMessage(),
                    ErrorCode.ERROR_500.getMessage());
        }
    }


}
