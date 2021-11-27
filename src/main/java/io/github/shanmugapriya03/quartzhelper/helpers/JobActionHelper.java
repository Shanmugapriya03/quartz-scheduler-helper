package io.github.shanmugapriya03.quartzhelper.helpers;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shanmugapriyar
 */
public class JobActionHelper {
    public List<JobDetail> getCurrentlyExecutingJobDetails(Scheduler scheduler){
        List<JobDetail> jobDetailsList = new ArrayList<>();
        try {
            List<JobExecutionContext> jobExecutionContextList = scheduler.getCurrentlyExecutingJobs();
            for (JobExecutionContext jobExecutionContext : jobExecutionContextList){
                jobDetailsList.add(jobExecutionContext.getJobDetail());
            }
        } catch (SchedulerException e){
            e.printStackTrace();
        }
        return jobDetailsList;
    }

    public List<String> getCurrentlyExecutingJobNames(Scheduler scheduler){
        List<String> jobNames = new ArrayList<>();
        List<JobDetail> jobDetailsList = getCurrentlyExecutingJobDetails(scheduler);
        for (JobDetail jobDetail:jobDetailsList){
            jobNames.add(jobDetail.getJobClass().getName());
        }
        return jobNames;
    }
}
