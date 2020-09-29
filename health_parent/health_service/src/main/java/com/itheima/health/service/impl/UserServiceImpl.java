package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.UserDao;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Autor: GengJiawei
 * @Date :  2020/9/28 18:47
 * @PackageName: com.itheima.health.service.impl
 */
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    //根据姓名查权限
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
