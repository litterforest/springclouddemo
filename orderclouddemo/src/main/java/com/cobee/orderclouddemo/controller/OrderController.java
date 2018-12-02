package com.cobee.orderclouddemo.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/12/1.
 */
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @GetMapping(value = "/getOrder")
    public String getOrder(Long id)
    {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "order " + id + " 's detail";
    }

    @GetMapping(value = "/job/clearOrderStatusJob")
    public String clearOrderStatusJob()
    {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "success";
    }

}
