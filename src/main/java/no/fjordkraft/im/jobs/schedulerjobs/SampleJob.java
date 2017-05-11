package no.fjordkraft.im.jobs.schedulerjobs;

import com.carfey.ops.job.Context;
import com.carfey.ops.job.InterruptableJob;
import com.carfey.ops.job.param.Configuration;
import com.carfey.ops.job.param.Description;
import com.carfey.ops.job.param.Parameter;
import com.carfey.ops.job.param.Type;
import no.fjordkraft.im.jobs.domain.JobInfo;
import no.fjordkraft.im.jobs.domain.JobStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by bhavi on 5/5/2017.
 */

@JobInfo(name = "SampleJob",
        schedule = "0/1 * * * ?",
        manualAllowed = true,
        checkForWorkingDay = false,
        editAllowed = true,
        status = JobStatus.enabled
)
@Description("Checks for new emails in email-table, sends emails and updates the status in the email table")
@Configuration(knownParameters = {
        @Parameter(name = "jobhash", required = true, type = Type.STRING)
})
@Component
public class SampleJob implements InterruptableJob {

    //@Value("${destinationPath}")
    //private String destinationPath;

    public void execute(Context context) throws Exception {
        System.out.println("Sample Job called "+new Date());
        //System.out.println("Path is "+ destinationPath);
        //while(true) {
            System.out.println("sample job "+ Thread.currentThread().getName());
          //  Thread.sleep(60000);
        //}

    }

    @Override
    public void beforeInterrupt() {
    }



}