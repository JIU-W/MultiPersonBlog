package com.example.mapper;

import com.example.entity.Blog;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface BlogMapper {

    void insert(Blog blog);

    void deleteById(Integer id);

    void updateById(Blog blog);

    Blog selectById(Integer id);

    List<Blog> selectAll(Blog blog);

    @Select("select * from blog where user_id = #{userId}")
    List<Blog> selectUserBlog(Integer userId);

    @Update("update blog set read_count = read_count + 1 where id = #{blogId}")
    void updateReadCount(Integer blogId);

    List<Blog> selectLike(Blog blog);

    List<Blog> selectCollect(Blog blog);

    List<Blog> selectComment(Blog blog);

}
