package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @Autor: GengJiawei
 * @Date :  2020/9/22 19:36
 * @PackageName: com.itheima.health.controller
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    private  static final Logger log =  LoggerFactory.getLogger(OrderSettingController.class);
    @Reference
    private OrderSettingService orderSettingService;

    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile){
        //读取excel内容
        try {
            List<String[]> strings = POIUtils.readExcel(excelFile);

            //转成LIST<ordersetting>
            List<OrderSetting> orderSettingList = new ArrayList<OrderSetting>(strings.size());
            //初始化ordersetting对象
            OrderSetting os = null;
            //通过simpledateformat 的到存在poiutils中的date格式
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            for (String[] string : strings) {
                //第一个数据为date的字符串
                Date orderDate = sdf.parse(string[0]);
                //第二个数据为数量
                os = new OrderSetting(orderDate, Integer.valueOf(string[1]));
                orderSettingList.add(os);
            }
            orderSettingService.add(orderSettingList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("批量导入失败",e);
        }
        return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);

    }

    @GetMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month){
        List<Map<String,Integer>> data = orderSettingService.getOrderSettingByMonth(month);
        return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,data);
    }

    @PostMapping("/editNumberByDate")

    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        orderSettingService.editNumberByDate(orderSetting);

        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }
}
