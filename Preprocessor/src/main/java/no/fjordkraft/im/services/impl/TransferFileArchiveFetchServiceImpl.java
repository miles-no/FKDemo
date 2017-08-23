package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.TransferFileArchive;
import no.fjordkraft.im.model.TransferFileId;
import no.fjordkraft.im.repository.TransferFileArchiveRepository;
import no.fjordkraft.im.services.TransferFileArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by miles on 8/11/2017.
 */
@Service("TransferFileArchiveService")
public class TransferFileArchiveFetchServiceImpl implements TransferFileArchiveService {

    @Autowired
    TransferFileArchiveRepository transferFileArchiveRepository;

    @Transactional(readOnly = true)
    @Override
    public TransferFileArchive findOne(TransferFileId tfId) {
        return transferFileArchiveRepository.findOne(tfId);
    }
}
