package com.zgakii.springboot.jdbc.service.impl;

import com.zgakii.springboot.jdbc.dao.AccountDao;
import com.zgakii.springboot.jdbc.service.AccountService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/19 22:54
 * @Description:
 **/
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
public class AccountServiceImpl implements AccountService {
    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void transfer(String outUser, String inUser, int money) {
        this.accountDao.out(outUser, money);
        this.accountDao.in(inUser, money);
    }
}
