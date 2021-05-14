package com.zgkaii.mysql.mission09.service;

import com.zgkaii.mysql.mission09.config.ReadOnly;
import com.zgkaii.mysql.mission09.entity.Student;
import com.zgkaii.mysql.mission09.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/05/13 15:57
 * @Description:
 **/
@Service
public class StudentService {

    @Autowired
    StudentMapper studentMapper;

    /**
     * 从库 读取所有数据
     *
     * @return
     */
    @ReadOnly
    public List<Student> query() {
        return studentMapper.query();
    }

    /**
     * 主库 插入数据
     */
    public void insert(Student student) {
        studentMapper.insert(student);
    }
}
