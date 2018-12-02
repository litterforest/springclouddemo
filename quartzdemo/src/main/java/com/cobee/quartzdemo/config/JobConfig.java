package com.cobee.quartzdemo.config;

import com.cobee.quartzdemo.job.MyJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2018/11/28.
 */
@Configuration
public class JobConfig {

    @Bean
    public JobDetail myJobDetail()
    {
        JobBuilder jobBuilder = null;
        jobBuilder = JobBuilder.newJob(MyJob.class);
        jobBuilder = jobBuilder.storeDurably();
        jobBuilder = jobBuilder.withIdentity(MyJob.class.getName() + "Detail");
        jobBuilder = jobBuilder.usingJobData("name", "陈a森");
        jobBuilder = jobBuilder.usingJobData("age", 30);
        return jobBuilder.build();
    }

    @Bean
    public Trigger myJobTrigger(JobDetail myJobDetail)
    {
        TriggerBuilder triggerBuilder = null;
        triggerBuilder = TriggerBuilder.newTrigger();
        triggerBuilder = triggerBuilder.forJob(myJobDetail);
        triggerBuilder = triggerBuilder.withIdentity(MyJob.class.getName() + "Trigger");
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForever(3);
        triggerBuilder.withSchedule(simpleScheduleBuilder);
        return triggerBuilder.build();
    }

}
