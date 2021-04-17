package com.zgkaii.spring.mission.mission2.impl;

import com.zgkaii.spring.mission.mission2.IAccountService;
import lombok.Data;

import java.util.*;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/17 21:55
 * @Description:
 **/
@Data
public class AccountServiceImpl3 implements IAccountService {

    private String[] myStrs;
    private List<String> myList;
    private Set<String> mySet;
    private Map<String, String> myMap;
    private Properties myProps;

    @Override
    public void saveAccount() {
        System.out.println(Arrays.toString(myStrs));
        System.out.println(myList);
        System.out.println(mySet);
        System.out.println(myMap);
        System.out.println(myProps);
    }
}
