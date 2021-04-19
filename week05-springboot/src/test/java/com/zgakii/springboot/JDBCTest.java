package com.zgakii.springboot;

import com.zgakii.springboot.jdbc.UserService;
import com.zgakii.springboot.jdbc.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/19 21:19
 * @Description:
 **/
public class JDBCTest {
    @Test
    public void testSave() throws Exception {
        UserService service = new UserService();
        service.save();
    }

    @Test
    public void testQuery() throws Exception {
        UserService service = new UserService();
        service.query();
    }

    @Test
    void testAnnotation() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
        AccountService accountService = (AccountService) applicationContext
                .getBean("accountService");
        accountService.transfer("zhangsan", "lisi", 100);
        accountService.transfer("wang", "lisi", 200);
    }
}
