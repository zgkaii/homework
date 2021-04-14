package com.zgkaii.spring.demo.spring02;

import com.zgkaii.spring.demo.spring01.Student;
import lombok.Data;

import java.util.List;

@Data
public class Klass { 
    
    List<Student> students;
    
    public void dong(){
        System.out.println(this.getStudents());
    }
    
}
