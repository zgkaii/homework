package com.zgkaii.mysql.mission09.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/05/13 14:48
 * @Description: 切换读/写模式
 * 利用ThreadLocal保存当前线程是否处于读模式（通过开始READ_ONLY注解在开始操作前设置模式为读模式，
 * 操作结束后清除该数据，避免内存泄漏，同时也为了后续在该线程进行写操作时任然为读模式。
 **/
public class DbContextHolder {

    private static Logger log = LoggerFactory.getLogger(DbContextHolder.class);
    public static final String WRITE = "write";
    public static final String READ = "read";

    private static ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDbType(String dbType) {
        if (dbType == null) {
            log.error("dbType为空");
            throw new NullPointerException();
        }
        log.info("设置dbType为：{}", dbType);
        contextHolder.set(dbType);
    }

    public static String getDbType() {
        return contextHolder.get() == null ? WRITE : contextHolder.get();
    }

    public static void clearDbType() {
        contextHolder.remove();
    }
}
