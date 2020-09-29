package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    OrderSetting findByOrderDate(Date orderDate);

    void updateNumber(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);
    //传入的两个数据都是基础数据
    List<Map<String, Integer>> getOrderSettingBetween(@Param("startDate") String startDate,@Param("endDate") String endDate);

    int updateReservations(OrderSetting orderSetting);
}
