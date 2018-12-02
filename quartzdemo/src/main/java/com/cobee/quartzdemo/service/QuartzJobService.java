package com.cobee.quartzdemo.service;

import com.cobee.core.dto.QuartzJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by Administrator on 2018/12/2.
 */
@Service
public class QuartzJobService {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    public Date addJob(QuartzJob quartzJob) throws ClassNotFoundException, SchedulerException {
        // 1, 获取任务调度器
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        // 2，构建jobdetail对象
        Class<Job> jobClazz = (Class<Job>) Class.forName(quartzJob.getClazzName());
        JobBuilder jobBuilder = JobBuilder.newJob(jobClazz);
        jobBuilder = jobBuilder.storeDurably();
        if (StringUtils.hasText(quartzJob.getJobGroupName()))
        {
            jobBuilder = jobBuilder.withIdentity(quartzJob.getJobName(), quartzJob.getJobGroupName());
        }
        else
        {
            jobBuilder = jobBuilder.withIdentity(quartzJob.getJobName());
        }
        JobDetail jobDetail = jobBuilder.build();
        // 3, 构建trigger对象
        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger();
        if (StringUtils.hasText(quartzJob.getTriggerGroupName()))
        {
            triggerBuilder = triggerBuilder.withIdentity(quartzJob.getTriggerName(), quartzJob.getTriggerGroupName());
        }
        else
        {
            triggerBuilder = triggerBuilder.withIdentity(quartzJob.getTriggerName());
        }
        triggerBuilder = triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCronExp()));
        Trigger trigger = triggerBuilder.build();

        // 4, 使用scheduler调度任务
        return scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 定时任务删除
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     */
    public void deleteJob(String jobName,String jobGroupName,String triggerName,String triggerGroupName) {
		/*
		 1/ 获取到定时任务调度器.
		 2/ 停止触发器.
		 3/删除触发器.
		 4/删除任务.
		 */
        // 1/ 获取到定时任务调度器.
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        try {
            // 2/ 停止触发器.
            TriggerKey triggerKey  = TriggerKey.triggerKey(triggerName, triggerGroupName);
            scheduler.pauseTrigger(triggerKey);

            // 3/删除触发器.
            scheduler.unscheduleJob(triggerKey);

            // 4/删除任务.
            JobKey jobKey = JobKey.jobKey(jobName,jobGroupName);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改定时任务的时间.
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     */
    public void modifyJobTime(String jobName,String jobGroupName,String triggerName,String triggerGroupName,String cronExpression) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
		/*
		 * 第一种方式： 删除&添加
		 * 在删除之前就需要先获取到JobDetail,否者就无法获取了.
		 */
		/*try {
			JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
			deleteJob(jobName, jobGroupName, triggerName, triggerGroupName);
			addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobDetail.getJobClass(), cronExpression);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}*/


        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName,triggerGroupName);
        try {
            CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
            String oldCronExpression = trigger.getCronExpression();
            if(oldCronExpression.equals(cronExpression)) {
                return;
            }

            trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

    /**
     * 启动所有的定时任务.
     * 这个是配合standby一起使用的，
     * 当job处于standby模式的时候，调用startJobs可以恢复定时任务.
     */
    public void startJobs() {
        Scheduler scheduler  = schedulerFactoryBean.getScheduler();
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭所有的定时任务.
     * 注意：一旦被shutdown之后，就不能调用startJobs.
     */
    public void shutdownJobs() {
        Scheduler scheduler  = schedulerFactoryBean.getScheduler();
        try {
            if(!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将定时任务设置为待机或者备用状态，暂停的模式.
     * 可以使用start进行恢复.
     */
    public void standby() {
        Scheduler scheduler  = schedulerFactoryBean.getScheduler();
        try {
            scheduler.standby();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
