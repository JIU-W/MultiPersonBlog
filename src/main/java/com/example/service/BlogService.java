package com.example.service;

import com.example.entity.Blog;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Set;

public interface BlogService {

    void add(Blog blog);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

    void updateById(Blog blog);

    Blog selectById(Integer id);

    List<Blog> selectAll(Blog blog);

    PageInfo<Blog> selectPage(Blog blog, Integer pageNum, Integer pageSize);

    List<Blog> selectTop();

    Set<Blog> selectRecommend(Integer blogId);

    void updateReadCount(Integer blogId);

    PageInfo<Blog> selectUser(Blog blog, Integer pageNum, Integer pageSize);

    PageInfo<Blog> selectLike(Blog blog, Integer pageNum, Integer pageSize);

    PageInfo<Blog> selectCollect(Blog blog, Integer pageNum, Integer pageSize);

    PageInfo<Blog> selectComment(Blog blog, Integer pageNum, Integer pageSize);

}
