package io.github.shanmugapriya03.quartzhelper.helpers;

import io.github.shanmugapriya03.quartzhelper.model.TimerInfo;
import org.quartz.*;

import java.util.Date;

/**
 * @author shanmugapriyar
 */
public class TriggerHelper {
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

    public Trigger buildCronTrigger(final Class jobClass, final String cronExpression, final String triggerType){
        CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule(cronExpression);
        return TriggerBuilder
                .newTrigger()
                .withIdentity(jobClass.getSimpleName(), triggerType)
                .withSchedule(builder)
                .build();
    }
}
