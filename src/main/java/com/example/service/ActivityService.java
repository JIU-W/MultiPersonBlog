package com.example.service;

import com.example.entity.Activity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ActivityService {

    public void add(Activity activity);

    /**
     * 删除
     */
    public void deleteById(Integer id);

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids);

    /**
     * 修改
     */
    public void updateById(Activity activity);

    /**
     * 根据ID查询
     */
    public Activity selectById(Integer id);

    /**
     * 查询所有
     */
    public List<Activity> selectAll(Activity activity);

    /**
     * 分页查询
     */
    public PageInfo<Activity> selectPage(Activity activity, Integer pageNum, Integer pageSize);

    List<Activity> selectTop();

    void updateReadCount(Integer activityId);

    PageInfo<Activity> selectUser(Activity activity, Integer pageNum, Integer pageSize);

    PageInfo<Activity> selectLike(Activity activity, Integer pageNum, Integer pageSize);

    PageInfo<Activity> selectCollect(Activity activity, Integer pageNum, Integer pageSize);

    PageInfo<Activity> selectComment(Activity activity, Integer pageNum, Integer pageSize);

}
