package no.fjordkraft.im.ui.services.impl;

import no.fjordkraft.im.jobs.schedulerjobs.InvoiceFeedWatcherJob;
import no.fjordkraft.im.model.StatusCount;
import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.model.TransferFile;
import no.fjordkraft.im.model.TransferTypeEnum;
import no.fjordkraft.im.repository.TransferFileRepository;
import no.fjordkraft.im.services.*;
import no.fjordkraft.im.statusEnum.SystemBatchInputStatusEnum;
import no.fjordkraft.im.ui.services.UITransferFileArchiveService;
import no.fjordkraft.im.ui.services.UITransferFileService;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavi on 5/9/2017.
 */
@Service
public class UITransferFileServiceImpl implements UITransferFileService {

    @Autowired
    private TransferFileRepository transferFileRepository;

    @Autowired
    private ConfigService configService;

    @Autowired
    private SystemBatchInputService systemBatchInputService;

    @Autowired
    UITransferFileArchiveService transferFileArchiveService;

    @Autowired
    StatementService statementService;


    public TransferFile getOneTransferFileWithEmptyIMStatus(){
        return transferFileRepository.readSingleTransferFile();
    }

    @Override
    @Transactional
    public TransferFile saveTransferFile(TransferFile transferFile) {
        return transferFileRepository.save(transferFile);
    }

    @Override
    public List<TransferFile> readTransferFileByBatchJobId(Long batchJobId) {
        return transferFileRepository.readTransferfileByBatchJobId(batchJobId);
    }

    public TransferFileRepository getTransferFileRepository() {
        return transferFileRepository;
    }

    public void setTransferFileRepository(TransferFileRepository transferFileRepository) {
        this.transferFileRepository = transferFileRepository;
    }

    public SystemBatchInputService getSystemBatchInputService() {
        return systemBatchInputService;
    }

    public void setSystemBatchInputService(SystemBatchInputService systemBatchInputService) {
        this.systemBatchInputService = systemBatchInputService;
    }

    public ConfigService getConfigService() {
        return configService;
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }


}