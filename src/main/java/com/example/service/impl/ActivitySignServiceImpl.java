package com.example.service.impl;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Account;
import com.example.entity.ActivitySign;
import com.example.exception.CustomException;
import com.example.mapper.ActivitySignMapper;
import com.example.service.ActivitySignService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ActivitySignServiceImpl implements ActivitySignService {

    @Resource
    private ActivitySignMapper activitySignMapper;

    public void add(ActivitySign activitySign) {
        Account currentUser = TokenUtils.getCurrentUser();
        ActivitySign as = this.selectByActivityIdAndUserId(activitySign.getActivityId(), currentUser.getId());  // 查看用户是否已经报名
        if (as != null) {
            throw new CustomException(ResultCodeEnum.ACTIVITY_SIGN_ERROR);
        }
        activitySign.setUserId(currentUser.getId());
        activitySign.setTime(DateUtil.now());
        activitySignMapper.insert(activitySign);
    }

    //通过活动id和用户id查询 用户的活动报名信息
    public ActivitySign selectByActivityIdAndUserId(Integer actId, Integer userId) {
        return activitySignMapper.selectByActivityIdAndUserId(actId, userId);
    }

    public PageInfo<ActivitySign> selectPage(ActivitySign activitySign, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ActivitySign> list = activitySignMapper.selectAll(activitySign);
        return PageInfo.of(list);
    }

    public void deleteById(Integer id) {
        activitySignMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    /**
     * 用户取消报名活动
     * @param activityId
     * @param userId
     */
    public void userDelete(Integer activityId, Integer userId) {
        activitySignMapper.userDelete(activityId, userId);
    }

}
