package no.fjordkraft.im.services;

import no.fjordkraft.im.model.StatusCount;
import no.fjordkraft.im.model.TransferFile;

import java.util.List;

/**
 * Created by bhavi on 5/9/2017.
 */
public interface TransferFileService {

    public void fetchAndProcess();

    public TransferFile getOneTransferFileWithEmptyIMStatus();

    public TransferFile saveTransferFile(TransferFile transferFile);

    List<TransferFile> readTransferFileByBatchJobId(Long batchJobId);

}
