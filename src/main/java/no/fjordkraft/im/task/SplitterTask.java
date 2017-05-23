package no.fjordkraft.im.task;

import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.statusEnum.SystemBatchInputStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by bhavi on 5/19/2017.
 */
@Service
@Scope("prototype")
public class SplitterTask implements Runnable{

    SystemBatchInput systemBatchInput;

    @Autowired
    StatementService statementService;

    public SplitterTask(SystemBatchInput systemBatchInput){
        this.systemBatchInput = systemBatchInput;
    }

    @Override
    public void run() {
        statementService.splitAndSave(systemBatchInput);
    }

    public SystemBatchInput getSystemBatchInput() {
        return systemBatchInput;
    }

    public void setSystemBatchInput(SystemBatchInput systemBatchInput) {
        this.systemBatchInput = systemBatchInput;
    }
}
