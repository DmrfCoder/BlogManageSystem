
# BlogManageSystem



[TOC]
## 概述
### 开发背景
随着一个个开源社区的不断成长，越来越多的技术人员在各种论坛发表自己的技术博客（blog），本系统希望实现一个团队博客管理的目的，即将一个团队成员的所有博客进行统一管理，有利于团队内部人员的技术交流和共同成长。
### 开发目标
通过本系统，使用者可以看到团队中所有的成员，管理员登录后可以对团队的人员进行编辑和管理。同时，管理员可以对系统成员的博客进行查看和管理。

### 参考资料
springmvc官方文档：http://projects.spring.io/spring-framework/ 
springmvc主要框架介绍：http://spring.io/projects 
一位大牛的教程：https://my.oschina.net/gaussik/blog

## 需求分析
### 需求陈述

+ 管理员的注册以及登录
+ 用户的管理
+ 博客的管理
### 操作用例
#### 管理员注册与登录
用户进入该模块之后只需输入nickname和密码然后点击登录即可进行登录
，若需要注册只需点击对应的注册button进入注册页面，注册页面与登录页面想死，主要区别是后端的处理。
#### 用户信息管理
这个模块首先使用表格的形式展示出当前系统所的用户信息，其次，对每一个用户都应该有详情、修改、删除三个按钮以管理用户。另外，在本页面还需有一个增加用户的button用于管理员增加用户。
#### 博客信息管理
本页面以表格的形式列出当前系统的所有博客，博客信息包括博客的标题、作者、发布日期，对于每一篇博客应该留有详情、修改、删除三大按钮，另外在本页面还需设置一个添加博客的按钮以添加新博客。

### 功能分析划分
划分为登录注册、用户管理、博客管理三大功能模块

#### 系统登录注册
实现管理员登录和注册，注册并登录成功后默认进入用户管理模块，可以看到当前系统的所有用户信息并对其进行管理。
#### 用户管理
实现用户信息的显示、添加、删除、修改功能。
#### 博客管理
实现博客的显示、添加、删除、修改功能。

### 运行环境
Ubuntu16.04+Intellij IDEA+tomcat8.5+MySQL
## 总体设计
### UML类图
![1](https://ws2.sinaimg.cn/large/006tNc79ly1fzywg081pmj312h0ohgn7.jpg)

### 界面设计

#### 登录界面设计
![2](https://ws1.sinaimg.cn/large/006tNc79ly1fzywgup849j30zd0qxaai.jpg)

#### 注册界面设计
![3](https://ws3.sinaimg.cn/large/006tNc79ly1fzywh1k7a4j30zj0qzjrv.jpg)

#### 用户管理界面设计
![4](https://ws3.sinaimg.cn/large/006tNc79ly1fzywhdz5xbj30zi0b40tk.jpg)

#### 博客管理界面设计
![5](https://ws1.sinaimg.cn/large/006tNc79ly1fzywhieqinj30zj08n751.jpg)

### 数据库结构设计
#### user（用户）

Column|Type|Nullable|Auto_increment
---------|-------|-----------|-----------------
id|int(11)|NO|YES
nickname|varchar(45)|NO|NO
password|varchar(45)|NO|NO
first_name|varchar(45)|YES|NO
last_name|varchar(45)|YES|NO

#### blog(博客)

Column|Type|Nullable|Auto_increment
---------|-------|-----------|-----------------
id|int(11)|NO|YES
title|varchar(100)|NO|NO
content|varchar(255)|YES|NO
user_id|int(11)|NO|NO
pub_date|date|NO|NO

#### manager(管理员)

Column|Type|Nullable|Auto_increment
---------|-------|-----------|-----------------
id|int(11)|NO|YES
nickname|varchar(45)|NO|NO
password|varchar(45)|NO|NO


#### 数据库E-R图
![6](https://ws4.sinaimg.cn/large/006tNc79ly1fzywhnjci1j30b00ff0tg.jpg)


## 详细设计
### 程序流程图
![7](https://ws1.sinaimg.cn/large/006tNc79ly1fzywhz038lj30eq0qdq37.jpg)

### 实现
#### controller

+ BlogController

```java
package com.dmrf.controller;

import com.dmrf.model.BlogEntity;
import com.dmrf.model.UserEntity;
import com.dmrf.repository.BlogRepository;
import com.dmrf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by dmrf on 2018/5/10.
 */
@Controller
public class BlogController {

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;

    // 查看所有博文
    @RequestMapping(value = "/admin/blogs", method = RequestMethod.GET)
    public String showBlogs(ModelMap modelMap) {
        List<BlogEntity> blogList = blogRepository.findAll();
        modelMap.addAttribute("blogList", blogList);
        return "admin/blogs";
    }

    // 添加博文
    @RequestMapping(value = "/admin/blogs/add", method = RequestMethod.GET)
    public String addBlog(ModelMap modelMap) {
        List<UserEntity> userList = userRepository.findAll();
        // 向jsp注入用户列表
        modelMap.addAttribute("userList", userList);
        return "admin/addBlog";
    }

    // 添加博文，POST请求，重定向为查看博客页面
    @RequestMapping(value = "/admin/blogs/addP", method = RequestMethod.POST)
    public String addBlogPost(@ModelAttribute("blog") BlogEntity blogEntity) {
        // 打印博客标题
        System.out.println(blogEntity.getTitle());
        // 打印博客作者
        System.out.println(blogEntity.getUserByUserId().getNickname());
        // 存库
        blogRepository.saveAndFlush(blogEntity);
        // 重定向地址
        return "redirect:/admin/blogs";
    }

    // 查看博文详情，默认使用GET方法时，method可以缺省
    @RequestMapping("/admin/blogs/show/{id}")
    public String showBlog(@PathVariable("id") int id, ModelMap modelMap) {
        BlogEntity blog = blogRepository.findOne(id);
        modelMap.addAttribute("blog", blog);
        return "admin/blogDetail";
    }

    // 修改博文内容，页面
    @RequestMapping("/admin/blogs/update/{id}")
    public String updateBlog(@PathVariable("id") int id, ModelMap modelMap) {
        // 是不是和上面那个方法很像
        BlogEntity blog = blogRepository.findOne(id);
        List<UserEntity> userList = userRepository.findAll();
        modelMap.addAttribute("blog", blog);
        modelMap.addAttribute("userList", userList);
        return "admin/updateBlog";
    }

    // 修改博客内容，POST请求
    @RequestMapping(value = "/admin/blogs/updateP", method = RequestMethod.POST)
    public String updateBlogP(@ModelAttribute("blogP") BlogEntity blogEntity) {
        // 更新博客信息
        System.out.println(blogEntity.getTitle());
        blogRepository.updateBlog(blogEntity.getTitle(), blogEntity.getUserByUserId().getId(),
                blogEntity.getContent(), blogEntity.getPubDate(), blogEntity.getId());
        blogRepository.flush();
        return "redirect:/admin/blogs";
    }

    // 删除博客文章
    @RequestMapping("/admin/blogs/delete/{id}")
    public String deleteBlog(@PathVariable("id") int id) {
        blogRepository.delete(id);
        blogRepository.flush();
        return "redirect:/admin/blogs";
    }
}

```
+ UserController

```java
package com.dmrf.controller;

import com.dmrf.model.UserEntity;
import com.dmrf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by dmrf on 2018/5/10.
 */
@Controller
public class UserController {

    // 自动装配数据库接口，不需要再写原始的Connection来操作数据库
    @Autowired
    UserRepository userRepository;




    @RequestMapping(value = "/admin/users", method = {RequestMethod.GET,RequestMethod.POST})
    public String getUsers(ModelMap modelMap) {
        // 查询user表中所有记录
        List<UserEntity> userList = userRepository.findAll();

        // 将所有记录传递给要返回的jsp页面，放在userList当中
        modelMap.addAttribute("userList", userList);

        // 返回pages目录下的admin/users.jsp页面
        return "admin/users";
    }

    // get请求，访问添加用户 页面
    @RequestMapping(value = "/admin/users/add", method = RequestMethod.GET)
    public String addUser() {
        // 返回 admin/addUser.jsp页面
        return "admin/addUser";
    }



    // post请求，处理添加用户请求，并重定向到用户管理页面
    @RequestMapping(value = "/admin/users/addP", method = RequestMethod.POST)
    public String addUserPost(@ModelAttribute("user") UserEntity userEntity) {
        // 注意此处，post请求传递过来的是一个UserEntity对象，里面包含了该用户的信息
        // 通过@ModelAttribute()注解可以获取传递过来的'user'，并创建这个对象

        // 数据库中添加一个用户，该步暂时不会刷新缓存
        //userRepository.save(userEntity);
        System.out.println(userEntity.getFirstName());
        System.out.println(userEntity.getLastName());

        // 数据库中添加一个用户，并立即刷新缓存
        userRepository.saveAndFlush(userEntity);

        // 重定向到用户管理页面，方法为 redirect:url
        return "redirect:/admin/users";
    }

    // 查看用户详情
    // @PathVariable可以收集url中的变量，需匹配的变量用{}括起来
    // 例如：访问 localhost:8080/admin/users/show/1 ，将匹配 id = 1
    @RequestMapping(value = "/admin/users/show/{id}", method = RequestMethod.GET)
    public String showUser(@PathVariable("id") Integer userId, ModelMap modelMap) {

        // 找到userId所表示的用户
        UserEntity userEntity = userRepository.findOne(userId);

        // 传递给请求页面
        modelMap.addAttribute("user", userEntity);
        return "admin/userDetail";
    }

    // 更新用户信息 页面
    @RequestMapping(value = "/admin/users/update/{id}", method = RequestMethod.GET)
    public String updateUser(@PathVariable("id") Integer userId, ModelMap modelMap) {

        // 找到userId所表示的用户
        UserEntity userEntity = userRepository.findOne(userId);

        // 传递给请求页面
        modelMap.addAttribute("user", userEntity);
        return "admin/updateUser";
    }

    // 更新用户信息 操作
    @RequestMapping(value = "/admin/users/updateP", method = RequestMethod.POST)
    public String updateUserPost(@ModelAttribute("userP") UserEntity user) {

        // 更新用户信息
        userRepository.updateUser(user.getNickname(), user.getFirstName(),
                user.getLastName(), user.getPassword(), user.getId());
        userRepository.flush(); // 刷新缓冲区
        return "redirect:/admin/users";
    }

    // 删除用户
    @RequestMapping(value = "/admin/users/delete/{id}", method = RequestMethod.GET)
    public String deleteUser(@PathVariable("id") Integer userId) {

        // 删除id为userId的用户
        userRepository.delete(userId);
        // 立即刷新
        userRepository.flush();
        return "redirect:/admin/users";
    }
}

```
+ MainController

```java
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

```
#### model

+ BlogEntity

```java
package com.dmrf.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dmrf on 2018/5/10.
 */
@Entity
@Table(name = "blog", schema = "springdemo", catalog = "")
public class BlogEntity {
    private int id;
    private String title;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date pubDate;
    private UserEntity userByUserId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "content", nullable = true, length = 255)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "pub_date", nullable = false)
    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogEntity that = (BlogEntity) o;

        if (id != that.id) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (pubDate != null ? !pubDate.equals(that.pubDate) : that.pubDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (pubDate != null ? pubDate.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public UserEntity getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(UserEntity userByUserId) {
        this.userByUserId = userByUserId;
    }
}

```

+ ManagerEntity

```java
package com.dmrf.model;

import javax.persistence.*;
import java.util.Objects;

//@NamedQuery(name = "ManagerEntity.queryManager", query = "SELECT mg FROM ManagerEntity mg WHERE mg.nickname AND mg.password")
@Entity
@Table(name = "manager", schema = "springdemo", catalog = "")
public class ManagerEntity {
    private int id;
    private String nickname;
    private String password;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nickname", nullable = false, length = 45)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 45)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManagerEntity that = (ManagerEntity) o;
        return id == that.id &&
                Objects.equals(nickname, that.nickname) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, nickname, password);
    }
}

```

+ UserEntity

```java
package com.dmrf.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by dmrf on 2018/5/10.
 */
@Entity
@Table(name = "user", schema = "springdemo", catalog = "")
public class UserEntity {
    private int id;
    private String nickname;
    private String password;
    private String firstName;
    private String lastName;
    private Collection<BlogEntity> blogsById;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nickname", nullable = false, length = 45)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 45)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "first_name", nullable = true, length = 45)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name", nullable = true, length = 45)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (id != that.id) return false;
        if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<BlogEntity> getBlogsById() {
        return blogsById;
    }

    public void setBlogsById(Collection<BlogEntity> blogsById) {
        this.blogsById = blogsById;
    }
}

```
#### repository

+ BlogRepository

```java
package com.dmrf.repository;

import com.dmrf.model.BlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by dmrf on 2018/5/10.
 */
@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Integer> {

    // 修改博文操作
    @Modifying
    @Transactional
    @Query("update BlogEntity blog set blog.title=:qTitle, blog.userByUserId.id=:qUserId," +
            " blog.content=:qContent, blog.pubDate=:qPubDate where blog.id=:qId")
    void updateBlog(@Param("qTitle") String title, @Param("qUserId") int userId, @Param("qContent") String content,
                    @Param("qPubDate") Date pubDate, @Param("qId") int id);
}

```

+ ManagerRepository

```java
package com.dmrf.repository;

import com.dmrf.model.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dmrf on 2018/5/10.
 */
@Repository
public interface ManagerRepository extends JpaRepository<ManagerEntity, Integer> {

    @Modifying      // 说明该方法是修改操作
    @Transactional  // 说明该方法是事务性操作
    // 定义查询
    // @Param注解用于提取参数
    @Query("update ManagerEntity  us set us.nickname=:qNickname,  us.password=:qPassword where us.id=:qId")
    public void updateManager(@Param("qNickname") String nickname, @Param("qPassword") String password, @Param("qId") Integer id);
    @Query("SELECT mg FROM ManagerEntity mg WHERE mg.nickname=:qNickname AND mg.password=:qPassword")
    public ManagerEntity queryManager(@Param("qNickname") String nickname, @Param("qPassword") String password);
}

```

+ UserRepository

```java
package com.dmrf.repository;

import com.dmrf.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dmrf on 2018/5/10.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Modifying      // 说明该方法是修改操作
    @Transactional  // 说明该方法是事务性操作
    // 定义查询
    // @Param注解用于提取参数
    @Query("update UserEntity us set us.nickname=:qNickname, us.firstName=:qFirstName, us.lastName=:qLastName, us.password=:qPassword where us.id=:qId")
    public void updateUser(@Param("qNickname") String nickname, @Param("qFirstName") String firstName,
                           @Param("qLastName") String qLastName, @Param("qPassword") String password, @Param("qId") Integer id);
}

```

## 测试
### 注册测试
![8](https://ws1.sinaimg.cn/large/006tNc79ly1fzywoitpflj30x30bct8x.jpg)

### 登录测试
![9](https://ws1.sinaimg.cn/large/006tNc79ly1fzywoucigrj30w60bxjro.jpg)
### 添加用户测试
![10](https://ws3.sinaimg.cn/large/006tNc79ly1fzywp17u81j30xl0c0dg8.jpg)
![11](https://ws1.sinaimg.cn/large/006tNc79ly1fzywp9bajzj30wc08ydgm.jpg)

### 删除用户测试
![13](https://ws4.sinaimg.cn/large/006tNc79ly1fzywrbhg7gj30y0081dgh.jpg)
### 查看用户详情测试
![14](https://ws4.sinaimg.cn/large/006tNc79ly1fzywvhsjppj30xc08ddg6.jpg)
### 添加博客测试
![15](https://ws2.sinaimg.cn/large/006tNc79ly1fzywwbrrg0j30yc0d3mxq.jpg)
![16](https://ws2.sinaimg.cn/large/006tNc79ly1fzywwhn4bpj30yp08ogme.jpg)

### 删除博客测试
![17](https://ws2.sinaimg.cn/large/006tNc79ly1fzywxxem4tj30xe07374u.jpg)
### 查看博客详情测试
![18](https://ws1.sinaimg.cn/large/006tNc79ly1fzywyv1cdlj30x709mq3t.jpg)

## 总结
### 对mvc模式的理解和感悟
MVC 模式代表 Model-View-Controller（模型-视图-控制器） 模式。这种模式用于应用程序的分层开发。

+ Model（模型） - 模型代表一个存取数据的对象或 JAVA POJO。它也可以带有逻辑，在数据变化时更新控制器。

+ View（视图） - 视图代表模型包含的数据的可视化。

+ Controller（控制器） - 控制器作用于模型和视图上。它控制数据流向模型对象，并在数据变化时更新视图。它使视图与模型分离开。

在本项目中使用mvc模式，具体的model为三个Entity，controller为三个controller，view则是不同的jsp页面。具体机制是Event(事件)导致Controller改变Model或View，或者同时改变两者。只要 Controller改变了Model的数据或者属性，所有依赖的View都会自动更新。类似的，只要Controller改变了View，View会从潜在的Model中获取数据来刷新自己。通过这种模式，我们可以将系统的不同模块分离开来，对每一个功能模块分别进行开发和修改，各个模块之间层次分明，利于维护和团队交接开发。

