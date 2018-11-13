package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.exceptions.StatementSplitterException;
import no.fjordkraft.im.model.*;
import no.fjordkraft.im.repository.StatementDetailRepository;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.repository.SystemBatchInputRepository;
import no.fjordkraft.im.services.*;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.statusEnum.SystemBatchInputStatusEnum;
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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
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
    StatementRepository statementRepository;

    @Autowired
    SystemBatchInputService systemBatchInputService;

    @Resource
    StatementDetailRepository statementDetailRepository;

    ApplicationContext applicationContext;

    /*@Autowired
    TransferFileServiceImpl transferFileService;*/

    //@Value("${if320.skip.bytes}")
    Boolean skipBytes;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Statement updateStatement(Statement statement){
        return statementRepository.saveAndFlush(statement);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Statement updateStatement(Statement statement, StatementStatusEnum status){
        statement.setStatus(status.getStatus());
        statement.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        Statement statement1 =  statementRepository.saveAndFlush(statement);
        if(status == StatementStatusEnum.PRE_PROCESSING) {
            statement1.getSystemBatchInput().getTransferFile().getFilename();
            statement1.getStatementPayload();
        }
        return statement1;
    }


    public List<Statement> getStatementsByStatus(StatementStatusEnum statementStatusEnum){
        return statementRepository.readStatements(statementStatusEnum.name());
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Transactional()
    public Statement saveIMStatementinDB(File statementFile, Statement imStatement) throws IOException {
        String xml = FileUtils.readFileToString(statementFile, String.valueOf(StandardCharsets.ISO_8859_1));

        //imStatement.setPayload(xml);
        imStatement.setStatus(StatementStatusEnum.PENDING.getStatus());
        imStatement.setCreateTime(new Timestamp(System.currentTimeMillis()));
        imStatement.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        StatementPayload statementPayload = new StatementPayload();
        statementPayload.setPayload(xml);
        statementPayload.setStatement(imStatement);
        return statementRepository.saveAndFlush(imStatement);
    }

    @Transactional()
    public Statement saveIMStatementinDB(String xml, Statement imStatement) throws IOException {
        imStatement.setStatus(StatementStatusEnum.NEW.getStatus());
        imStatement.setCreateTime(new Timestamp(System.currentTimeMillis()));
        imStatement.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        StatementPayload statementPayload = new StatementPayload();
        statementPayload.setPayload(xml);
        statementPayload.setStatement(imStatement);

        imStatement.setStatementPayload(statementPayload);
        return statementRepository.saveAndFlush(imStatement);
    }

    @Transactional(readOnly = true)
    public Statement getStatement(Long statementId){
        Statement stmt = statementRepository.findOne(statementId);
        stmt.getSystemBatchInput().getTransferFile().getFilename();
        return stmt;
    }

    public Map<String, Integer> getStatementBySystemBatchId(Long systemBatchInputId) {
        List<StatusCount> statusCountList =  statementRepository.getStatementBySbiId(systemBatchInputId);
        Map<String, Integer> statusCountMap = new HashMap<>(statusCountList.size());
        for (StatusCount statusCount : statusCountList ) {
            statusCountMap.put(statusCount.getName(),statusCount.getValue().intValue());
        }

        for(StatementStatusEnum statusEnum : StatementStatusEnum.values()) {
            if(null == statusCountMap.get(statusEnum.getStatus())) {
                statusCountMap.put(statusEnum.getStatus(),0);
            }
        }
        return statusCountMap;
    }

    @Override
    public void deleteStatementBySiId(Long siId) {
       statementRepository.deleteStatementsBySiId(siId,StatementStatusEnum.NEW.getStatus());
    }

    @Override
    public int updateStatementsBySiId(Long siId,StatementStatusEnum status) {
       return statementRepository.updateStatementsBySiId(siId,status.getStatus());
    }

}
