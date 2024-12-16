package com.example.be.service;

import com.example.be.common.Page;
import com.example.be.entity.Product;
import com.example.be.payload.FilterPayload;
import com.example.be.payload.ProductPayload;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductService extends CrudService<ProductPayload, Product, Long>{
    Page<Object> getAllAndSearch(FilterPayload filter);
}
