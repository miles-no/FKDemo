package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.repository.StatementPayloadRepository;
import no.fjordkraft.im.services.StatementPayloadService;
import no.fjordkraft.im.services.StatementService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by bhavi on 8/30/2018.
 */
public class StatementPayloadServiceImpl implements StatementPayloadService{

    @Autowired
    private StatementPayloadRepository statementPayloadRepository;

    @Override
    public int deleteStatementPayloadTillDate(Date tillDate) {
        return statementPayloadRepository.deleteStatementPayloadTillDate(tillDate);
    }

    public StatementPayloadRepository getStatementPayloadRepository() {
        return statementPayloadRepository;
    }

    public void setStatementPayloadRepository(StatementPayloadRepository statementPayloadRepository) {
        this.statementPayloadRepository = statementPayloadRepository;
    }
}
