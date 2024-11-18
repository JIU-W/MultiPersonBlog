package com.example.mapper;

import com.example.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CategoryMapper {

    void insert(Category category);

    @Delete("delete from category where id = #{id}")
    void deleteById(Integer id);

    void updateById(Category category);

    @Select("select * from category where id = #{id}")
    Category selectById(Integer id);

    List<Category> selectAll(Category category);

}
