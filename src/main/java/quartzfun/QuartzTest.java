package quartzfun;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzTest.class);

    public static void main(String[] args) {

        try {

            LOGGER.info("Starting scheduler...");
            Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
            sched.start();


            JobDetail jobSimple = JobBuilder
                    .newJob(HelloJob.class)
                    .withIdentity("HelloJobRepeated", "group1")
                    .usingJobData("DESCRIPTION", "Simple Job")
                    .build();

            //Run every 5 seconds using the schedule builder
            Trigger simpleTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("HelloRepeater", "group1")
                    .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(5).repeatForever())
                    .build();

            sched.scheduleJob(jobSimple, simpleTrigger);



            JobDetail jobCron = JobBuilder
                    .newJob(HelloJob.class)
                    .withIdentity("HelloJobScheduled", "group1")
                    .usingJobData("DESCRIPTION", "Cron Job")
                    .build();

            //Run every 10 seconds using Cron style scheduling
            //http://www.quartz-scheduler.org/documentation/quartz-2.x/tutorials/crontrigger.html
            Trigger cronTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("HelloTimed", "group1")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
                    .build();

            sched.scheduleJob(jobCron, cronTrigger);

            //You need to use a host other than localhost if you are not running a local SMTP server
//            try{
//                MailUtil.sendMail(
//                    "localhost",
//                    "email@email.com",
//                    "email@email.com",
//                    "test message",
//                    "This is a test of the emergency email system!");
//            }  catch (AddressException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (MessagingException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }

//            LOGGER.info("Shutting down scheduler...");
//            sched.shutdown();

        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
