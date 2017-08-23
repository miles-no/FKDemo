package no.fjordkraft.im.jobs.schedulerjobs;

import com.carfey.ops.job.Context;
import com.carfey.ops.job.InterruptableJob;
import com.carfey.ops.job.param.Configuration;
import com.carfey.ops.job.param.Description;
import com.carfey.ops.job.param.Parameter;
import com.carfey.ops.job.param.Type;
import no.fjordkraft.im.jobs.domain.JobInfo;
import no.fjordkraft.im.jobs.domain.JobStatus;
import no.fjordkraft.im.services.StatementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by bhavi on 5/9/2017.
 */
@JobInfo(name = "InvoiceExtractorJob",
        schedule = "0/1 * * * ?",
        manualAllowed = true,
        checkForWorkingDay = false,
        editAllowed = true,
        status = JobStatus.disabled
)
@Description("Reads single invoice and splits into multiple statement and saves in statements table")
@Configuration(knownParameters = {
        @Parameter(name = "jobhash", required = true, type = Type.STRING)
})
@Component
public class InvoiceExtractorJob  implements InterruptableJob {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceExtractorJob.class);

    @Autowired
    StatementService statementService;

    public void execute(Context context) {
        logger.debug("InvoiceExtractor job invoked statementService.fetchAndSplit");
        // statementService.fetchAndSplit();
    }

    @Override
    public void beforeInterrupt() {

    }
}
