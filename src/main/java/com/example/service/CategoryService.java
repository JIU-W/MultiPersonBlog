package com.example.service;

import com.example.entity.Category;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CategoryService {

    void add(Category category);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

    void updateById(Category category);

    Category selectById(Integer id);

    List<Category> selectAll(Category category);

    PageInfo<Category> selectPage(Category category, Integer pageNum, Integer pageSize);

}
