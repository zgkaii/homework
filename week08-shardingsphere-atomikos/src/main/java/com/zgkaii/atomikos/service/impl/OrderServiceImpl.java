package com.zgkaii.atomikos.service.impl;

import com.zgkaii.atomikos.mapper.OrderMapper;
import com.zgkaii.atomikos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/05/25 11:12
 * @Description:
 **/
@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    OrderMapper orderMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//    @ShardingTransactionType(TransactionType.XA)
//    public void insert01() {
//        for (int i = 1; i < 10; i++) {
//            Order order = new Order();
//            order.setOrderId(1);
//            order.setUserId(i);
//            orderMapper.insert();
//        }
//    }
//
//    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//    @ShardingTransactionType(TransactionType.XA)
//    public void insert02() {
//        for (long i = 1; i < 10; i++) {
//            String insertSql = "INSERT INTO t_order(order_id,user_id) values (?,?)";
//            long finalI = i;
//            jdbcTemplate.execute(insertSql, (PreparedStatementCallback<Object>) ps -> {
//                ps.setLong(1, 111);
//                ps.setLong(2, finalI);
//                ps.executeUpdate();
//                return 1;
//            });
//        }
//    }

    @Override
    public void query() {

    }

    @Override
    public void insert() {

    }
}

