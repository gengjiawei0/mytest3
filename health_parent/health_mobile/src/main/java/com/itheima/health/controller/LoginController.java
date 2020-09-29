package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.http.HttpResponse;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Autor: GengJiawei
 * @Date :  2020/9/26 0:03
 * @PackageName: com.itheima.health.controller
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @PostMapping("/check")
    public Result check(@RequestBody Map<String,String> loginInfo , HttpServletResponse res){
        //手机号
        String telephone = loginInfo.get("telephone");
        //验证码
        String validateCode = loginInfo.get("validateCode");
        //查询redis
        String key = RedisMessageConstant.SENDTYPE_LOGIN +"_"+ validateCode;

        Jedis jedis = jedisPool.getResource();

        String codeInRedis = jedis.get(key);

        //判断有无验证码
        if (null==codeInRedis){
            //失效或没有发送成功
            return new Result(false,"请重新获取验证码");
        }
        if (!codeInRedis.equals(validateCode)){
            return new Result(false,"验证码错误");
        }
        jedis.del(key);

        //判断是否为会员
        Member member = memberService.findByTelephone(telephone);

        if (null==member){
            //注册成为会员
            member = new Member();
            member.setRegTime(new Date());
            member.setRemark("手机快速注册");
            member.setPhoneNumber(telephone);
            memberService.add(member);
        }
        //跟踪手机号访问记录，代表会员访问记录
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        //保存时间
        cookie.setMaxAge(30*24*60*60);
        //访问的路径权限 "/"所有路径根路径下
        cookie.setPath("/");
        res.addCookie(cookie);

        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
