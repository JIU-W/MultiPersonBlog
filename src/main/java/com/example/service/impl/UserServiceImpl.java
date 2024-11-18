package com.example.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 新增
     */
    public void add(User user){
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword(Constants.USER_DEFAULT_PASSWORD);//默认密码为123
        }
        if (ObjectUtil.isEmpty(user.getName())) {
            user.setName(user.getUsername());
        }
        user.setRole(RoleEnum.USER.name());
        userMapper.insert(user);

    }
    //删除
    public void deleteById(Integer id){
        userMapper.deleteById(id);
    }

    //修改
    public void updateById(User user) {
        userMapper.updateById(user);
    }

    //批量删除
    public void deleteBanth(List<Integer> ids) {
        for (Integer id : ids) {
            userMapper.deleteById(id);
        }
    }

    //根据id查询
    public User selectById(Integer id) {
        User user = userMapper.selectById(id);
        return user;
    }
    //查询所有
    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }
    //分页查询
    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        //用PageHelper插件 进行分页
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    //登录
    public Account login(Account account) {
        Account dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {//比较的是值
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbUser.getId() + "-" + RoleEnum.USER.name();
        String token = TokenUtils.createToken(tokenData, dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    //用户注册
    public void register(Account account) {
        User user = new User();
        BeanUtils.copyProperties(account, user);
        add(user);
    }

    //修改密码
    public void updatePassword(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(account.getNewPassword());
        userMapper.updateById(dbUser);
    }


}
