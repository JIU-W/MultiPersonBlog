package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Account;
import com.example.entity.ActivitySign;
import com.example.exception.CustomException;
import com.example.mapper.ActivitySignMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

public interface ActivitySignService {


    public void add(ActivitySign activitySign);

    public ActivitySign selectByActivityIdAndUserId(Integer actId, Integer userId);

    public PageInfo<ActivitySign> selectPage(ActivitySign activitySign, Integer pageNum, Integer pageSize);

    public void deleteById(Integer id);

    public void deleteBatch(List<Integer> ids);

    void userDelete(Integer activityId, Integer userId);

}