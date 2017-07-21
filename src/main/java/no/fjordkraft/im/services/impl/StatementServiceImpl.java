package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.domain.RestInvoicePdf;
import no.fjordkraft.im.domain.RestStatement;
import no.fjordkraft.im.model.*;
import no.fjordkraft.im.repository.StatementDetailRepository;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.repository.SystemBatchInputRepository;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.services.StatementSplitter;
import no.fjordkraft.im.services.SystemBatchInputService;
import no.fjordkraft.im.services.TransferFileArchiveService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.statusEnum.SystemBatchInputStatusEnum;
import no.fjordkraft.im.statusEnum.UIStatementStatusEnum;
import no.fjordkraft.im.task.SplitterTask;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bhavi on 5/9/2017.
 */
@Service
public class StatementServiceImpl implements StatementService,ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(StatementServiceImpl.class);

    @Autowired
    SystemBatchInputRepository systemBatchInputRepository;

    @Autowired
    StatementSplitter statementSplitter;

    @Autowired
    @Qualifier("FileSplitterExecutor")
    TaskExecutor taskExecutor;

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    SystemBatchInputService systemBatchInputService;

    @Resource
    StatementDetailRepository statementDetailRepository;

    ApplicationContext applicationContext;

    @Autowired
    TransferFileArchiveService transferFileArchiveService;

    @Autowired
    TransferFileServiceImpl transferFileService;

    @Value("${if320.skip.bytes}")
    Boolean skipBytes;


    @Transactional
    public void processTransferFile(SystemBatchInput systemBatchInput){
            logger.debug("Fetch and split file "+systemBatchInput.getTransferFile().getFilename());
            //systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.PROCESSING.getStatus());
            SplitterTask splitterTask = applicationContext.getBean(SplitterTask.class,systemBatchInput);
            taskExecutor.execute(splitterTask);
    }

    @Override
    public List<StatusCount> getStatusAndCountByTransferfile(String transferFile) {
        Long sbiId = systemBatchInputRepository.getSBIIdByFilename(transferFile);
        Integer numOfRecords = systemBatchInputService.getNumOfRecordsById(sbiId);
        return mapStatusToUIStatus(statementRepository.getStatementBySbiId(sbiId), (null != numOfRecords ? Long.valueOf(numOfRecords):null));
    }

    @Override
    public List<StatusCount> getStatusByTransferfileBatchId(Long ekBatchJobId) {
        List<TransferFile> transferFileList = transferFileService.readTransferFileByBatchJobId(ekBatchJobId);
        List<StatusCount> statusCountList = new ArrayList<>();
        Integer numOfRecords = 0;
        Long totalRecords = 0l;

        if(null != transferFileList && IMConstants.ZERO != transferFileList.size()) {
            for (TransferFile transferFile : transferFileList) {
                Long sbiId = systemBatchInputRepository.getSBIIdByFilename(transferFile.getFilename());
                numOfRecords = systemBatchInputService.getNumOfRecordsById(sbiId);
                if(null != numOfRecords) {
                    totalRecords += numOfRecords;
                }
                List<StatusCount> statusCounts = statementRepository.getStatementBySbiId(sbiId);
                statusCountList.addAll(statusCounts);
            }
            return mapStatusToUIStatus(statusCountList, totalRecords);
        }
        return null;
    }

    private List<StatusCount> mapStatusToUIStatus(List<StatusCount> statusCounts, Long numOfRecords) {
        Map<String, Long> statusMap = new HashMap<String, Long>();
        List<StatusCount> uiStatusCount = new ArrayList<StatusCount>();
        StatusCount status = null;
        Long sum = 0l;

        statusMap.put(UIStatementStatusEnum.PENDING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.PRE_PROCESSING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.PROCESSING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.MERGING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.READY.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.FAILED.getStatus(), 0l);

        for(StatusCount statusCount:statusCounts) {
            if(statusCount.getName().equals(StatementStatusEnum.PENDING.getStatus())) {
                statusMap.put(UIStatementStatusEnum.PENDING.getStatus(), statusCount.getValue());
            } else if(statusCount.getName().equals(StatementStatusEnum.PRE_PROCESSING.getStatus())){
                statusMap.put(UIStatementStatusEnum.PRE_PROCESSING.getStatus(), statusCount.getValue());
            } else if(statusCount.getName().equals(StatementStatusEnum.PRE_PROCESSED.getStatus())
                    || statusCount.getName().equals(StatementStatusEnum.PDF_PROCESSING.getStatus())){
                sum = statusMap.get(UIStatementStatusEnum.PROCESSING.getStatus());
                sum += statusCount.getValue();
                statusMap.put(UIStatementStatusEnum.PROCESSING.getStatus(), sum);
            } else if(statusCount.getName().equals(StatementStatusEnum.PDF_PROCESSED.getStatus())
                    || statusCount.getName().equals(StatementStatusEnum.INVOICE_PROCESSING.getStatus())) {
                sum = statusMap.get(UIStatementStatusEnum.MERGING.getStatus());
                sum += statusCount.getValue();
                statusMap.put(UIStatementStatusEnum.MERGING.getStatus(), sum);
            } else if(statusCount.getName().equals(StatementStatusEnum.INVOICE_PROCESSED.getStatus())
                    || statusCount.getName().equals(StatementStatusEnum.DELIVERY_PENDING.getStatus())) {
                sum = statusMap.get(UIStatementStatusEnum.READY.getStatus());
                sum += statusCount.getValue();
                statusMap.put(UIStatementStatusEnum.READY.getStatus(), sum);
            } else if(statusCount.getName().equals(StatementStatusEnum.PRE_PROCESSING_FAILED.getStatus()) ||
                    statusCount.getName().equals(StatementStatusEnum.PDF_PROCESSING_FAILED.getStatus()) ||
                    statusCount.getName().equals(StatementStatusEnum.INVOICE_PROCESSING_FAILED.getStatus()) ||
                    statusCount.getName().equals(StatementStatusEnum.DELIVERY_FAILED.getStatus())) {

                sum = statusMap.get(UIStatementStatusEnum.FAILED.getStatus());
                sum += statusCount.getValue();
                statusMap.put(UIStatementStatusEnum.FAILED.getStatus(), sum);
            }
        }

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.PENDING.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.PENDING.getStatus()));
        uiStatusCount.add(0, status);

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.PRE_PROCESSING.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.PRE_PROCESSING.getStatus()));
        uiStatusCount.add(1, status);

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.PROCESSING.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.PROCESSING.getStatus()));
        uiStatusCount.add(2, status);

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.MERGING.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.MERGING.getStatus()));
        uiStatusCount.add(3, status);

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.READY.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.READY.getStatus()));
        uiStatusCount.add(4, status);

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.FAILED.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.FAILED.getStatus()));
        uiStatusCount.add(5, status);

        status = new StatusCount();
        status.setName("Total Invoice Processed");
        status.setValue(Long.valueOf(statusCounts.size()));
        uiStatusCount.add(6, status);

        if(null != numOfRecords) {
            status = new StatusCount();
            status.setName("Total statements");
            status.setValue(Long.valueOf(statusCounts.size()));
            uiStatusCount.add(6, status);
        }
        return uiStatusCount;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void splitAndSave(SystemBatchInput systemBatchInput){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("SplitStatementFile with systemBatchInputId " + systemBatchInput.getId() + " Filename " + systemBatchInput.getTransferFile().getFilename() );
        try {
            systemBatchInput = systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.PROCESSING.getStatus());
            TransferFileId transferFileId = systemBatchInput.getTransferFile().getCompositeKey();
            TransferFileArchive transferFileArchive = transferFileArchiveService.findOne(transferFileId);
            String payload = transferFileArchive.getFileContent();
            InputStream inputStream = new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8));
            if(skipBytes) {
                byte[] b = new byte[2];
                int i = inputStream.read(b);
                logger.debug("bytes read is "+ new String(b) + " "+ i);
            }
            statementSplitter.batchFileSplit(inputStream, transferFileArchive.getFilename(), systemBatchInput);

            systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.PROCESSED.getStatus());
            logger.debug("File split successful for file " + transferFileArchive.getFilename() + " with id " + systemBatchInput.getId()+ stopWatch.prettyPrint());
        } catch (Exception e) {
            logger.error("Exception while splitting file " +systemBatchInput.getTransferFile().getFilename()+ " id " + systemBatchInput.getId(), e);
            systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.FAILED.getStatus());
        }
        stopWatch.stop();
        logger.debug("Time for splitting file with id "+ systemBatchInput.getId() + " Filename "+ systemBatchInput.getTransferFile().getFilename() + stopWatch.prettyPrint());
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateStatement(Statement statement){
        statementRepository.saveAndFlush(statement);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateStatement(Statement statement, StatementStatusEnum status){
        statement.setStatus(status.getStatus());
        statement.setUdateTime(new Timestamp(System.currentTimeMillis()));
        statementRepository.saveAndFlush(statement);
    }


    public List<Statement> getStatementsByStatus(StatementStatusEnum statementStatusEnum){
        return statementRepository.readStatements(statementStatusEnum.name());
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Transactional()
    public void saveIMStatementinDB(File statementFile, Statement imStatement) throws IOException {
        String xml = FileUtils.readFileToString(statementFile, StandardCharsets.ISO_8859_1);

        //imStatement.setPayload(xml);
        imStatement.setStatus(StatementStatusEnum.PENDING.getStatus());
        imStatement.setCreateTime(new Timestamp(System.currentTimeMillis()));
        imStatement.setUdateTime(new Timestamp(System.currentTimeMillis()));

        StatementPayload statementPayload = new StatementPayload();
        statementPayload.setPayload(xml);
        statementPayload.setStatement(imStatement);
        statementRepository.saveAndFlush(imStatement);
    }

    @Transactional()
    public void saveIMStatementinDB(String xml, Statement imStatement) throws IOException {
       // String xml = FileUtils.readFileToString(statementFile, StandardCharsets.ISO_8859_1);

        //imStatement.setPayload(xml);
        imStatement.setStatus(StatementStatusEnum.PENDING.getStatus());
        imStatement.setCreateTime(new Timestamp(System.currentTimeMillis()));
        imStatement.setUdateTime(new Timestamp(System.currentTimeMillis()));

        StatementPayload statementPayload = new StatementPayload();
        statementPayload.setPayload(xml);
        statementPayload.setStatement(imStatement);

        imStatement.setStatementPayload(statementPayload);
        statementRepository.saveAndFlush(imStatement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestStatement> getDetails(int page, int size, String status, Timestamp fromTime, Timestamp toTime,
                                          String brand, String customerID, String invoiceNumber) {
        String mappedStatus = mapStatus(status);
        String mappedBrand = mapBrand(brand);

        List<Statement> statementList = statementDetailRepository.getDetails(page, size, mappedStatus, fromTime, toTime,
                mappedBrand, customerID, invoiceNumber);

        List<RestStatement> restStatementList = new ArrayList<>();
        //List<Long> statementIdList =  new ArrayList<>();
        Map<Long,RestStatement> statementMap = new HashMap<Long,RestStatement>();
        if(null != statementList) {
            for (Statement statement : statementList) {
                RestStatement restStatement = new RestStatement();
                restStatement.setId(statement.getId());
                restStatement.setStatus(statement.getStatus());
                restStatement.setUdateTime(statement.getUdateTime());
                restStatement.setPdfAttachment(statement.getPdfAttachment());
                restStatement.setAccountNumber(statement.getAccountNumber());
                restStatement.setAmount(statement.getAmount());
                restStatement.setInvoiceNumber(statement.getInvoiceNumber());
                restStatement.setCustomerId(statement.getCustomerId());
                restStatement.setDistributionMethod(statement.getDistributionMethod());
                restStatement.setVersion(statement.getVersion());
                restStatement.setCity(statement.getCity());
                restStatement.setStatementId(statement.getStatementId());
                restStatement.setDueDate(statement.getDueDate());
                restStatement.setCreateTime(statement.getCreateTime());
                restStatementList.add(restStatement);
                //statementIdList.add(statement.getId());
                statementMap.put(statement.getId(),restStatement);
            }
        }

        if(!statementMap.isEmpty()) {
            List<RestInvoicePdf> invoicePdfList = statementDetailRepository.getInvoicePdfs(statementMap.keySet());

            for (RestInvoicePdf restInvoicePdf : invoicePdfList) {
                RestStatement restStatement = statementMap.get(restInvoicePdf.getStatementId());
                restStatement.getInvoicePdfList().add(restInvoicePdf);
            }
        }
        return restStatementList;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountByStatus(String status, Timestamp fromTime,
                                 Timestamp toTime, String brand, String customerID, String invoiceNumber) {
        String mappedStatus = mapStatus(status);
        String mappedBrand = mapBrand(brand);
        return statementDetailRepository.getCountByStatus(mappedStatus, fromTime, toTime,
                mappedBrand, customerID, invoiceNumber);
    }

    private String mapBrand(String brands) {
        if(null == brands) {
            return null;
        }
        String[] brandList = brands.split(",");
        StringBuffer mappedBrand = new StringBuffer();

        for(String brand:brandList) {
            if(!mappedBrand.toString().equals(IMConstants.EMPTY_STRING)) {
                mappedBrand.append(",");
            }
            mappedBrand.append("'" + brand + "'");
        }
        return mappedBrand.toString();
    }

    private String mapStatus(String states) {
        if(null == states) {
            return null;
        }
        String[] statusList = states.split(",");
        StringBuffer mappedStatusList = new StringBuffer();

        for(String status:statusList) {
            if(!mappedStatusList.toString().equals(IMConstants.EMPTY_STRING)) {
                mappedStatusList.append(",");
            }
            if(UIStatementStatusEnum.PENDING.getStatus().equals(status)) {
                mappedStatusList.append("'" + StatementStatusEnum.PENDING.getStatus() + "'");
            } else if(UIStatementStatusEnum.PRE_PROCESSING.getStatus().equals(status)) {
                mappedStatusList.append("'" + StatementStatusEnum.PRE_PROCESSING.getStatus() + "'");
            } else if(UIStatementStatusEnum.PROCESSING.getStatus().equals(status)) {
                mappedStatusList.append("'" + StatementStatusEnum.PRE_PROCESSED.getStatus() + "'");
                mappedStatusList.append(",");
                mappedStatusList.append("'" + StatementStatusEnum.PDF_PROCESSING.getStatus()+ "'");
            } else if(UIStatementStatusEnum.MERGING.getStatus().equals(status)) {
                mappedStatusList.append("'" + StatementStatusEnum.PDF_PROCESSED.getStatus() + "'");
                mappedStatusList.append(",");
                mappedStatusList.append("'" + StatementStatusEnum.INVOICE_PROCESSING.getStatus() + "'");
            } else if(UIStatementStatusEnum.READY.getStatus().equals(status)) {
                mappedStatusList.append("'" + StatementStatusEnum.INVOICE_PROCESSED.getStatus() + "'");
                mappedStatusList.append(",");
                mappedStatusList.append("'" + StatementStatusEnum.DELIVERY_PENDING.getStatus() + "'");
            } else if(UIStatementStatusEnum.FAILED.getStatus().equals(status)) {
                mappedStatusList.append("'" + StatementStatusEnum.PRE_PROCESSING_FAILED.getStatus() + "'");
                mappedStatusList.append(",");
                mappedStatusList.append("'" + StatementStatusEnum.PDF_PROCESSING_FAILED.getStatus() + "'");
                mappedStatusList.append(",");
                mappedStatusList.append("'" + StatementStatusEnum.INVOICE_PROCESSING_FAILED.getStatus() + "'");
                mappedStatusList.append(",");
                mappedStatusList.append("'" + StatementStatusEnum.DELIVERY_FAILED.getStatus() + "'");
            }
        }

        return mappedStatusList.toString();
    }


}
