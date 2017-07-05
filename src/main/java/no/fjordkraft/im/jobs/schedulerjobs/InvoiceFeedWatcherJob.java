package no.fjordkraft.im.jobs.schedulerjobs;

import com.carfey.ops.job.Context;
import com.carfey.ops.job.InterruptableJob;
import com.carfey.ops.job.param.Configuration;
import com.carfey.ops.job.param.Description;
import com.carfey.ops.job.param.Parameter;
import com.carfey.ops.job.param.Type;
import no.fjordkraft.im.jobs.domain.JobInfo;
import no.fjordkraft.im.jobs.domain.JobStatus;
import no.fjordkraft.im.services.TransferFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by bhavi on 5/9/2017.
 */

@JobInfo(name = "InvoiceFeedWatcherJob",
        schedule = "0/1 * * * ?",
        manualAllowed = true,
        checkForWorkingDay = false,
        editAllowed = true,
        status = JobStatus.disabled
)
@Description("Moves invoices from TransferFile table to SystemBatchInput")
@Configuration(knownParameters = {
        @Parameter(name = "jobhash", required = true, type = Type.STRING)
})
@Component
public class InvoiceFeedWatcherJob implements InterruptableJob {

    @Autowired
    private TransferFileService transferFileService;
    private static final Logger logger = LoggerFactory.getLogger(InvoiceFeedWatcherJob.class);

    public void execute(Context context) throws Exception {
        logger.info("InvoiceFeedWatcher job invoked ");
        transferFileService.fetchAndProcess();
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
