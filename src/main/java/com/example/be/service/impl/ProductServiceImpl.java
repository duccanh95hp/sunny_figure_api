package com.example.be.service.impl;


import com.example.be.entity.Category;
import com.example.be.entity.Product;
import com.example.be.payload.FilterPayload;
import com.example.be.payload.ProductPayload;
import com.example.be.repository.ProductRepo;
import com.example.be.service.CategoryService;
import com.example.be.service.ImageService;
import com.example.be.service.ProductService;
import com.example.be.statics.Status;
import com.example.be.statics.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ImageService imageService;
    private final CategoryService categoryService;
    @Override
    public Product save(ProductPayload payload) {
        Category category = categoryService.findById(payload.getCategoryId());
        String urlAvatar = "";
        if(!payload.getFile().isEmpty()){
            urlAvatar = imageService.upload(payload.getFile());
        }

        Product product = Product.builder()
                .name(payload.getName())
                .description(payload.getDescription())
                .category(category)
                .avatarUrl(urlAvatar)
                .price(payload.getPrice())
                .stockQuantity(payload.getStockQuantity())
                .manufacturer(payload.getManufacturer())
                .height(payload.getHeight())
                .weight(payload.getWeight())
                .accessory(payload.getAccessory())
                .box(payload.getBox())
                .isDelete(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .type(Type.NEW)
                .build();
        return productRepo.save(product);
    }

    @Override
    public Product update(ProductPayload payload, Long aLong) {
        Product product = findById(aLong);
        Category category = categoryService.findById(payload.getCategoryId());
        String urlAvatar = "";
        if(payload.getFile() != null){
            urlAvatar = imageService.upload(payload.getFile());
        }
        if(product != null){
            product.setName(payload.getName().isEmpty() ? product.getName() : payload.getName());
            product.setCategory(category == null ? product.getCategory() : category);
            product.setDescription(payload.getDescription() == null ? product.getDescription() : payload.getDescription());
            product.setAvatarUrl(payload.getFile() == null ? product.getAvatarUrl() : urlAvatar);
            product.setPrice(payload.getPrice());
            product.setStockQuantity(payload.getStockQuantity());
            product.setManufacturer(payload.getManufacturer());
            product.setHeight(payload.getHeight());
            product.setWeight(payload.getWeight());
            product.setAccessory(payload.getAccessory());
            product.setBox(payload.getBox());
            product.setUpdatedAt(LocalDateTime.now());
            return productRepo.save(product);
        }
        return null;
    }

    @Override
    public boolean delete(Long aLong) {
        Product product = findById(aLong);
        if(product != null){
            product.setIsDelete(true);
            productRepo.save(product);
            return true;
        }
        return false;
    }

    @Override
    public Product findById(Long aLong) {
        Product product = productRepo.findById(aLong).orElse(null);
        if(product != null){
            return product;
        }
        return null;
    }

    @Override
    public List<Product> findAll() {
        return productRepo.findAll();
    }


    @Override
    public com.example.be.common.Page<Object> getAllAndSearch(FilterPayload filter) {
        org.springframework.data.domain.Page<Product> page = productRepo.getAllAndSearch(filter, PageRequest.of(filter.getPage() - 1, filter.getSize()));
        if(page.isEmpty()) {
            return null;
        }
        List<Object> products = new ArrayList<>();
        for (Product p : page.getContent()){

            products.add(p);
        }
        return com.example.be.common.Page.builder()
                .result(products)
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .pageNumber(filter.getPage())
                .pageSize(filter.getSize())
                .build();
    }
}
