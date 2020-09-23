package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Autor: GengJiawei
 * @Date :  2020/9/22 19:56
 * @PackageName: com.itheima.health.service.impl
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Override
    @Transactional
    public void add(List<OrderSetting> orderSettingList) throws HealthException {
        if (null != orderSettingList &&orderSettingList.size()>0){
            for (OrderSetting orderSetting : orderSettingList) {
                //通过日期判断数据库中是否有数据
             OrderSetting osInDB   = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
             //如果有 先判断想改的最大预约数量是否大于数据库中已经预约的数量
             if (null != osInDB){
                 //如果小 抛出异常
                 if (orderSetting.getNumber()<osInDB.getReservations()){
                     throw new HealthException("最大预约数量不能小于已经预约的数量");
                 }
                 orderSettingDao.updateNumber(orderSetting);
             }else {
                 //如果数据库中没有数据，则直接添加数据到数据库中
                 orderSettingDao.add(orderSetting);
             }
            }
        }
    }

    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String month) {
    //拼接每个月开始第一天和最后一天进行范围查询
        String startDate = month+"-01";
        String endDate = month+"-31";

       return orderSettingDao.getOrderSettingBetween(startDate,endDate);

    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting)throws HealthException {
        //通过日期查询预约设置信息
        OrderSetting os = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());

        if (null!=os){
            if (orderSetting.getNumber()<os.getReservations()){
                throw new HealthException("设置异常！最大预约数量不能小于已预约数量");
            }
            orderSettingDao.updateNumber(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }

    }
}
