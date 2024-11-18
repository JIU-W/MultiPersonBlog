package com.example.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.example.common.enums.LikesModuleEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.mapper.BlogMapper;
import com.example.service.BlogService;
import com.example.service.CollectService;
import com.example.service.LikesService;
import com.example.service.UserService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.relation.RoleInfo;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {
    @Resource
    private BlogMapper blogMapper;

    @Resource
    private UserService userService;

    @Resource
    private LikesService likesService;

    @Resource
    private CollectService collectService;

    /**
     * 新增
     */
    public void add(Blog blog) {
        blog.setDate(DateUtil.today());
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            blog.setUserId(currentUser.getId());
        }
        blogMapper.insert(blog);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        blogMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    //这种循环调用数据库效率太耗时，优化
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            blogMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Blog blog) {
        blogMapper.updateById(blog);
    }

    /**
     * 根据ID查询
     */

    public Blog selectById(Integer id) {
        Blog blog = blogMapper.selectById(id);
        User user = userService.selectById(blog.getUserId());
        //查询发表的博客的数量
        List<Blog> userBlogList = blogMapper.selectUserBlog(user.getId());
        user.setBlogCount(userBlogList.size());
        //查询用户收到的点赞量,查询用户发表的博客的收藏量
        int userLikesCount = 0;
        int userCollectCount = 0;
        for (Blog b : userBlogList) {
            Integer fid = b.getId();
            int likesCount = likesService.selectByFidAndModule(fid, LikesModuleEnum.BIOG.getValue());
            userLikesCount += likesCount;
            int collectCount = collectService.selectByFidAndModule(fid, LikesModuleEnum.BIOG.getValue());
            userCollectCount += collectCount;
        }
        user.setLikesCount(userLikesCount);
        user.setCollectCount(userCollectCount);

        blog.setUser(user);//设置作者信息
        //查询当前博客的点赞数量
        int likesCount = likesService.selectByFidAndModule(id, LikesModuleEnum.BIOG.getValue());
        blog.setLikesCount(likesCount);
        //查询当前用户是否点过赞
        Likes userlikes = likesService.selectUserLikes(id, LikesModuleEnum.BIOG.getValue());
        blog.setUserLike(userlikes != null);

        //查询当前博客的收藏数量
        int collectCount = collectService.selectByFidAndModule(id, LikesModuleEnum.BIOG.getValue());
        blog.setCollectCount(collectCount);
        //查询当前用户是否收藏
        Collect usercollect = collectService.selectUserCollect(id, LikesModuleEnum.BIOG.getValue());
        blog.setUserCollect(usercollect != null);

        return blog;
    }

    /**
     * 查询所有
     */
    public List<Blog> selectAll(Blog blog) {
        return blogMapper.selectAll(blog);
    }

    /**
     * 分页查询
     */
    public PageInfo<Blog> selectPage(Blog blog, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> list = blogMapper.selectAll(blog);
        for(Blog b : list){
            int likesCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BIOG.getValue());
            b.setLikesCount(likesCount);
        }
        return PageInfo.of(list);
    }

    /**
     * 分页查询当前用户博客列表
     * @param blog
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Blog> selectUser(Blog blog, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            blog.setUserId(currentUser.getId());
        }
        return this.selectPage(blog,pageNum,pageSize);
    }

    /**
     * 博客榜单
     * @return
     */
    public List<Blog> selectTop() {
        List<Blog> blogList = this.selectAll(null);
        blogList = blogList.stream().sorted((b1, b2) -> b2.getReadCount().compareTo(b1.getReadCount()))
                .limit(20)
                .collect(Collectors.toList());
        return blogList;
    }


    /**
     * 博客推荐
     */
    public Set<Blog> selectRecommend(Integer blogId) {
        Blog blog = this.selectById(blogId);
        String tags = blog.getTags();
        Set<Blog> blogSet = new HashSet<>();
        if (ObjectUtil.isNotEmpty(tags)) {
            List<Blog> blogList = this.selectAll(null);
            JSONArray tagsArr = JSONUtil.parseArray(tags);
            for (Object tag : tagsArr) {
                //筛选出包含当前博客标签的其他的博客列表
                Set<Blog> collect = blogList.stream().filter(b -> b.getTags().contains(tag.toString()) && !blogId.equals(b.getId()))
                        .collect(Collectors.toSet());
                blogSet.addAll(collect);
            }
        }
        blogSet = blogSet.stream().limit(5).collect(Collectors.toSet());
        blogSet.forEach(b -> {
            int likesCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BIOG.getValue());
            b.setLikesCount(likesCount);
        });
        return blogSet;
    }

    //更新阅读量
    public void updateReadCount(Integer blogId) {
        blogMapper.updateReadCount(blogId);
    }

    /**
     * 分页查询用户点赞的博客
     * @param blog
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Blog> selectLike(Blog blog, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            blog.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> list = blogMapper.selectLike(blog);
        PageInfo<Blog> pageInfo = PageInfo.of(list);
        List<Blog> blogList = pageInfo.getList();
        for (Blog b : blogList) {
            int likesCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BIOG.getValue());
            b.setLikesCount(likesCount);
        }
        return pageInfo;
    }

    /**
     * 分页查询用户收藏的博客
     * @param blog
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Blog> selectCollect(Blog blog, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            blog.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> list = blogMapper.selectCollect(blog);
        PageInfo<Blog> pageInfo = PageInfo.of(list);
        List<Blog> blogList = pageInfo.getList();
        for (Blog b : blogList) {
            int likesCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BIOG.getValue());
            b.setLikesCount(likesCount);
        }
        return pageInfo;
    }

    /**
     * 分页查询用户评论的博客
     * @param blog
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Blog> selectComment(Blog blog, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            blog.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> list = blogMapper.selectComment(blog);
        PageInfo<Blog> pageInfo = PageInfo.of(list);
        List<Blog> blogList = pageInfo.getList();
        for (Blog b : blogList) {
            int likesCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BIOG.getValue());
            b.setLikesCount(likesCount);
        }
        return pageInfo;
    }

}
