package com.zgkaii.jvm.demo.demo6;

import sun.security.ec.CurveDB;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/03/22 21:41
 * @Description:
 **/
public class ClassLoaderTest {
    public static void main(String[] args) {
        System.out.println("*********扩展类加载器************");
        String extDirs = System.getProperty("java.ext.dirs");
        for (String path : extDirs.split(";")) {
            System.out.println(path);
        }

        // Java\lib\ext目录下随意选择一个类，查看他的类加载器是什么
        ClassLoader classLoader = CurveDB.class.getClassLoader();
        System.out.println(classLoader);
    }
}
