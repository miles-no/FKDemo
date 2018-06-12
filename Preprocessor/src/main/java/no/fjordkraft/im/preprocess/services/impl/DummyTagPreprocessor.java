package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Dummy;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavi on 6/12/2018.
 */
@Service
@PreprocessorInfo(order = 90)
public class DummyTagPreprocessor extends BasePreprocessor {

    int totalDummyTag = 13;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {
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
        for(int i = 0 ;i>(totalDummyTag-transactionsCount);i++ ) {
            dummyList.add(d);
        }
        stmt.setDummy(dummyList);
    }
}


