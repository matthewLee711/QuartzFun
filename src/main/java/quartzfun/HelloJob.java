package quartzfun;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloJob.class);
    private static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    //Execute runs first and it activates every 5 seconds
    //When it is activated, it then goes to the context and gets the job detail.
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {

            Thread.sleep(5000);

            LOGGER.info(
                    "Hi!  I did this because I am a " +
                            context.getJobDetail().getJobDataMap().getString("DESCRIPTION") +
                            "  The current time is: " + df.format(Calendar.getInstance().getTime()));
            
            //Send Mail Repeatedly 
            sendMail(context.getJobDetail().getJobDataMap().getString("SMTPSERVER"),
            		context.getJobDetail().getJobDataMap().getString("TO"),
            		context.getJobDetail().getJobDataMap().getString("FROM"),
            		context.getJobDetail().getJobDataMap().getString("SUBJECT"),
            		context.getJobDetail().getJobDataMap().getString("TEXT"));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    
    
    public static void sendMail(
            String smtpServer,
            String to,
            String from,
            String subject,
            String text) throws AddressException, MessagingException{

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", smtpServer);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

       // Create a default MimeMessage object.
       MimeMessage message = new MimeMessage(session);

       // Set From: header field of the header.
       message.setFrom(new InternetAddress(from));

       // Set To: header field of the header.
       message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

       // Set Subject: header field
       message.setSubject(subject);

       // Now set the actual message
       message.setText(text);

       // Send message
       Transport.send(message);
       System.out.println("Sent message successfully....");

    }
    
    
    
    
    

}
