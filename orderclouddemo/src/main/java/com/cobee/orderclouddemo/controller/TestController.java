package com.cobee.orderclouddemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/12/3.
 */
@RestController
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping(value = "getServiceList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ServiceInstance> getServiceList()
    {
        List<String> nameList = discoveryClient.getServices();
        if (!CollectionUtils.isEmpty(nameList))
        {
            List<ServiceInstance> serviceInstanceList = new ArrayList<>();
            for (String name : nameList)
            {
                serviceInstanceList.addAll(discoveryClient.getInstances(name));
            }
            return serviceInstanceList;
        }
        return Collections.<ServiceInstance>emptyList();
    }

}
