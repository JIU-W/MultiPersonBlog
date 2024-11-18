package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 姓名 */
    private String name;
    /** 电话 */
    private String phone;
    /** 邮箱 */
    private String email;
    /** 头像 */
    private String avatar;
    /** 角色标识 */
    private String role;

    /** 性别 */
    private String sex;
    //简介
    private String info;
    //生日
    private String birth;

    //用户发表的博客的数量
    private Integer blogCount;
    //用户收到的点赞量
    private Integer likesCount;
    //用户收到的收藏量
    private Integer collectCount;


}
