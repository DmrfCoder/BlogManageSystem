package com.dmrf.controller;

import com.dmrf.model.ManagerEntity;
import com.dmrf.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
    // 自动装配数据库接口，不需要再写原始的Connection来操作数据库
    @Autowired
    ManagerRepository managerRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "admin/login";
    }

    @RequestMapping(value = "/admin/manager/loginP", method = RequestMethod.POST)
    public String addUserPost(@ModelAttribute("manager") ManagerEntity managerEntity) {
        // 注意此处，post请求传递过来的是一个UserEntity对象，里面包含了该用户的信息
        // 通过@ModelAttribute()注解可以获取传递过来的'user'，并创建这个对象

        // 数据库中添加一个用户，该步暂时不会刷新缓存
        //userRepository.save(userEntity);



        // 重定向到用户管理页面，方法为 redirect:url
        return "admin/users";
    }

    @RequestMapping(value = "/admin/main/signupP", method = RequestMethod.POST)
    public String SignUp() {
        System.out.println("signup");
        return "admin/users";
    }

}
