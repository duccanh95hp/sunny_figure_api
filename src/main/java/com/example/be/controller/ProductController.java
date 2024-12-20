package com.example.be.controller;

import com.example.be.common.Page;
import com.example.be.common.Result;
import com.example.be.entity.Product;
import com.example.be.payload.FilterPayload;
import com.example.be.payload.ProductPayload;
import com.example.be.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import static com.example.be.common.Constants.FAILURE;
import static com.example.be.common.Constants.SUCCESS;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<?> create(@ModelAttribute("payload") ProductPayload payload){
        payload.setOriginalPrice(payload.getPrice());
        Product product = productService.save(payload);
        if(product ==  null) return Result.result(HttpStatus.BAD_REQUEST.value(), FAILURE, null);
        return Result.result(HttpStatus.OK.value(), SUCCESS, product);
    }
    @PostMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<?> update(@PathVariable("id") Long id,@ModelAttribute("payload") ProductPayload payload){
        Product product = productService.update(payload, id);
        if(product ==  null) return Result.result(HttpStatus.BAD_REQUEST.value(), FAILURE, null);
        return Result.result(HttpStatus.OK.value(), SUCCESS, product);
    }
    @GetMapping("/{id}")
    public Result<?> get(@PathVariable("id") Long id){
        Product product = productService.findById(id);
        if(product ==  null) return Result.result(HttpStatus.BAD_REQUEST.value(), FAILURE, null);
        return Result.result(HttpStatus.OK.value(), SUCCESS, product);
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable("id") Long id){
        boolean result = productService.delete(id);
        if(result) return Result.success().setCode(HttpStatus.OK.value());
        return Result.result(HttpStatus.BAD_REQUEST.value(), FAILURE, null);
    }
    @PostMapping
    public Result<?> getAll(@RequestBody FilterPayload payload){
        Page result = productService.getAllAndSearch(payload);
        return Result.result(HttpStatus.OK.value(), SUCCESS, result);
    }
}
