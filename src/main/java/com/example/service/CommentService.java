package com.example.service;

import com.example.entity.Comment;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CommentService {

    void add(Comment comment);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

    void updateById(Comment comment);

    Comment selectById(Integer id);

    List<Comment> selectAll(Comment comment);

    PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize);

    List<Comment> selectForUser(Comment comment);

    Integer selectCount(Integer fid, String module);

}
