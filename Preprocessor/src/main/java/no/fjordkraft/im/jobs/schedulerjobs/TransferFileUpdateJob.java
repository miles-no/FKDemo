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
import no.fjordkraft.im.services.TransferFileService;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by bhavi on 9/27/2017.
 */

@JobInfo(name = "TransferFileUpdateJob",
        schedule = "0/5 * * * ?",
        manualAllowed = true,
        checkForWorkingDay = false,
        editAllowed = true,
        status = JobStatus.enabled
)
@Description("Updates the status of transferfile")
@Configuration(knownParameters = {
        @Parameter(name = "jobhash", required = true, type = Type.STRING)
})
@Component
public class TransferFileUpdateJob implements InterruptableJob{

    @Autowired
    private TransferFileService transferFileService;

    @Autowired
    ConfigService configService;

    private static final Logger logger = LoggerFactory.getLogger(InvoiceFeedWatcherJob.class);

    public void execute(Context context) throws Exception {

        if(configService.getBoolean(IMConstants.TRANSFERFILE_UPDATE_JOB_ACTIVE)) {
            //logger.info("TransferFileUpdateJob active and job invoked ");
            transferFileService.consolidateAndUpdateStatus();
        } else {
            logger.info("TransferFileUpdateJob job is inactive ");
        }
    }

    @Override
    public void beforeInterrupt() {

    }

    public TransferFileService getTransferFileService() {
        return transferFileService;
    }

    public void setTransferFileService(TransferFileService transferFileService) {
        this.transferFileService = transferFileService;
    }
}