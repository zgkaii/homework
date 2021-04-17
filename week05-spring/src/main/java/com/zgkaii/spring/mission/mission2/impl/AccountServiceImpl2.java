package com.zgkaii.spring.mission.mission2.impl;

import com.zgkaii.spring.mission.mission2.IAccountService;

import java.util.Date;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/17 21:46
 * @Description:
 **/
public class AccountServiceImpl2 implements IAccountService {
    private String name;
    private Integer age;
    private Date birthday;

    public AccountServiceImpl2(String name, Integer age, Date birthday) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
    }

    @Override
    public void saveAccount() {
        System.out.println(name + "," + age + "," + birthday);
    }
}
