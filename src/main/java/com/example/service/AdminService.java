package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;

import java.util.List;

public interface AdminService {

    /**
     * 新增
     * @param admin
     */
    void add(Admin admin);
    /**
     * 删除
     */
    void deleteById(Integer id);

    /**
     * 批量删除
     */
    void deleteBatch(List<Integer> ids);

    /**
     * 修改
     */
    void updateById(Admin admin);

    /**
     * 根据ID查询
     */
    Admin selectById(Integer id);

    /**
     * 查询所有
     */
    List<Admin> selectAll(Admin admin);

    /**
     * 分页查询
     */
    PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize);

    /**
     * 登录
     */
    Account login(Account account);



    /**
     * 修改密码
     */
    void updatePassword(Account account);

}
