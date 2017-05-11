package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.repository.SystemBatchInputRepository;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.services.StatementSplitter;
import no.fjordkraft.im.statusEnum.SystemBatchInputStatusEnum;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

/**
 * Created by bhavi on 5/9/2017.
 */
@Service
public class StatementServiceImpl implements StatementService {

    private static final Logger logger = LoggerFactory.getLogger(StatementServiceImpl.class);

    @Autowired
    SystemBatchInputRepository systemBatchInputRepository;

    @Autowired
    StatementSplitter statementSplitter;

    public void splitSystemBatchInputFile() {
        StopWatch stopWatch = new StopWatch(getClass().getSimpleName());
        Long transferFileID = -1l;
        stopWatch.start("Statement Splitter");
        SystemBatchInput systemBatchInput = null;
        try {
            InputStream inputStream = null;
            //FileInputStream fileInputStream = null;
            //File targetFile = new File("src/main/resources/targetFile.tmp");

            systemBatchInput = systemBatchInputRepository.readSingleSystemBatchInputFile(String.valueOf(SystemBatchInputStatusEnum.PENDING.getStatus()));
            if(null != systemBatchInput) {
                inputStream = new ByteArrayInputStream(systemBatchInput.getPayload().getBytes(StandardCharsets.ISO_8859_1));
                //FileUtils.copyInputStreamToFile(inputStream, targetFile);
                //fileInputStream = new FileInputStream(targetFile);
                transferFileID = systemBatchInput.getId();
                updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.PROCESSING.getStatus());
                statementSplitter.batchFileSplit(inputStream, systemBatchInput.getFilename(), systemBatchInput);
                updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.PROCESSED.getStatus());
                logger.debug("File split successful for file " + systemBatchInput.getFilename() + " with id " + systemBatchInput.getId());
            }

        } catch(Exception ex) {
            //if(transferFileID > 0) {
                updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.FAILED.getStatus());
            //}
            throw new RuntimeException(ex.getMessage());
        }
        stopWatch.stop();
        logger.debug(stopWatch.prettyPrint());
    }

    void updateStatusOfIMSystemBatchInput(SystemBatchInput systemBatchInput , String status) {
        //SystemBatchInput imSystemBatchInput = systemBatchInputRepository.findOne(id);
        systemBatchInput.setStatus(status);
        systemBatchInput.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        systemBatchInputRepository.save(systemBatchInput);
    }
}
