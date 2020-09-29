package com.itheima.health.service;

import com.itheima.health.pojo.User;

/**
 * @Autor: GengJiawei
 * @Date :  2020/9/28 18:45
 * @PackageName: com.itheima.health.service
 */
public interface UserService {

    //根据姓名查权限
    User findByUsername(String username);
}
