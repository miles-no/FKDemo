package no.fjordkraft.im.services.impl;


import no.fjordkraft.im.model.TransferFileArchive;
import no.fjordkraft.im.model.TransferFileId;
import no.fjordkraft.im.repository.TransferFileArchiveRepository;
import no.fjordkraft.im.services.TransferFileArchiveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("TransferFileArchiveService")
public class TransferFileArchiveFetchServiceImpl implements TransferFileArchiveService {

    @Resource
    private TransferFileArchiveRepository transferFileArchiveRepository;

    @Transactional(readOnly = true)
    @Override
    public TransferFileArchive findOne(TransferFileId tfId) {
        return transferFileArchiveRepository.findOne(tfId);
    }

    @Transactional
    @Override
    public TransferFileArchive save(TransferFileArchive transferFileArchive){
        return transferFileArchiveRepository.saveAndFlush(transferFileArchive);
    }
}


