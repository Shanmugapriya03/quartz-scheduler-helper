package io.github.shanmugapriya03.quartzhelper.helpers;

import io.github.shanmugapriya03.quartzhelper.model.TimerInfo;
import org.quartz.*;

import java.util.Date;

/**
 * @author shanmugapriyar
 */
public class TriggerHelper {
    /**
     * Builds a Simple Trigger with Identity name as Job Class name and Identity group as triggerType
     *
     * @param jobClass Job Class
     * @param timerInfo timer details to build Job - refer io/github/shanmugapriya03/quartzhelper/model/TimerInfo.java
     * @param triggerType Type of Trigger - SimpleTrigger/ CronTrigger
     * @return returns Simple Trigger
     */
    public Trigger buildSimpleTrigger(final Class jobClass, final TimerInfo timerInfo, final String triggerType){
        SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(timerInfo.getRepeatIntervalMs());
        if (timerInfo.isRunForever()){
            builder = builder.repeatForever();
        }else {
            builder = builder.withRepeatCount(timerInfo.getTotalFireCount()-1);
        }
        return TriggerBuilder
                .newTrigger()
                .withIdentity(jobClass.getSimpleName(), triggerType)
                .withSchedule(builder)
                .startAt(new Date(System.currentTimeMillis() + timerInfo.getInitialOffsetMs()))
                .build();
    }

    /**
     * Builds a Cron Trigger with Identity name as Job Class name and Identity group as triggerType
     *
     * @param jobClass Job Class
     * @param cronExpression cron expression
     * @param triggerType Type of Trigger - SimpleTrigger/ CronTrigger
     * @return returns Cron Trigger
     */
    public Trigger buildCronTrigger(final Class jobClass, final String cronExpression, final String triggerType){
        CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule(cronExpression);
        return TriggerBuilder
                .newTrigger()
                .withIdentity(jobClass.getSimpleName(), triggerType)
                .withSchedule(builder)
                .build();
    }
}
