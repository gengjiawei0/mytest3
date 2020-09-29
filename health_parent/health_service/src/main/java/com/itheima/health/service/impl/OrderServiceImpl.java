package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Autor: GengJiawei
 * @Date :  2020/9/25 18:40
 * @PackageName: com.itheima.health.service.impl
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;

    @Autowired
     private  OrderSettingDao orderSettingDao;
    @Override
    @Transactional
    public Order submitOrder(Map<String, String> orderInfo) throws HealthException{
        // 1. 通过日期查询预约设置信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date orderDate = null;
        String orderDate1 = orderInfo.get("orderDate");
        try {
             orderDate = sdf.parse(orderDate1);
        } catch (ParseException e) {
            //e.printStackTrace();
            throw new HealthException("日期格式不正确，请选择正确的日期");
        }
        // ordersettingDao
        // 预约日期 前端来
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        // 如果不存在则报错 throw new healthException
        if (null==orderSetting){
            throw  new HealthException("所选日期不能预约，请选择其他日期");
        }
        // 存在：判断预约已满，满了则报错 throw new healthException
        if (orderSetting.getReservations()>=orderSetting.getNumber()){
        throw new HealthException("所选日期已经选满，请选择其他日期");
        }
        // 2. 判断是否重复预约
        String telephone = orderInfo.get("telephone");
        // 通过手机号查询会员信息
        Member member =memberDao.findByTelephone(telephone);
        // memberDao.findByTelephone
        //  存在才需要判断是否重复预约
        // orderDao
        Order order = new Order();
        order.setOrderDate(orderDate);
        order.setSetmealId(Integer.valueOf(orderInfo.get("setmealId")));
        // 不存在
        if (null!=member){
            order.setMemberId(member.getId());
            // 查询t_order, 条件orderDate=? and setmeal_id=?,member=?
            List<Order> orderList = orderDao.findByCondition(order);
            if (null!=orderList&&orderList.size()>0){
                throw new HealthException("套餐已经预约，请勿重复预约");
            }
        }else {
            member = new Member();
            member.setName(orderInfo.get("name"));
            member.setSex(orderInfo.get("sex"));
            member.setPhoneNumber(telephone);
            member.setIdCard(orderInfo.get("idCard"));
            member.setRegTime(new Date());
            member.setPassword("12345678");
            member.setRemark("预约直接注册");
            //  添加会员
            memberDao.add(member);
            // memberDao.add
            order.setMemberId(member.getId());
        }
        // 3. 可预约
        order.setOrderType(orderInfo.get("orderType"));
        // 添加t_order 预约信息
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        // orderDao.add
        orderDao.add(order);
        // 4. 更新已预约人数
       int affectedCount= orderSettingDao.updateReservations(orderSetting);
       if (affectedCount==0){
           throw new HealthException(MessageConstant.ORDER_FULL);
       }
        // 5. 返回新添加的订单对象
        return order;
    }

    @Override
    public Map<String, Object> findOrderDetailById(int id) {
        return orderDao.findById4Detail(id);
    }
}
