package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @Autor: GengJiawei
 * @Date :  2020/9/18 19:52
 * @PackageName: com.itheima.health.controller
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;



    @GetMapping("/findAll")
    public Result findAll(){

     List<CheckItem> list =  checkItemService.findAll();

     return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);

    }

    @PostMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){


        checkItemService.add(checkItem);

        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
      PageResult<CheckItem> pageResult = checkItemService.findPage(queryPageBean);

        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,pageResult);

    }

    @PostMapping("/deleteById")
    public Result deleteById(int id)   {
        checkItemService.deleteById(id);

        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @GetMapping("/findById")
    public Result findById(int id){
      CheckItem checkItem = checkItemService.findById(id);

      return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }

    @PostMapping("/update")
    public Result update(@RequestBody CheckItem checkItem){
    checkItemService.update(checkItem);

    return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }
}
