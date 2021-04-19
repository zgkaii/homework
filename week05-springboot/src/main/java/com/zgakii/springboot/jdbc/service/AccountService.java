package com.zgakii.springboot.jdbc.service;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/19 22:53
 * @Description:
 **/
public interface AccountService {
    /**
     * 转账
     */
    void transfer(String outUser, String inUser, int money);
}
