package com.example.service.impl;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Comment;
import com.example.mapper.CommentMapper;
import com.example.service.CommentService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    /**
     * 新增
     */
    public void add(Comment comment) {
        Account currentUser = TokenUtils.getCurrentUser();
        if(currentUser.getRole().equals(RoleEnum.USER.name())){
            comment.setUserId(currentUser.getId());
        }
        comment.setTime(DateUtil.now());
        commentMapper.insert(comment);//先插入数据 拿到主键ID 再设置数据
        if(comment.getRootId() == null) {//如果是根评论(前端不传rootId)，则将其自身id设为根评论id
            comment.setRootId(comment.getId());
            commentMapper.updateById(comment);
        }
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        commentMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            commentMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Comment comment) {
        commentMapper.updateById(comment);
    }

    /**
     * 根据ID查询
     */
    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Comment> selectAll(Comment comment) {
        return commentMapper.selectAll(comment);
    }

    /**
     * 查询前台展示的评论信息
     * */
    public List<Comment> selectForUser(Comment comment) {
        List<Comment> commentList = commentMapper.selectForUser(comment);//查询一级评论
        for (Comment c : commentList) {  // 查询回复列表
            Comment param = new Comment();
            param.setRootId(c.getId());
            List<Comment> children = this.selectAll(param);//
            children = children.stream().filter
                    (child -> !child.getId().equals(c.getId()))
                    .collect(Collectors.toList());  //排除当前查询结果里最外层节点
            c.setChildren(children);
        }
        return commentList;
    }


    public Integer selectCount(Integer fid, String module) {
        return commentMapper.selectCount(fid,module);
    }

    /**
     * 分页查询
     */
    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }



}
