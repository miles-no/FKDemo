package no.fjordkraft.im.services;

import no.fjordkraft.im.model.SystemBatchInput;

/**
 * Created by miles on 8/21/2017.
 */
public interface ProcessTransferFileService {

    public void processTransferFile(SystemBatchInput systemBatchInput);

    public void splitAndSave(SystemBatchInput systemBatchInput);
}
