package com.itheima.health.dao;

import com.itheima.health.pojo.User;

/**
 * @Autor: GengJiawei
 * @Date :  2020/9/28 18:48
 * @PackageName: com.itheima.health.dao
 */
public interface UserDao {
    User findByUsername(String username);
}
