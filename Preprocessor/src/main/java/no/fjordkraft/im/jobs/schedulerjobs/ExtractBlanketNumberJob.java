package no.fjordkraft.im.jobs.schedulerjobs;

import com.carfey.ops.job.Context;
import com.carfey.ops.job.InterruptableJob;
import com.carfey.ops.job.param.Configuration;
import com.carfey.ops.job.param.Description;
import com.carfey.ops.job.param.Parameter;
import com.carfey.ops.job.param.Type;
import no.fjordkraft.im.jobs.domain.JobInfo;
import no.fjordkraft.im.jobs.domain.JobStatus;
import no.fjordkraft.im.services.BlanketNumberService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/24/18
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
@JobInfo(name = "ExtractBlanketNumberJob",
        schedule = "0/1 * * * ?",
        manualAllowed = true,
        checkForWorkingDay = false,
        editAllowed = true,
        status = JobStatus.disabled

)
@Description("Reads the blanket numbers from table and check the date if the active blanket number is expired then reactivate another blanket number which are in inactive state. The validity period is configured in configuration table.")
@Configuration(knownParameters = {
        @Parameter(name = "jobhash", required = true, type = Type.STRING)
})
@Component
public class ExtractBlanketNumberJob implements InterruptableJob {


    @Autowired
    BlanketNumberService blanketNumberService;

    private static final Logger logger = LoggerFactory.getLogger(InvoiceFeedWatcherJob.class);

    @Override
    public void beforeInterrupt() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void execute(Context context) throws Exception {
       logger.debug("ExtractBlanketInvoiceNumber job invoked ");
       blanketNumberService.extractBlanketNumber();

    }
}
