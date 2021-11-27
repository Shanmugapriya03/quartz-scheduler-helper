package io.github.shanmugapriya03.quartzhelper.constants;

/**
 * @author shanmugapriyar
 */
public class SchedulerConstants {
    public enum JobState {
        trigger,
        pause,
        resume,
        interrupt
    }

    public enum GroupState {
        pause,
        resume
    }

    public static class TriggerType {
        public static final String SIMPLE_TRIGGER = "SimpleTrigger";
        public static final String CRON_TRIGGER = "CronTrigger";
    }
}
