package no.fjordkraft.im.ui.services;
import no.fjordkraft.im.model.TransferFile;

import java.util.List;

/**
 * Created by bhavi on 5/9/2017.
 */
public interface UITransferFileService {

    public TransferFile getOneTransferFileWithEmptyIMStatus();

    public TransferFile saveTransferFile(TransferFile transferFile);

    List<TransferFile> readTransferFileByBatchJobId(Long batchJobId);

}
