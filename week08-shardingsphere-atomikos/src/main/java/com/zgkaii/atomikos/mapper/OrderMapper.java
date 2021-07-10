package com.zgkaii.atomikos.mapper;

import com.zgkaii.atomikos.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/05/25 11:12
 * @Description:
 **/
@Mapper
public interface OrderMapper {
    List<Order> query();

    void insert();
}
