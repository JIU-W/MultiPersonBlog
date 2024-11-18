package com.example.controller;

import com.example.common.Result;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.service.impl.UserServiceImpl;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //新增
    @PostMapping("/add")
    public Result add(@RequestBody User user){
        userService.add(user);
        return Result.success();
    }
    //删除
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){
        userService.deleteById(id);
        return Result.success();
    }
    //批量删除
    @DeleteMapping("/delete/batch")
    public Result deleteBanth(@RequestBody List<Integer> ids){
        userService.deleteBanth(ids);
        return Result.success();
    }

    //修改
    @PutMapping("/update")
    public Result updateById(@RequestBody User user){
        userService.updateById(user);
        return Result.success();
    }

    //根据id查询
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        User user = userService.selectById(id);
        return Result.success(user);
    }

    //查询所有(满足查询条件的均可以被查到，可能会有多个。如果没有查询条件就是查询所有)
    @GetMapping("/selectAll")
    public Result selectAll(User user) {
        List<User> list = userService.selectAll(user);
        return Result.success(list);
    }

    //分页查询
    @GetMapping("/selectPage")
    public Result selectPage(User user,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<User> page = userService.selectPage(user, pageNum, pageSize);
        return Result.success(page);
    }


}
