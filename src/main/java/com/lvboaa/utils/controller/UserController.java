package com.lvboaa.utils.controller;

import com.lvboaa.utils.VO.BaseVo;
import com.lvboaa.utils.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author: lv.bo
 * @create: 2023-08-25 14:12
 */
@Api(value = "用户操作接口", tags = "用户操作接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public BaseVo getUsers(){
        return userService.getUsers();
    }
}
