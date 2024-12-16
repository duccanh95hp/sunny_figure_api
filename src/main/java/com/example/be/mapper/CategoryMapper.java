package com.example.be.mapper;

import com.example.be.entity.Category;
import com.example.be.model.CategoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    Category categoryModelToEntity(CategoryModel categoryModel);
    CategoryModel categoryEntityToModel(Category category);
}
