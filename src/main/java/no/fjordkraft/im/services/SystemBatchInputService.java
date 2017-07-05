package no.fjordkraft.im.services;

import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.model.TransferFile;

/**
 * Created by bhavi on 5/22/2017.
 */
public interface SystemBatchInputService {

    public SystemBatchInput updateStatusOfIMSystemBatchInput(SystemBatchInput systemBatchInput, String status);

    //void saveSingleIMSysteBatchInput(TransferFile transferFile);

    public SystemBatchInput saveIMSysteBatchInput(TransferFile transferFile);
}
