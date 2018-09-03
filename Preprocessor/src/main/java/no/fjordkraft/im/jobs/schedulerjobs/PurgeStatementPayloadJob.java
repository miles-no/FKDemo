package no.fjordkraft.im.jobs.schedulerjobs;

import com.carfey.ops.job.Context;
import com.carfey.ops.job.InterruptableJob;
import com.carfey.ops.job.param.Configuration;
import com.carfey.ops.job.param.Description;
import com.carfey.ops.job.param.Parameter;
import com.carfey.ops.job.param.Type;
import no.fjordkraft.im.jobs.domain.JobInfo;
import no.fjordkraft.im.jobs.domain.JobStatus;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.services.InvoiceService;
import no.fjordkraft.im.services.StatementPayloadService;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 7/25/18
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
@JobInfo(name = "PurgeStatementPayloadJob",
        schedule = "30 23 * * *",
        manualAllowed = true,
        checkForWorkingDay = false,
        editAllowed = true,
        status = JobStatus.enabled

)
@Description("This Job will purge statement payload before no of days. Number of days is configurable and can set the value in IM_CONFIG table.")
@Configuration(knownParameters = {
        @Parameter(name = "jobhash", required = true, type = Type.STRING)
})
@Component
public class PurgeStatementPayloadJob implements InterruptableJob
{

    private static final Logger logger = LoggerFactory.getLogger(PurgeStatementPayloadJob.class);

    @Autowired
    ConfigService configService;

    @Autowired
    StatementPayloadService statementPayloadService;

    @Override
    public void beforeInterrupt() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void execute(Context context) throws Exception {
        int noOfDays = 7;
        try {
            logger.debug("PurgeStatementPayloadJob job invoked ");

            if(null != configService.getInteger(IMConstants.DELETE_STATEMENT_PAYLOAD_BEFORE_DAYS)) {
                noOfDays = configService.getInteger(IMConstants.DELETE_STATEMENT_PAYLOAD_BEFORE_DAYS);
            }
            logger.debug("No of Days before StatementPayload to be deleted " + noOfDays);
            Date today = new Date(System.currentTimeMillis());
            Date tillDate =  DateUtils.addDays(today, -noOfDays);
            logger.debug("Till date ",tillDate);
            int recordsDeleted =  statementPayloadService.deleteStatementPayloadTillDate(tillDate);
            logger.info("Statement Payload deleted " + recordsDeleted);
        } catch (Exception e)
        {
            logger.error("Exception while purging Statement Payload "+  e);
        }
    }
}
