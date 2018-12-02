package com.cobee.quartzdemo.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/11/28.
 */
@Service
public class MyService {

    public void print(String content)
    {
        System.out.println(content);
    }

}
