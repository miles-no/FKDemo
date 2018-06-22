package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Dummies;
import no.fjordkraft.im.if320.models.Dummy;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavi on 6/12/2018.
 */
@Service
@PreprocessorInfo(order = 90)
public class DummyTagPreprocessor extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(DummyTagPreprocessor.class);


    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {
        int totalDummyTag = 0;
        int initialDummyTags = 10;

        if("YES".equals(request.getStatement().getDirectDebit()) && request.getStatement().isOneMeter() ) {
            totalDummyTag = initialDummyTags + 3 ;
        }
        else if(!request.getStatement().isOneMeter() && "YES".equals(request.getStatement().getDirectDebit())) {
            totalDummyTag = initialDummyTags + 5;
        } else if(!request.getStatement().isOneMeter()) {
            totalDummyTag = initialDummyTags + 5;
        } else {
            totalDummyTag = initialDummyTags + 4;
        }

        logger.debug("in DummyTagPreprocessor ");
        Statement stmt = request.getStatement();
        int transactionsCount = 0;

        if(null != stmt.getTransactionGroup().getTransaction()) {
          transactionsCount = stmt.getTransactionGroup().getTransaction().size();
        }

        if(null != stmt.getTransactionGroup().getTransactionSummary()) {
            transactionsCount = transactionsCount +  stmt.getTransactionGroup().getTransactionSummary().size();
        }
        List<Dummy> dummyList =  new ArrayList<>();
        Dummy d = new Dummy();
        d.setDummyId("");
        logger.debug("in DummyTagPreprocessor transaction count "+ transactionsCount);
        logger.debug("in DummyTagPreprocessor count "+ (totalDummyTag-transactionsCount));
        for(int i = 0 ;i< (totalDummyTag-transactionsCount);i++ ) {
            logger.debug("Adding dummy ");
            dummyList.add(d);
        }
        Dummies dummies = new Dummies();
        dummies.setDummy(dummyList);
        stmt.setDummies(dummies);
    }
}


