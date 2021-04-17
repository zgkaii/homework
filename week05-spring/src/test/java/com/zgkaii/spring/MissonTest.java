package com.zgkaii.spring;

import com.zgkaii.spring.mission.mission2.IAccountService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/17 20:52
 * @Description:
 **/
public class MissonTest {

    @Test
    public void testString() {
        // 定义Spring配置文件的路径
        String xmlPath = "applicationContext.xml";
        // 初始化Spring容器，加载配置文件
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                xmlPath);
        // 通过容器获取IAccountService实例
//        IAccountService as = (IAccountService) applicationContext
//                .getBean("accountService1");
//        IAccountService as = (IAccountService) applicationContext
//                .getBean("accountService2");
        IAccountService as = (IAccountService) applicationContext
                .getBean("accountService3");
        // 调用IAccountService的saveAccount方法
        as.saveAccount();
    }
}
