package com.zgkaii.mysql.mission09.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/05/13 14:59
 * @Description:
 **/
public class MyAbstractRoutingDataSource extends AbstractRoutingDataSource {
    @Value("${mysql.datasource.num}")
    private int num;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected Object determineCurrentLookupKey() {
        String typeKey = DbContextHolder.getDbType();
        if (typeKey == DbContextHolder.WRITE) {
            log.info("===> 使用了主库");
            return typeKey;
        }
        // 使用随机数决定使用哪个读库
        // 这里只配置了一个从库，实际开发中随机完全无法实现从库间的负载均衡
        int sum = getRandom(1, num);
        log.info("===> 使用了读库{}", sum);
        return DbContextHolder.READ + sum;
    }

    private static int getRandom(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1)) + min;
    }
}
