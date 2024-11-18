package com.example.service.impl;

import com.example.entity.Account;
import com.example.entity.Likes;
import com.example.mapper.LikesMapper;
import com.example.service.LikesService;
import com.example.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikesServicempl implements LikesService{


    @Autowired
    private LikesMapper likesMapper;

    //点赞和取消点赞
    public void set(Likes likes) {
        Account currentUser = TokenUtils.getCurrentUser();
        likes.setUserId(currentUser.getId());
        Likes dbLikes = likesMapper.selectUserLikes(likes);
        if (dbLikes == null) {
            likesMapper.insert(likes);
        } else {
            likesMapper.deleteById(dbLikes.getId());
        }
    }

    //查询当前用户是否点过赞
    public Likes selectUserLikes(Integer fid, String module) {
        Account currentUser = TokenUtils.getCurrentUser();
        Likes likes = new Likes();
        likes.setUserId(currentUser.getId());
        likes.setFid(fid);
        likes.setModule(module);
        return likesMapper.selectUserLikes(likes);
    }

    public int selectByFidAndModule(Integer fid, String module) {
        return likesMapper.selectByFidAndModule(fid, module);
    }


}
