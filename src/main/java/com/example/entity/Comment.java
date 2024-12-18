package com.example.entity;

import lombok.Data;

import java.util.List;

@Data
public class Comment {

    /** ID */
    private Integer id;
    /** 内容 */
    private String content;
    /** 评论人 */
    private Integer userId;
    /** 父级ID */
    private Integer pid;
    /** 根节点ID */
    private Integer rootId;
    /** 评论时间 */
    private String time;

    /** 博客ID */
    private Integer fid;

    private String module;

    /**评论人*/
    private String userName;

    /**用户头像*/
    private String avatar;

    /**回复数据*/
    private List<Comment> children;

    private String replyUser;  // 回复给谁 就是谁的名称

}
