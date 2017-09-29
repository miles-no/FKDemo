package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.jobs.schedulerjobs.InvoiceFeedWatcherJob;
import no.fjordkraft.im.model.*;
import no.fjordkraft.im.repository.TransferFileRepository;
import no.fjordkraft.im.services.*;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.statusEnum.SystemBatchInputStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by miles on 8/11/2017.
 */
@Service
public class TransferFileServiceImpl implements TransferFileService {

    private static final Logger logger = LoggerFactory.getLogger(TransferFileServiceImpl.class);

    @Autowired
    TransferFileRepository transferFileRepository;

    @Autowired
    ProcessTransferFileService processTransferFileService;

    @Autowired
    ConfigService configService;

    @Autowired
    StatementService statementService;

    @Autowired
    SystemBatchInputService systemBatchInputService;

    @Transactional
    public void fetchAndProcess() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String afiFlag = configService.getString(IMConstants.AFI_TRANSFER_FILE_AUTO_PICK);
        List<TransferFile> transferFiles;

        if(null != afiFlag && !afiFlag.isEmpty() && IMConstants.YES.equals(afiFlag)) {
            Long numOfThreads = configService.getLong(IMConstants.NUM_OF_THREAD_FILESPLITTER);
            if(null == numOfThreads) {
                numOfThreads = 1l;
            }
            transferFiles = transferFileRepository.readTransferFileForAutoMode(numOfThreads, TransferTypeEnum.if320);
        } else {
            transferFiles = transferFileRepository.readPendingTransferFiles("PENDING", TransferTypeEnum.if320);
        }
        logger.debug("Files to be read "+ transferFiles.size());
        if (null != transferFiles) {
            for(TransferFile transferFile: transferFiles) {
                SystemBatchInput systemBatchInput = getSystemBatchInput(transferFile);
                createOutputFolders(transferFile.getFilename());
                processTransferFileService.processTransferFile(systemBatchInput);
                transferFile.setImStatus(SystemBatchInputStatusEnum.PROCESSING.getStatus());
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                transferFile.setImSatusChanged(timestamp);
                transferFileRepository.save(transferFile);
            }
            logger.debug("moved files " + transferFiles.size() + " " + stopWatch.prettyPrint());
        }
    }

    private SystemBatchInput getSystemBatchInput(TransferFile transferFile){
        SystemBatchInput imSystemBatchInput = new SystemBatchInput();
        imSystemBatchInput.setTransferFile(transferFile);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        imSystemBatchInput.setCreateTime(timestamp);
        imSystemBatchInput.setUpdateTime(timestamp);
        imSystemBatchInput.setStatus(SystemBatchInputStatusEnum.PENDING.getStatus());
        return imSystemBatchInput;
    }

    void createOutputFolders(String filename){
        String destinationPath =configService.getString(IMConstants.BASE_DESTINATION_FOLDER_PATH);
        String folderName = filename.substring(0, filename.indexOf('.'));
        File baseFolder = new File(new File(destinationPath) , folderName);
        baseFolder.mkdir();
    }

    @Transactional
    public void consolidateAndUpdateStatus(){
        List<SystemBatchInput> systemBatchInputList =  systemBatchInputService.getAllByStatus(SystemBatchInputStatusEnum.PROCESSED.getStatus());
        for(SystemBatchInput systemBatchInput : systemBatchInputList){
            logger.debug("Fetching status for "+systemBatchInput.getTransferFile().getFilename());
            Map<String, Integer> statusCountMap =  statementService.getStatementBySystemBatchId(systemBatchInput.getId());
            String status = computeTransferFileStatus(statusCountMap , systemBatchInput);
            if(!StringUtils.isEmpty(status)) {
                TransferFile transferFile = systemBatchInput.getTransferFile();
                transferFile.setImStatus(status);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                transferFile.setImSatusChanged(timestamp);
                transferFileRepository.save(transferFile);
                systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, status);
            }
        }
    }

    @Override
    @Transactional
    public void updateTransferFile(TransferFileId tfId, String status) {
        TransferFile transferFile = transferFileRepository.findOne(tfId);
        transferFile.setImStatus(status);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        transferFile.setImSatusChanged(timestamp);
        transferFileRepository.save(transferFile);
    }


    private String computeTransferFileStatus(Map<String, Integer> statusCountMap, SystemBatchInput imSystemBatchInput){
        String statusToUpdate = "";
        int failedRecords = statusCountMap.get(StatementStatusEnum.INVOICE_PROCESSING_FAILED.getStatus()) +
                statusCountMap.get(StatementStatusEnum.PDF_PROCESSING_FAILED.getStatus()) + statusCountMap.get(StatementStatusEnum.PRE_PROCESSING_FAILED.getStatus());
        int totalNoOfRecords = imSystemBatchInput.getNumOfRecords();

        if(totalNoOfRecords == statusCountMap.get(StatementStatusEnum.INVOICE_PROCESSED.getStatus())) {
            statusToUpdate = SystemBatchInputStatusEnum.PROCESSED.getStatus();
        } else if(failedRecords == totalNoOfRecords) {
            statusToUpdate = SystemBatchInputStatusEnum.FAILED.getStatus();
        } else if (failedRecords + statusCountMap.get(StatementStatusEnum.INVOICE_PROCESSED.getStatus()) == totalNoOfRecords){
            statusToUpdate = SystemBatchInputStatusEnum.PARTIAL_PROCESSED.getStatus();
        }

        return statusToUpdate;
    }

}
