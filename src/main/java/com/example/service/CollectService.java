package com.example.service;

import com.example.entity.Account;
import com.example.entity.Collect;
import com.example.utils.TokenUtils;

import javax.annotation.Resource;

public interface CollectService {

    //收藏和取消
    public void set(Collect collect);

    /**
     * 查询当前用户是否收藏过
     */
    public Collect selectUserCollect(Integer fid, String module);

    public int selectByFidAndModule(Integer fid, String module);


}
