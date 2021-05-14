package com.zgkaii.mysql;

import com.zgkaii.mysql.mission02.InsertData;
import com.zgkaii.mysql.mission09.entity.Student;
import com.zgkaii.mysql.mission09.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class Week07MysqlApplicationTests {

    @Test
    void testInsert() {
        InsertData insertData = new InsertData();
//        insertData.insert1();
//        insertData.insert2();
        insertData.insert3();
    }

    @Autowired
    private StudentService studentService;

    @Test
    public void writeAndRead() {
        Student student = new Student();
        student.setId(4);
        student.setName("mazi");
        studentService.insert(student);
        List<Student> studentList = studentService.query();
        System.out.println("===> 读取数据");
        System.out.println(studentList.toString());
    }
}
