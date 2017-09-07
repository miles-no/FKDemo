package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.model.StatementPayload;
import no.fjordkraft.im.repository.StatementDetailRepository;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.repository.SystemBatchInputRepository;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.services.StatementServiceTemp;
import no.fjordkraft.im.services.SystemBatchInputService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by bhavi on 5/9/2017.
 */
@Service
public class StatementServiceImpl implements StatementServiceTemp,ApplicationContextAware {

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

    @Value("${if320.skip.bytes}")
    Boolean skipBytes;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Statement updateStatement(Statement statement){
        logger.debug(" transaction name ::"+ TransactionSynchronizationManager.getCurrentTransactionName());
        logger.debug(" isActualTransactionActive:: " + TransactionSynchronizationManager.isActualTransactionActive());
        statement =  statementRepository.saveAndFlush(statement);
        logger.debug(statement.getId() + " :: "+ statement.getStatus());
        return statement;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Statement updateStatement(Statement statement, StatementStatusEnum status){
        logger.debug(" transaction name ::"+ TransactionSynchronizationManager.getCurrentTransactionName());
        logger.debug(" isActualTransactionActive:: "+TransactionSynchronizationManager.isActualTransactionActive());
        statement.setStatus(status.getStatus());
        statement.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        statement =  statementRepository.saveAndFlush(statement);
        logger.debug(statement.getId() + " :: "+ statement.getStatus());
        return statement;
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
        String xml = FileUtils.readFileToString(statementFile, String.valueOf(StandardCharsets.ISO_8859_1));

        //imStatement.setPayload(xml);
        imStatement.setStatus(StatementStatusEnum.PENDING.getStatus());
        imStatement.setCreateTime(new Timestamp(System.currentTimeMillis()));
        imStatement.setUpdateTime(new Timestamp(System.currentTimeMillis()));

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
        imStatement.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        StatementPayload statementPayload = new StatementPayload();
        statementPayload.setPayload(xml);
        statementPayload.setStatement(imStatement);

        imStatement.setStatementPayload(statementPayload);
        statementRepository.saveAndFlush(imStatement);
    }

    @Transactional(readOnly = true)
    public Statement getStatement(Long statementId){
        Statement stmt = statementRepository.findOne(statementId);
        stmt.getSystemBatchInput().getTransferFile().getFilename();
        return stmt;
    }

}
