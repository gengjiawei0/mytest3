package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Autor: GengJiawei
 * @Date :  2020/9/23 20:28
 * @PackageName: com.itheima.health.controller
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealService setmealService;

    @GetMapping("/getSetmeal")
    public Result getSetmeal(){
        //查询所有套餐信息
    List<Setmeal> list = setmealService.findAll();

    //拼接套餐里的全路径
        list.forEach(setmeal -> {
            setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
        });

        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,list);
    }

    @GetMapping("/findDetailById")
    public Result findDetailById (int id){
        Setmeal setmeal =setmealService.findDetailById(id);

        setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());

        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
}
