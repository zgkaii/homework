package com.zgkaii.mysql.mission02;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/05/11 14:46
 * @Description:
 **/
public class InsertData {

    /**
     * 普通插入
     */
    public void insert1() {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO orders(orderid, proid, userid, payment, paymenttype, postage, ostatus, paymenttime) VALUES (?, ?, ?, ?, 1, ?, 2, ?);";
            pstm = conn.prepareStatement(sql);
            long startTime = System.currentTimeMillis();
            Random random = new Random();
            for (int i = 1; i <= 10000; i++) {
                pstm.setString(1, String.valueOf(i));
                pstm.setString(2, String.valueOf(random.nextInt(20)));
                pstm.setInt(3, i);
                pstm.setBigDecimal(4, BigDecimal.valueOf(2, random.nextInt(20)));
                pstm.setBigDecimal(5, BigDecimal.valueOf(2, random.nextInt(5)));
                pstm.setDate(6, java.sql.Date.valueOf(java.time.LocalDate.now()));
                pstm.executeUpdate();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Insert data success! Running time: " + (endTime - startTime));
        } catch (Exception e) {
            System.out.println("Insert data failure!");
            e.printStackTrace();
        } finally {
            releaseResources(pstm, conn);
        }
    }

    /**
     * 事务提交
     */
    public void insert2() {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO orders(orderid, proid, userid, payment, paymenttype, postage, ostatus, paymenttime) VALUES (?, ?, ?, ?, 1, ?, 2, ?);";
            pstm = conn.prepareStatement(sql);
            conn.setAutoCommit(false);
            long startTime = System.currentTimeMillis();
            Random random = new Random();
            for (int i = 1; i <= 100000; i++) {
                pstm.setString(1, String.valueOf(i));
                pstm.setString(2, String.valueOf(random.nextInt(20)));
                pstm.setInt(3, i);
                pstm.setBigDecimal(4, BigDecimal.valueOf(2, random.nextInt(20)));
                pstm.setBigDecimal(5, BigDecimal.valueOf(2, random.nextInt(5)));
                pstm.setDate(6, java.sql.Date.valueOf(java.time.LocalDate.now()));
                pstm.executeUpdate();
            }
            conn.commit();
            long endTime = System.currentTimeMillis();
            System.out.println("Insert data success! Running time: " + (endTime - startTime));
        } catch (Exception e) {
            System.out.println("Insert data failure!");
            e.printStackTrace();
        } finally {
            releaseResources(pstm, conn);
        }
    }

    /**
     * 批量处理
     */
    public void insert3() {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO orders(orderid, proid, userid, payment, paymenttype, postage, ostatus, paymenttime) VALUES (?, ?, ?, ?, 1, ?, 2, ?)";
            pstm = conn.prepareStatement(sql);
            conn.setAutoCommit(false);// 改进三：批量处理+事务
            long startTime = System.currentTimeMillis();
            Random random = new Random();
            for (int i = 1; i <= 1000000; i++) {
                pstm.setString(1, String.valueOf(i));
                pstm.setString(2, String.valueOf(random.nextInt(20)));
                pstm.setInt(3, i);
                pstm.setBigDecimal(4, BigDecimal.valueOf(2, random.nextInt(20)));
                pstm.setBigDecimal(5, BigDecimal.valueOf(2, random.nextInt(5)));
                pstm.setDate(6, java.sql.Date.valueOf(java.time.LocalDate.now()));
                // 积攒SQL
                pstm.addBatch();
                // 积攒到一定程度执行一次，清空先前"积攒"的SQL
                if (i % 500 == 0) {
                    pstm.executeBatch();
                    pstm.clearBatch();
                }
            }
            conn.commit();//改进三：批量处理+事务
            long endTime = System.currentTimeMillis();
            System.out.println("Insert data success! Running time: " + (endTime - startTime));
        } catch (Exception e) {
            System.out.println("Insert data failure!");
            e.printStackTrace();
        } finally {
            releaseResources(pstm, conn);
        }
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        // mysql注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://192.168.99.228:3307/learn?useServerPrepStmts=true&rewriteBatchedStatements=true";
//        String url = "jdbc:mysql://192.168.99.228:3307/learn?rewriteBatchedStatements=true";
//        String url = "jdbc:mysql://192.168.99.228:3307/learn";
        String username = "zgkaii";
        String password = "513701";
        // 获取连接对象
        Connection conn = DriverManager.getConnection(url, username, password);
        if (conn != null) {
            System.out.println("Database connection success!");
        }
        return conn;
    }

    private void releaseResources(PreparedStatement pstm, Connection conn) {
        // 释放资源
        if (pstm != null) {
            try {
                pstm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
