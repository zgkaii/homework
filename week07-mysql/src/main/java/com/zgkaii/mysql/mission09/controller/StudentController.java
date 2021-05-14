package com.zgkaii.mysql.mission09.controller;

import com.zgkaii.mysql.mission09.entity.Student;
import com.zgkaii.mysql.mission09.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/05/13 15:57
 * @Description:
 **/
@RestController
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping("/getAll")
    public Object getAll() {
        return studentService.query();
    }

    @GetMapping("/insert")
    public void insert(Student student) {
        studentService.insert(student);
    }
}
