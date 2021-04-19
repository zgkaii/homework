package com.zgakii.springboot.jdbc.dao;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/19 22:48
 * @Description:
 **/
public interface AccountDao {
    /**
     * 取款
     */
    public void out(String outUser, int money);

    /**
     * 存款
     */
    public void in(String inUser, int money);
}
