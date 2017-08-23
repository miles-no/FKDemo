package no.fjordkraft.im.services.impl;


import no.fjordkraft.im.model.TransferFileArchive;
import no.fjordkraft.im.repository.TransferFileArchiveRepository;
import no.fjordkraft.im.services.UITransferFileArchiveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
public class UITransferFileArchiveFetchServiceImpl implements UITransferFileArchiveService {

    @Resource
    private TransferFileArchiveRepository transferFileArchiveRepository;

    @Transactional
    @Override
    public TransferFileArchive save(TransferFileArchive transferFileArchive){
        return transferFileArchiveRepository.saveAndFlush(transferFileArchive);
    }
}


