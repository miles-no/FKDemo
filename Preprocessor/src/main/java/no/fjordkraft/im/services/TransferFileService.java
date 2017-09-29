package no.fjordkraft.im.services;

import no.fjordkraft.im.model.TransferFile;
import no.fjordkraft.im.model.TransferFileId;

/**
 * Created by miles on 8/11/2017.
 */
public interface TransferFileService {

    void fetchAndProcess();

    void consolidateAndUpdateStatus();

    void updateTransferFile(TransferFileId tfId, String status);
}
