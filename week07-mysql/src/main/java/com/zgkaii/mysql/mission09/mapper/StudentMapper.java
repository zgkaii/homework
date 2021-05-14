package com.zgkaii.mysql.mission09.mapper;

import com.zgkaii.mysql.mission09.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/05/13 15:56
 * @Description:
 **/
@Repository
public interface StudentMapper {
    /**
     * 获取全部数据
     */
    List<Student> query();

    /**
     * 插入一条数据
     */
    void insert(Student student);
}
