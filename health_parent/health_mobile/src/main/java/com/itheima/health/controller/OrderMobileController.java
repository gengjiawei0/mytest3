package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Autor: GengJiawei
 * @Date :  2020/9/25 18:05
 * @PackageName: com.itheima.health.controller
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,String> orderInfo){
        //  验证前端提交过来的验证码与redis的验证码是否一致
        Jedis jedis = jedisPool.getResource();

        //  1. 先从redis中取出验证码codeInRedis key=手机号码
        String telephone = orderInfo.get("telephone");
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        //  2. codeInRedis没值，提示用户重新获取验证码
        String codeInRdeis = jedis.get(key);
        if (null==codeInRdeis){
            return new Result(false, "重新发送验证码");
        }
        //  3. codeInRedis有值, 则比较前端的验证码是否一致
        if (!codeInRdeis.equals(orderInfo.get("validateCode"))){
            //  不一样，则返回验证码错误
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
        //  一样，则继续执行调用服务的方法预约
        jedis.del(key);
        //  4. 返回订单信息给页面
        orderInfo.put("orderType", Order.ORDERTYPE_WEIXIN );

        Order order = orderService.submitOrder(orderInfo);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order);

    }

    @GetMapping("/findById")
    public Result findById(int id){
        Map<String, Object> orderInfo = orderService.findOrderDetailById(id);

        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,orderInfo);
    }
}
