package io.github.shanmugapriya03.quartzhelper.helpers;

import io.github.shanmugapriya03.quartzhelper.constants.*;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shanmugapriyar
 */
public class JobHelper {
    /**
     * Builds a Job with Identity name as Job Class name and Identity group as groupName_triggerType
     *
     * @param jobClass Job Class
     * @param groupName Name of group - to group jobs
     * @param triggerType Type of Trigger - SimpleTrigger/ CronTrigger
     * @return Returns JobDetail
     */
    public JobDetail buildJobDetail(final Class jobClass, final String groupName, final String triggerType){
        return JobBuilder
                .newJob(jobClass)
                .withIdentity(jobClass.getSimpleName(), groupName+"_"+triggerType)
                .build();
    }

    /**
     * Builds a durable Job with Identity name as Job Class name and Identity group as groupName_triggerType
     *
     * @param jobClass Job Class
     * @param groupName Name of group - to group jobs
     * @param triggerType Type of Trigger - SimpleTrigger/ CronTrigger
     * @return Returns durable JobDetail
     */
    public JobDetail buildDurableJobDetail(final Class jobClass, final String groupName, final String triggerType){
        return JobBuilder
                .newJob(jobClass)
                .withIdentity(jobClass.getSimpleName(), groupName+"_"+triggerType)
                .storeDurably()
                .build();
    }

    /**
     * Fetches the Job details of currently executing jobs
     *
     * @param scheduler instance of scheduler
     * @return List of JobDetail which are currently in execution
     */
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

    /**
     * Fetches the Job names of currently executing jobs
     *
     * @param scheduler instance of scheduler
     * @return List of Job Names which are currently in execution
     */
    public List<String> getCurrentlyExecutingJobNames(Scheduler scheduler){
        List<String> jobNames = new ArrayList<>();
        List<JobDetail> jobDetailsList = getCurrentlyExecutingJobDetails(scheduler);
        for (JobDetail jobDetail:jobDetailsList){
            jobNames.add(jobDetail.getJobClass().getName());
        }
        return jobNames;
    }

    /**
     * Pauses all the Jobs
     *
     * @param scheduler instance of scheduler
     */
    public void pauseAllJobs(Scheduler scheduler){
        try {
            scheduler.pauseAll();
        }catch (SchedulerException e){
            e.printStackTrace();
        }
    }

    /**
     * Resumes all the Jobs
     *
     * @param scheduler instance of scheduler
     */
    public void resumeAllJobs(Scheduler scheduler){
        try {
            scheduler.resumeAll();
        }catch (SchedulerException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates the state of the Job
     *
     * @param scheduler instance of scheduler
     * @param jobClass Job Class
     * @param groupName Name of group - to group jobs
     * @param jobState State of Job
     */
    private void updateJobState(Scheduler scheduler, final Class jobClass, final String groupName, final SchedulerConstants.JobState jobState) {
        final JobDetail jobDetail = buildJobDetail(jobClass,groupName, SchedulerConstants.TriggerType.CRON_TRIGGER);
        try {
            switch (jobState){
                case pause:
                    scheduler.pauseJob(jobDetail.getKey());
                    break;
                case resume:
                    scheduler.resumeJob(jobDetail.getKey());
                    break;
                case interrupt:
                    scheduler.interrupt(jobDetail.getKey());
                    break;
                case trigger:
                    final JobDetail durableJobDetail = buildDurableJobDetail(jobClass, groupName, SchedulerConstants.TriggerType.SIMPLE_TRIGGER);
                    scheduler.addJob(durableJobDetail, true);
                    scheduler.triggerJob(durableJobDetail.getKey());
                    break;
            }
        }catch (SchedulerException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates the state of the Job to Pause
     *
     * @param scheduler instance of scheduler
     * @param jobClass Job Class
     * @param groupName Name of group - to group jobs
     */
    public void updateJobStateToPause(Scheduler scheduler, final Class jobClass, final String groupName){
        updateJobState(scheduler,jobClass, groupName, SchedulerConstants.JobState.pause);
    }

    /**
     * Updates the state of the Job to Resume
     *
     * @param scheduler instance of scheduler
     * @param jobClass Job Class
     * @param groupName Name of group - to group jobs
     */
    public void updateJobStateToResume(Scheduler scheduler, final Class jobClass, final String groupName){
        updateJobState(scheduler,jobClass, groupName, SchedulerConstants.JobState.resume);
    }

    /**
     * Updates the state of the Job to Interrupt
     *
     * @param scheduler instance of scheduler
     * @param jobClass Job Class
     * @param groupName Name of group - to group jobs
     */
    public void updateJobStateToInterrupt(Scheduler scheduler, final Class jobClass, final String groupName){
        updateJobState(scheduler,jobClass, groupName, SchedulerConstants.JobState.interrupt);
    }

    /**
     * Updates the state of the Job to Trigger
     *
     * @param scheduler instance of scheduler
     * @param jobClass Job Class
     * @param groupName Name of group - to group jobs
     */
    public void updateJobStateToTrigger(Scheduler scheduler, final Class jobClass, final String groupName){
        updateJobState(scheduler,jobClass, groupName, SchedulerConstants.JobState.trigger);
    }

    /**
     * Updates the state of the Group
     *
     * @param scheduler instance of scheduler
     * @param groupName Name of group - to group jobs
     * @param groupState State of Group
     */
    private void updateGroupState(Scheduler scheduler, final String groupName, final SchedulerConstants.GroupState groupState) {
        GroupMatcher<JobKey> jobKeyGroupMatcher = GroupMatcher.jobGroupEquals(groupName+"_"+ SchedulerConstants.TriggerType.CRON_TRIGGER);
        try {
            switch (groupState) {
                case pause:
                    scheduler.pauseJobs(jobKeyGroupMatcher);
                    break;
                case resume:
                    scheduler.resumeJobs(jobKeyGroupMatcher);
                    break;
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the state of the Group to Pause
     * @param scheduler instance of scheduler
     * @param groupName Name of group - to group jobs
     */
    public void updateGroupStateToPause(Scheduler scheduler, final String groupName){
        updateGroupState(scheduler,groupName, SchedulerConstants.GroupState.pause);
    }

    /**
     * Updates the state of the Group to Resume
     * @param scheduler instance of scheduler
     * @param groupName Name of group - to group jobs
     */
    public void updateGroupStateToResume(Scheduler scheduler, final String groupName){
        updateGroupState(scheduler,groupName, SchedulerConstants.GroupState.resume);
    }
}
