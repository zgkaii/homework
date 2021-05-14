package com.zgkaii.mysql.mission09.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/05/13 16:03
 * @Description: 通过该接口注释的service使用读模式，其他使用写模式
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadOnly {
}
