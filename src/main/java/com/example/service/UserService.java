package com.example.service;

import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {
    /**
     * 新增
     */
    void add(User user);
    //删除
    void deleteById(Integer id);

    //修改
    void updateById(User user);

    //批量删除
    void deleteBanth(List<Integer> ids);

    //根据id查询
    User selectById(Integer id);
    //查询所有
    List<User> selectAll(User user);
    //分页查询
    PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize);
    //登录
    Account login(Account account);
    //用户注册
    void register(Account account);
    //修改密码
    void updatePassword(Account account);

}
