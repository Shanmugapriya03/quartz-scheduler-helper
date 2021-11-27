package io.github.shanmugapriya03.quartzhelper.helpers;

import org.quartz.*;

/**
 * @author shanmugapriyar
 */
public class JobHelper {
    public JobDetail buildJobDetail(final Class jobClass, final String groupName, final String triggerType){
        return JobBuilder
                .newJob(jobClass)
                .withIdentity(jobClass.getSimpleName(), groupName+"_"+triggerType)
                .build();
    }

    public JobDetail buildDurableJobDetail(final Class jobClass, final String groupName, final String triggerType){
        return JobBuilder
                .newJob(jobClass)
                .withIdentity(jobClass.getSimpleName(), groupName+"_"+triggerType)
                .storeDurably()
                .build();
    }
}
