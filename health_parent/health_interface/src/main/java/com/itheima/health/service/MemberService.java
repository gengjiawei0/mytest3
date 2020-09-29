package com.itheima.health.service;

import com.itheima.health.pojo.Member;

public interface MemberService {

    //根据手机号查询会员
    Member findByTelephone(String telephone);
    //新增会员
    void add(Member member);
}
