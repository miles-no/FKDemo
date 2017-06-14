package no.fjordkraft.im.jobs.schedulerjobs;

import com.carfey.ops.job.Context;
import com.carfey.ops.job.InterruptableJob;
import com.carfey.ops.job.param.Configuration;
import com.carfey.ops.job.param.Description;
import com.carfey.ops.job.param.Parameter;
import com.carfey.ops.job.param.Type;
import no.fjordkraft.im.jobs.domain.JobInfo;
import no.fjordkraft.im.jobs.domain.JobStatus;
import no.fjordkraft.im.services.PDFGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by miles on 5/12/2017.
 */
@JobInfo(name = "PDFGeneratorJob",
        schedule = "0/1 * * * ?",
        manualAllowed = true,
        checkForWorkingDay = false,
        editAllowed = true,
        status = JobStatus.enabled
)
@Description("Generates Invoice PDF through Birt Runtime Engine.")
@Configuration(knownParameters = {
        @Parameter(name = "jobhash", required = true, type = Type.STRING)
})
@Component
public class PDFGeneratorJob implements InterruptableJob{
    private static final Logger logger = LoggerFactory.getLogger(PDFGeneratorJob.class);

    @Autowired
    PDFGenerator pdfGenerator;

    public void execute(Context context) throws InterruptedException {
        logger.debug("PDFGenerator job invoked " );
        pdfGenerator.generateInvoicePDF();
    }

    @Override
    public void beforeInterrupt() {

    }
}
