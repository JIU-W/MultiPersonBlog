package com.example.mapper;

import com.example.entity.Admin;
import com.example.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    //新增
    void insert(User user);

    //删除
    void deleteById(Integer id);

    //根据用户名查询
    @Select("select * from user where username = #{username}")
    User selectByUsername(String username);
    //修改
    void updateById(User user);


    User selectById(Integer id);

    //查询所有
    List<User> selectAll(User user);


}
