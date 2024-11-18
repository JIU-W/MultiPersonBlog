package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 博客信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog {

    /** ID */
    private Integer id;
    /** 标题 */
    private String title;
    /** 内容 */
    private String content;
    /** 简介 */
    private String descr;
    /** 封面 */
    private String cover;
    /** 标签 */
    private String tags;
    /** 发布人ID */
    private Integer userId;
    /** 发布日期 */
    private String date;
    /** 浏览量 */
    private Integer readCount;

    private Integer categoryId;

    private String categoryName;

    private String userName;

    private User user;

    //博客的点赞数量
    private Integer likesCount;

    //判断有没有被当前用户点过赞
    private Boolean userLike;

    //博客的收藏数量
    private Integer collectCount;

    //判断有没有被当前用户收藏
    private Boolean userCollect;



    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blog blog = (Blog) o;
        return Objects.equals(getId(), blog.getId());
    }

    public int hashCode() {
        return Objects.hash(getId());
    }

}