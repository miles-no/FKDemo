package no.fjordkraft.im.ui.services.impl;

import no.fjordkraft.im.model.TransferFile;
import no.fjordkraft.im.model.TransferTypeEnum;
import no.fjordkraft.im.repository.TransferFileRepository;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.services.SystemBatchInputService;
import no.fjordkraft.im.ui.services.UITransferFileArchiveService;
import no.fjordkraft.im.ui.services.UITransferFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return transferFileRepository.readSingleTransferFile(TransferTypeEnum.if320);
    }

    @Override
    @Transactional
    public TransferFile saveTransferFile(TransferFile transferFile) {
        return transferFileRepository.save(transferFile);
    }

    @Override
    public List<TransferFile> readTransferFileByBatchJobId(Long batchJobId, TransferTypeEnum transferType) {
        return transferFileRepository.readTransferfileByBatchJobId(batchJobId, TransferTypeEnum.if320);
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