package com.cobee.quartzdemo.job;

import com.cobee.quartzdemo.service.MyService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

/**
 * Created by Administrator on 2018/11/28.
 */
public class MyJob extends QuartzJobBean{

    @Autowired
    private MyService myService;

    private String name;
    private Integer age;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        myService.print(Thread.currentThread().getName() + ":hello " + name + ", age:" + age + " at datetime:" + LocalDateTime.now().toString());
    }

}
