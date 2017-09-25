package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.services.RetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by miles on 9/21/2017.
 */
@Service
public class RetryServiceImpl implements RetryService {

    @Autowired
    StatementServiceImpl statementService;

    @Override
    public void retryStatementFromPending(Long statementId) {
        Statement statement = statementService.getStatement(statementId);
    }
}
