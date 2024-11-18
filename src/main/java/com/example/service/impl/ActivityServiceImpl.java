package com.example.service.impl;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.LikesModuleEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.mapper.ActivityMapper;
import com.example.service.ActivityService;
import com.example.service.ActivitySignService;
import com.example.service.CollectService;
import com.example.service.LikesService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private ActivitySignService activitySignService;

    @Resource
    private LikesService likesService;

    @Resource
    private CollectService collectService;
    /**
     * 新增
     */
    public void add(Activity activity) {
        activityMapper.insert(activity);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        activityMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            activityMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Activity activity) {
        activityMapper.updateById(activity);
    }

    /**
     * 根据ID查询
     */
    public Activity selectById(Integer id) {
        Activity activity = activityMapper.selectById(id);
        this.setAct(activity,TokenUtils.getCurrentUser());

        //查询点赞量和收藏量
        int likesCount = likesService.selectByFidAndModule(id, LikesModuleEnum.ACTIVITY.getValue());
        int collectCount = collectService.selectByFidAndModule(id, LikesModuleEnum.ACTIVITY.getValue());
        activity.setLikesCount(likesCount);
        activity.setCollectCount(collectCount);

        //查询用户是否点过赞，收藏
        Likes likes = likesService.selectUserLikes(id, LikesModuleEnum.ACTIVITY.getValue());
        activity.setIsLike(likes != null);
        Collect collect = collectService.selectUserCollect(id, LikesModuleEnum.ACTIVITY.getValue());
        activity.setIsCollect(collect != null);
        return activity;
    }

    /**
     * 查询所有
     */
    public List<Activity> selectAll(Activity activity) {
        return  activityMapper.selectAll(activity);
    }

    /**
     * 分页查询
     */
    public PageInfo<Activity> selectPage(Activity activity, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectAll(activity);
        PageInfo<Activity> pageInfo = PageInfo.of(list);
        List<Activity> activityList = pageInfo.getList();
        //获取当前用户
        Account currentUser = TokenUtils.getCurrentUser();
        for (Activity act : activityList) {
            this.setAct(act,currentUser);
        }
        return pageInfo;
    }

    /**
     * 分页查询用户报名的活动列表
     * @param activity
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Activity> selectUser(Activity activity, Integer pageNum, Integer pageSize) {
        //获取当前用户
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            activity.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectUser(activity);
        PageInfo<Activity> pageInfo = PageInfo.of(list);
        List<Activity> activityList = pageInfo.getList();
        for (Activity act : activityList) {
            this.setAct(act,currentUser);
        }
        return pageInfo;
    }

    /**
     * 查询用户点赞的活动列表
     * @param activity
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Activity> selectLike(Activity activity, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            activity.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectLike(activity);
        PageInfo<Activity> pageInfo = PageInfo.of(list);
        List<Activity> activityList = pageInfo.getList();
        for (Activity act : activityList) {
            this.setAct(act, currentUser);
        }
        return pageInfo;
    }

    /**
     * 查询用户收藏的活动列表
     * @param activity
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Activity> selectCollect(Activity activity, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            activity.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectCollect(activity);
        PageInfo<Activity> pageInfo = PageInfo.of(list);
        List<Activity> activityList = pageInfo.getList();
        for (Activity act : activityList) {
            this.setAct(act, currentUser);
        }
        return pageInfo;
    }

    /**
     * 查询用户评论的活动列表
     * @param activity
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Activity> selectComment(Activity activity, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            activity.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectComment(activity);
        PageInfo<Activity> pageInfo = PageInfo.of(list);
        List<Activity> activityList = pageInfo.getList();
        for (Activity act : activityList) {
            this.setAct(act, currentUser);
        }
        return pageInfo;
    }

    private void setAct(Activity act,Account currentUser){
        //活动的结束时间在当前时间之前，就表示活动结束了
        act.setIsEnd(DateUtil.parseDate(act.getEnd()).isBefore(new Date()));
        ActivitySign activitySign = activitySignService.selectByActivityIdAndUserId(act.getId(), currentUser.getId());
        act.setIsSign(activitySign != null);
    }

    /**
     * 热门活动
     */
    public List<Activity> selectTop() {
        List<Activity> activityList = this.selectAll(null);
        activityList = activityList.stream().sorted((b1, b2) -> b2.getReadCount().compareTo(b1.getReadCount()))
                .limit(2)
                .collect(Collectors.toList());
        return activityList;
    }

    //更新阅读量
    public void updateReadCount(Integer activityId) {
        activityMapper.updateReadCount(activityId);
    }

}
