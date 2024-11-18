package com.example.service.impl;

import com.example.entity.Account;
import com.example.entity.Collect;
import com.example.mapper.CollectMapper;
import com.example.service.CollectService;
import com.example.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CollectServiceImpl implements CollectService {

    @Resource
    private CollectMapper collectMapper;

    //收藏和取消
    public void set(Collect collect) {
        Account currentUser = TokenUtils.getCurrentUser();
        collect.setUserId(currentUser.getId());
        Collect dblCollect = collectMapper.selectUserCollect(collect);
        if (dblCollect == null) {
            collectMapper.insert(collect);
        } else {
            collectMapper.deleteById(dblCollect.getId());
        }
    }

    //查询当前用户是否收藏过
    public Collect selectUserCollect(Integer fid, String module) {
        Account currentUser = TokenUtils.getCurrentUser();
        Collect collect = new Collect();
        collect.setUserId(currentUser.getId());
        collect.setFid(fid);
        collect.setModule(module);
        return collectMapper.selectUserCollect(collect);
    }

    public int selectByFidAndModule(Integer fid, String module) {
        return collectMapper.selectByFidAndModule(fid, module);
    }

}
