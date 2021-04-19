package com.zgakii.springboot.jdbc;

import java.sql.*;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/19 21:17
 * @Description: 原生JDBC连接数据库
 **/
public class UserService {
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        // mysql注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String username = "root";
        String password = "513701";
        // 获取连接对象
        Connection conn = DriverManager.getConnection(url, username, password);
        if (conn != null) {
            System.out.println("Database connection success!");
        }
        return conn;
    }

    /**
     * 插入
     */
    public void save() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            System.out.println("默认事务隔离级别" + conn.getTransactionIsolation());
            // 创建执行SQL语句的Statement对象
            stmt = conn.createStatement();
            // 创建并执行SQL语句
            stmt.execute("insert account values (3,'wang',2000)");
            System.out.println("Insert data success!");

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally { // 释放资源
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection terminated!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 查询
     */
    public void query() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            String query = "select * from account";
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String uername = rs.getString("username");
                String money = rs.getString("money");
                System.out.println("id: " + id + " name: " + uername + " money: " + money);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection terminated!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
