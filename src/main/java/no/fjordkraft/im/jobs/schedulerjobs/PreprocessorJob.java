package no.fjordkraft.im.jobs.schedulerjobs;

import com.carfey.ops.job.Context;
import com.carfey.ops.job.InterruptableJob;
import com.carfey.ops.job.param.Configuration;
import com.carfey.ops.job.param.Description;
import com.carfey.ops.job.param.Parameter;
import com.carfey.ops.job.param.Type;
import no.fjordkraft.im.jobs.domain.JobInfo;
import no.fjordkraft.im.jobs.domain.JobStatus;
import no.fjordkraft.im.preprocess.services.PreprocessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by bhavi on 5/10/2017.
 */

@JobInfo(name = "PreprocessorJob",
        schedule = "0/1 * * * ?",
        manualAllowed = true,
        checkForWorkingDay = false,
        editAllowed = true,
        status = JobStatus.enabled
)
@Description("Preprocess statements")
@Configuration(knownParameters = {
        @Parameter(name = "jobhash", required = true, type = Type.STRING)
})
@Component
public class PreprocessorJob implements InterruptableJob {

    private static final Logger logger = LoggerFactory.getLogger(PreprocessorJob.class);

    @Autowired
    PreprocessorService preprocessorService;

    @Override
    public void beforeInterrupt() {

    }

    @Override
    public void execute(Context context) throws Exception {
        logger.debug("Preprocessor job invoked");
        preprocessorService.preprocess();
    }
}
