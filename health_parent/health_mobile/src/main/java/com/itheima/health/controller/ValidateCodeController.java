package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Autor: GengJiawei
 * @Date :  2020/9/25 17:47
 * @PackageName: com.itheima.health.controller
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/send4Order")
    public Result send4Order(String telephone){
        //生成redis
        Jedis jedis = jedisPool.getResource();

        String key = RedisMessageConstant.SENDTYPE_ORDER+"_"+telephone;

        //redis中的验证码
        String codeInRedis = jedis.get(key);

        if (null==codeInRedis){
            //不存在，生成验证码并发送
            Integer code = ValidateCodeUtils.generateValidateCode(6);

            //发送
            try {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code+"");

                //存入redis
                jedis.setex(key,10*60,code+"");

                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (ClientException e) {
                e.printStackTrace();
                return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
            }
        }

        //验证码已经存在
        return new Result(false,"验证码已经发送，请注意查收");
    }

    @PostMapping("/send4Login")
    public Result send4Login(String telephone){
        //生成redis
        Jedis jedis = jedisPool.getResource();

        String key = RedisMessageConstant.SENDTYPE_LOGIN+"_"+telephone;

        //redis中的验证码
        String codeInRedis = jedis.get(key);

        if (null==codeInRedis){
            //不存在，生成验证码并发送
            Integer code = ValidateCodeUtils.generateValidateCode(6);

            //发送
            try {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code+"");

                //存入redis
                jedis.setex(key,10*60,code+"");

                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (ClientException e) {
                e.printStackTrace();
                return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
            }
        }

        //验证码已经存在
        return new Result(false,"验证码已经发送，请注意查收");
    }
}
