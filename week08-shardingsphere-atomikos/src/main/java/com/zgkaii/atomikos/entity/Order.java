package com.zgkaii.atomikos.entity;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/05/25 13:55
 * @Description:
 **/
public class Order {
    private Integer orderId;
    private Integer userId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                '}';
    }
}
