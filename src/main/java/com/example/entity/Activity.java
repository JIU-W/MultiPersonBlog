package com.example.entity;

import lombok.Data;

@Data
public class Activity {

    /** ID */
    private Integer id;
    /** 活动名称 */
    private String name;
    /** 活动简介 */
    private String descr;
    /** 开始时间 */
    private String start;
    /** 结束时间 */
    private String end;
    /** 活动形式 */
    private String form;
    /** 活动地址 */
    private String address;
    /** 主办方 */
    private String host;
    /** 浏览量 */
    private Integer readCount;
    private String content;
    private String cover;

    //活动是否结束
    private Boolean isEnd;

    //是否报名活动
    private Boolean isSign;

    //活动的点赞数量
    private Integer likesCount;
    //活动的收藏数量
    private Integer collectCount;

    //用户是否点过赞
    private boolean isLike;
    //用户是否收藏
    private boolean isCollect;

    private Integer userId;

    public void setIsLike(boolean b) {
        this.isLike = b;
    }

    public void setIsCollect(boolean b) {
        this.isCollect = b;
    }

}