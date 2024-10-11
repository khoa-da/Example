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
import com.example.kalban_greenbag.utils.SecurityUtil;
import java.util.Collections;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
            ProductResponse productResponse = modelMapper.map(product.get(), ProductResponse.class);
            productResponse.setDescription(product.get().getDesciption());
            return productResponse;
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

            List<Product> productList = productRepository.findAllByOrderByCreatedDateDesc(pageable);
            List<ProductResponse> productResponses = productList.stream()
                    .map(product -> {
                        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
                        productResponse.setDescription(product.getDesciption()); // Thêm dòng này để set description
                        return productResponse;
                    })
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

            List<Product> productList = productRepository.findAllByStatusOrderByCreatedDateDesc(ConstStatus.ACTIVE_STATUS, pageable);
            List<ProductResponse> productResponses = productList.stream()
                    .map(product -> {
                        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
                        productResponse.setDescription(product.getDesciption()); // Thêm dòng này để set description
                        return productResponse;
                    })
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
            newProduct.setDesciption(createProductRequest.getDescription());
            Product savedProduct = productRepository.save(newProduct);
            ProductResponse productResponse = modelMapper.map(savedProduct, ProductResponse.class);
            productResponse.setDescription(newProduct.getDesciption());
            return productResponse;
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public ProductResponse update(UpdateProductRequest updateProductRequest) throws BaseException {
        try {
            // Find the existing product
            Product product = productRepository.findById(updateProductRequest.getId())
                    .orElseThrow(() -> new BaseException(ErrorCode.ERROR_404.getCode(),
                            ConstError.Product.PRODUCT_NOT_FOUND,
                            ErrorCode.ERROR_404.getMessage()));

            // Update fields only if they are provided in the request
            if (updateProductRequest.getProductName() != null) {
                product.setProductName(updateProductRequest.getProductName());
            }
            if (updateProductRequest.getStock() != null) {
                product.setStock(updateProductRequest.getStock());
            }
            if (updateProductRequest.getProductName() != null) {
                product.setProductName(updateProductRequest.getProductName());
            }
            if (updateProductRequest.getDescription() != null) {
                product.setDesciption(updateProductRequest.getDescription());
            }
            if (updateProductRequest.getImg() != null) {
                product.setImg(updateProductRequest.getImg());
            }
            if (updateProductRequest.getFinalPrice() != null) {
                product.setFinalPrice(updateProductRequest.getFinalPrice());
            }
            if (updateProductRequest.getStatus() != null) {
                product.setStatus(updateProductRequest.getStatus());
            }
            // Add more fields as necessary, following the same pattern

            // Update the modified by field
            product.setModifiedBy(SecurityUtil.getCurrentUsername());

            // Save the updated product
            Product updatedProduct = productRepository.save(product);

            // Map the updated product to the response
            ProductResponse productResponse = modelMapper.map(updatedProduct, ProductResponse.class);
            productResponse.setDescription(product.getDesciption());
            return productResponse;

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

    @Override
    public PagingModel<ProductResponse> getProductByName(String name, Integer page, Integer limit) throws BaseException {
        try{
            if(page == null || limit == null){
                page = 1;
                limit = 10;
            }
            PagingModel<ProductResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            List<Product> productList;
            if(name == null){
                productList = productRepository.findAllByStatusOrderByCreatedDateDesc(ConstStatus.ACTIVE_STATUS, pageable);
            }else {
                productList = productRepository.findAllByProductNameContaining(name, pageable);
            }
            List<ProductResponse> productResponses = productList.stream()
                    .map(product -> {
                        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
                        return productResponse;
                    })
                    .toList();

            result.setListResult(productResponses);
            result.setTotalPage((int) Math.ceil((double) totalActiveItems() / limit));
            result.setLimit(limit);

            return result;
        }catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(),
                    exception.getMessage(),
                    ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel<ProductResponse> getProductByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Integer page, Integer limit) throws BaseException {
        try{
            if(page == null || limit == null){
                page = 1;
                limit = 10;
            }
            PagingModel<ProductResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            List<Product> productList;
            if(minPrice == null && maxPrice == null){
                productList = productRepository.findAllByStatusOrderByCreatedDateDesc(ConstStatus.ACTIVE_STATUS, pageable);
            }else {
                productList = productRepository.findAllByPriceRange(minPrice, maxPrice, pageable);
            }
            List<ProductResponse> productResponses = productList.stream()
                    .map(product -> {
                        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
                        return productResponse;
                    })
                    .toList();

            result.setListResult(productResponses);
            result.setTotalPage((int) Math.ceil((double) totalActiveItems() / limit));
            result.setLimit(limit);

            return result;
        }catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(),
                    exception.getMessage(),
                    ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel<ProductResponse> getProductByNameAndPriceRange(String name, BigDecimal minPrice, BigDecimal maxPrice, Integer page, Integer limit) throws BaseException {
        try {
            if (page == null || limit == null) {
                page = 1;
                limit = 10;
            }

            PagingModel<ProductResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            List<Product> productList;

            // Kiểm tra các điều kiện tìm kiếm
            if ((name == null || name.isEmpty()) && (minPrice == null && maxPrice == null)) {
                // Không có điều kiện, lấy tất cả sản phẩm
                productList = productRepository.findAllByStatusOrderByCreatedDateDesc(ConstStatus.ACTIVE_STATUS, pageable);
            } else if (name != null && (minPrice == null && maxPrice == null)) {
                // Tìm theo tên sản phẩm
                productList = productRepository.findAllByProductNameContaining(name, pageable);
            } else if (name == null && (minPrice != null || maxPrice != null)) {
                // Tìm theo khoảng giá
                productList = productRepository.findAllByPriceRange(minPrice, maxPrice, pageable);
            } else {
                // Tìm theo cả tên và khoảng giá
                productList = productRepository.findAllByProductNameContainingAndPriceRange(name, minPrice, maxPrice, pageable);
            }

            // Nếu không tìm thấy sản phẩm, trả về mảng trống
            if (productList == null || productList.isEmpty()) {
                result.setListResult(Collections.emptyList());
                result.setTotalPage(0);
                result.setLimit(limit);
                return result;
            }

            // Chuyển đổi kết quả tìm kiếm sang dạng ProductResponse
            List<ProductResponse> productResponses = productList.stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .toList();

            result.setListResult(productResponses);

            // Cập nhật totalPage
            int totalItems;
            if ((name == null || name.isEmpty()) && (minPrice == null && maxPrice == null)) {
                totalItems = totalActiveItems(); // Tổng sản phẩm đang hoạt động
            } else {
                totalItems = productRepository.countByCriteria(name, minPrice, maxPrice); // Tổng sản phẩm theo điều kiện
            }

            result.setTotalPage((int) Math.ceil((double) totalItems / limit));
            result.setLimit(limit);

            return result;
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(),
                exception.getMessage(),
                ErrorCode.ERROR_500.getMessage());
        }
    }


}
