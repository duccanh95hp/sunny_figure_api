package com.example.be.controller;

import com.example.be.common.Result;
import com.example.be.entity.Category;
import com.example.be.payload.CategoryPayload;
import com.example.be.service.CategoryService;
import com.example.be.statics.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.be.common.Constants.FAILURE;
import static com.example.be.common.Constants.SUCCESS;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @PostMapping
    public Result<?> create(@RequestBody CategoryPayload payload){
        Category cate = categoryService.save(payload);
        if(cate ==  null) return Result.result(HttpStatus.BAD_REQUEST.value(), FAILURE, null);
        return Result.result(HttpStatus.OK.value(), SUCCESS, cate);
    }
    @PutMapping("/{id}")
    public Result<?> update(@PathVariable("id") Long id,@RequestBody CategoryPayload payload){
        Category cate = categoryService.update(payload, id);
        if(cate ==  null) return Result.result(HttpStatus.BAD_REQUEST.value(), FAILURE, null);
        return Result.result(HttpStatus.OK.value(), SUCCESS, cate);
    }
    @GetMapping("/{id}")
    public Result<?> get(@PathVariable("id") Long id){
        Category category = categoryService.findById(id);
        if(category ==  null) return Result.result(HttpStatus.BAD_REQUEST.value(), FAILURE, null);
        return Result.result(HttpStatus.OK.value(), SUCCESS, category);
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable("id") Long id){
        boolean result = categoryService.delete(id);
        if(result) return Result.success().setCode(HttpStatus.OK.value());
        return Result.result(HttpStatus.BAD_REQUEST.value(), FAILURE, null);
    }
    @GetMapping
    public Result<?> getAll(){
        List<Category> categories = categoryService.findAllByStatus(Status.ACTIVE);

        return Result.result(HttpStatus.OK.value(), SUCCESS, categories.stream().limit(3).collect(Collectors.toList()));
    }

}
