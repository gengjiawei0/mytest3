package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @Autor: GengJiawei
 * @Date :  2020/9/22 19:54
 * @PackageName: com.itheima.health.service
 */
public interface OrderSettingService {

    void add(List<OrderSetting> orderSettingList) throws HealthException;

    List<Map<String, Integer>> getOrderSettingByMonth(String month);

    void editNumberByDate(OrderSetting orderSetting)throws HealthException;
}
