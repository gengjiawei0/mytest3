package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Autor: GengJiawei
 * @Date :  2020/9/21 19:46
 * @PackageName: com.itheima.health.controller
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/upload")
    public Result upload (MultipartFile imgFile){
        //得到原有图片名称
        String originalFilename = imgFile.getOriginalFilename();

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        //生成唯一文件名，拼写后缀
        String filename = UUID.randomUUID() + extension;

        //调用七牛上传文件
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),filename);


            //- 返回数据给页面
            //{
            //    flag:
            //    message:
            //    data:{
            //        imgName: 图片名,
            //        domain: QiNiuUtils.DOMAIN
            //    }
            //}

            Map<String,String> map = new HashMap<String,String>();

            map.put("imgName",filename);
            map.put("domain",QiNiuUtils.DOMAIN);

            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);

    }

    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){

        Integer setmealId = setmealService.add(setmeal, checkgroupIds);

        //添加redis的套餐静态页面
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        jedis.sadd(key,setmealId+"|1|"+System.currentTimeMillis());
        jedis.close();

        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @PostMapping("/findPage")
    public Result findPage (@RequestBody QueryPageBean queryPageBean){

        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);

        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,pageResult);
    }
}
