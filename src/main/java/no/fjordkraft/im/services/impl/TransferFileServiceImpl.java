package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.jobs.schedulerjobs.InvoiceFeedWatcherJob;
import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.model.TransferFile;
import no.fjordkraft.im.repository.SystemBatchInputRepository;
import no.fjordkraft.im.repository.SystemConfigRepository;
import no.fjordkraft.im.repository.TransferFileRepository;
import no.fjordkraft.im.services.TransferFileService;
import no.fjordkraft.im.statusEnum.SystemBatchInputStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import org.apache.tomcat.util.descriptor.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by bhavi on 5/9/2017.
 */
@Service
public class TransferFileServiceImpl implements TransferFileService {

    @Autowired
    private TransferFileRepository transferFileRepository;

    @Autowired
    private SystemBatchInputRepository systemBatchInputRepository;

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    private static final Logger logger = LoggerFactory.getLogger(InvoiceFeedWatcherJob.class);

    @Transactional
    public void saveIMSystemBatchInput() {
        List<TransferFile> transferFiles = transferFileRepository.readPendingTransferFiles("PENDING");
        logger.debug("Files to be read "+ transferFiles.size());
        if (null != transferFiles) {
            for(TransferFile singleTransferFile: transferFiles) {
                saveSingleIMSysteBatchInput(singleTransferFile);
                createOutputFolders(singleTransferFile.getFileName());
                updateTransferFileStatus(singleTransferFile.getId(), "DONE");
            }
        }
    }

    void saveSingleIMSysteBatchInput(TransferFile transferFile) {
        SystemBatchInput imSystemBatchInput = new SystemBatchInput();
        imSystemBatchInput.setTfId(transferFile.getId());
        imSystemBatchInput.setBrand(transferFile.getBrand());
        imSystemBatchInput.setFilename(transferFile.getFileName());
        imSystemBatchInput.setPayload(transferFile.getFileContent());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        imSystemBatchInput.setCreateTime(timestamp);
        imSystemBatchInput.setUpdateTime(timestamp);
        imSystemBatchInput.setStatus(SystemBatchInputStatusEnum.PENDING.getStatus());
        systemBatchInputRepository.save(imSystemBatchInput);
    }

    void updateTransferFileStatus(Long id, String status) {
        TransferFile transferFile = transferFileRepository.findOne(id);
        transferFile.setTransferState(status);
        transferFileRepository.save(transferFile);
    }

    void createOutputFolders(String filename){
        String destinationPath = systemConfigRepository.getConfigValue(IMConstants.DESTINATION_PATH);
        String folderName = filename.substring(0, filename.indexOf('.'));
        File baseFolder = new File(new File(destinationPath) , folderName);
        baseFolder.mkdir();
    }

    public TransferFileRepository getTransferFileRepository() {
        return transferFileRepository;
    }

    public void setTransferFileRepository(TransferFileRepository transferFileRepository) {
        this.transferFileRepository = transferFileRepository;
    }

    public SystemBatchInputRepository getSystemBatchInputRepository() {
        return systemBatchInputRepository;
    }

    public void setSystemBatchInputRepository(SystemBatchInputRepository systemBatchInputRepository) {
        this.systemBatchInputRepository = systemBatchInputRepository;
    }

    public SystemConfigRepository getSystemConfigRepository() {
        return systemConfigRepository;
    }

    public void setSystemConfigRepository(SystemConfigRepository systemConfigRepository) {
        this.systemConfigRepository = systemConfigRepository;
    }
}