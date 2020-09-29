package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Autor: GengJiawei
 * @Date :  2020/9/28 18:21
 * @PackageName: com.itheima.health.security
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //根据传入的姓名查找会员信息
        com.itheima.health.pojo.User user = userService.findByUsername(username);
        //如果会员存在
        if(null!=user){
        //得到密码
            String password = user.getPassword();
            //用list集合存储权限
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

            //找到GrantedAuthority 接口的实现类SimpleGrantedAuthority 去接受所拥有的权限和角色
            SimpleGrantedAuthority authority = null;

            Set<Role> roles = user.getRoles();

            if (null!=roles){
                for (Role role : roles) {
                    authority = new SimpleGrantedAuthority(role.getKeyword());

                    authorities.add(authority);

                    //得到roles下的所有权限 并且添加到权限集合中
                    Set<Permission> permissions = role.getPermissions();
                    if (null!=permissions){
                        for (Permission permission : permissions) {
                        authority = new SimpleGrantedAuthority(permission.getKeyword());

                        authorities.add(authority);
                    }
                    }

                }
            }

            return new User(username,password,authorities);

        }

        //返回空值，限制访问
        return null;
    }
}
