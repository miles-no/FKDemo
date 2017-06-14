package no.fjordkraft.im.task;

import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.services.StatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

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
