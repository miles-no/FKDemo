package no.fjordkraft.im.task;

import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.preprocess.services.PreprocessorService;
import no.fjordkraft.im.services.ProcessTransferFileService;
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
    ProcessTransferFileService processTransferFileService;

    public SplitterTask(SystemBatchInput systemBatchInput){
        this.systemBatchInput = systemBatchInput;
    }

    @Override
    public void run() {
        processTransferFileService.splitAndSave(systemBatchInput);
    }

    public SystemBatchInput getSystemBatchInput() {
        return systemBatchInput;
    }

    public void setSystemBatchInput(SystemBatchInput systemBatchInput) {
        this.systemBatchInput = systemBatchInput;
    }
}
