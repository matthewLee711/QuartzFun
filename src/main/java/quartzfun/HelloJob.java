package quartzfun;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloJob.class);
    private static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {

            Thread.sleep(5000);

            LOGGER.info(
                    "Hi!  I did this because I am a " +
                            context.getJobDetail().getJobDataMap().getString("DESCRIPTION") +
                            "  The current time is: " + df.format(Calendar.getInstance().getTime()));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
