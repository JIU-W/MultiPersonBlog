package com.example.service;

import com.example.entity.Account;
import com.example.entity.Likes;
import com.example.utils.TokenUtils;

import java.util.List;

public interface LikesService {

    //点赞和取消
    void set(Likes likes);

    /**
     * 查询当前用户是否点过赞
     */
    public Likes selectUserLikes(Integer fid, String module);

    public int selectByFidAndModule(Integer fid, String module);

}
