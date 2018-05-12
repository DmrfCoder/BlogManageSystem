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
        return "admin/signup";
    }

    @RequestMapping(value = "/admin/manager/signupP", method = RequestMethod.POST)
    public String addManagerPost(@ModelAttribute("manager") ManagerEntity managerEntity) {
        // 注意此处，post请求传递过来的是一个ManagerEntity对象，里面包含了该用户的信息
        // 通过@ModelAttribute()注解可以获取传递过来的'manager'，并创建这个对象

        // 数据库中添加一个用户，该步暂时不会刷新缓存
//        userRepository.save(userEntity);
        System.out.println(managerEntity.getNickname());
        System.out.println(managerEntity.getPassword());
        // 存库
        managerRepository.saveAndFlush(managerEntity);


        // 重定向到首页
        return "admin/blogs";
    }

    @RequestMapping(value = "/admin/signupP", method = RequestMethod.GET)
    public String SignUp() {
        System.out.println("signup");
        return "admin/signup";
    }

    @RequestMapping(value = "/admin/loginP", method = RequestMethod.POST)
    public String Login(@ModelAttribute("manager") ManagerEntity managerEntity) {
        System.out.println(managerEntity.getNickname());
        System.out.println(managerEntity.getPassword());

        // 找到userId所表示的用户
        ManagerEntity managerEntity1 = managerRepository.queryManager(managerEntity.getNickname(), managerEntity.getPassword());

        if(managerEntity1 == null) {return "error";}
        return "admin/blogs";
    }
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(){
        return "hello";
    }

}
