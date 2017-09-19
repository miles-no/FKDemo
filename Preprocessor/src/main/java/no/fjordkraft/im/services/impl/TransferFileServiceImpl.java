package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.jobs.schedulerjobs.InvoiceFeedWatcherJob;
import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.model.TransferFile;
import no.fjordkraft.im.model.TransferTypeEnum;
import no.fjordkraft.im.repository.TransferFileRepository;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.services.ProcessTransferFileService;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.services.TransferFileService;
import no.fjordkraft.im.statusEnum.SystemBatchInputStatusEnum;
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
 * Created by miles on 8/11/2017.
 */
@Service
public class TransferFileServiceImpl implements TransferFileService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceFeedWatcherJob.class);

    @Autowired
    TransferFileRepository transferFileRepository;

    @Autowired
    ProcessTransferFileService processTransferFileService;

    @Autowired
    ConfigService configService;

    @Transactional
    public void fetchAndProcess() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String afiFlag = configService.getString(IMConstants.AFI_TRANSFER_FILE_AUTO_PICK);
        List<TransferFile> transferFiles = new ArrayList<>();

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
                transferFile.setImStatus("DONE");
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
}
