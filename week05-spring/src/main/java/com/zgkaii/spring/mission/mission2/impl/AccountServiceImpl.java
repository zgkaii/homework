package com.zgkaii.spring.mission.mission2.impl;

import com.zgkaii.spring.mission.mission2.IAccountService;

import java.util.Date;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/17 21:19
 * @Description: 账户的业务层实现类
 **/
public class AccountServiceImpl implements IAccountService {
    private String name;
    private Integer age;
    private Date birthday;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public void saveAccount() {
        System.out.println(name + "," + age + "," + birthday);
    }
}
