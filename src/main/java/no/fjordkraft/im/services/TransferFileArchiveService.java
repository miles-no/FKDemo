package no.fjordkraft.im.services;

import no.fjordkraft.im.model.TransferFileArchive;
import no.fjordkraft.im.model.TransferFileId;

/**
 * Created by miles on 8/11/2017.
 */
public interface TransferFileArchiveService {

    TransferFileArchive findOne(TransferFileId tfId);
}
