package quartzfun;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

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

            //Create a job for which Execute will check.
            //JobDetail jobSimple = JobBuilder
            //        .newJob(HelloJob.class)
            //        .withIdentity("HelloJobRepeated", "group1")
            //        .usingJobData("DESCRIPTION", "Simple Job")//This is grabbed and displayed as text 
            //        .build();
            
            //Create the timer for jobSimple. Right above^^
            //Run every 5 seconds using the schedule builder
            Trigger simpleTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("HelloRepeater", "group1")
                    .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(5).repeatForever())
                    .build();

            //sched.scheduleJob(jobSimple, simpleTrigger);
            
            //MailUtil Job setup!
            JobDetail jobMail = JobBuilder
            		.newJob(MailUtil.class)
                    .withIdentity("HelloJobScheduler", "group1")
                    .usingJobData("DESCRIPTION", "Cron Job")
                    .usingJobData("SMTPSERVER", "localhost")
            		.usingJobData("TO", "matthew_lee2@homedepot.com")
            		.usingJobData("FROM", "matthew_lee2@homedepot.com")
            		.usingJobData("SUBJECT", "A message from an SMTP server1212!")
            		.usingJobData("TEXT", "Yay")
                    .build();
            
            Trigger mailTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("HelloTimed", "group1")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
                    .build();

            sched.scheduleJob(jobMail, mailTrigger);
            
            

            //Create a job for which Execute will check
            JobDetail jobCron = JobBuilder
                    .newJob(HelloJob.class)
                    .withIdentity("HelloJobScheduled", "group1")
                    .usingJobData("DESCRIPTION", "Cron Job")
                    .usingJobData("SMTPSERVER", "localhost")
            		.usingJobData("TO", "matthew_lee2@homedepot.com")
            		.usingJobData("FROM", "matthew_lee2@homedepot.com")
            		.usingJobData("SUBJECT", "A message from an SMTP server!")
            		.usingJobData("TEXT", "I")
                    .build();

            //Run every 10 seconds using Cron style^^^ scheduling for jobDetail jobcron 
            //http://www.quartz-scheduler.org/documentation/quartz-2.x/tutorials/crontrigger.html
            Trigger cronTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("HelloTimed", "group1")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
                    .build();

            sched.scheduleJob(jobCron, cronTrigger);
            
            //TO run this, create server, install and configure PostFix on linux as
            //You need to use a host other than localhost if you are not running a local SMTP server
            try{
                MailUtil.sendMail(
                    "localhost",
                    "matthew_lee2@homedepot.com",
                    "matthew_lee2@homedepot.com",
                    "test message",
                    "This is a test of the emergency email system! From Quartz!!");
            }  catch (AddressException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (MessagingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

//            LOGGER.info("Shutting down scheduler...");
//            sched.shutdown();

        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
