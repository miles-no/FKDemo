package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.services.TransactionGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bhavi on 12/12/2017.
 */
@Service
@PreprocessorInfo(order=14)
public class LineItemPreprocessor extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(LineItemPreprocessor.class);

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {
        no.fjordkraft.im.if320.models.TransactionGroup transactionGroup = request.getStatement().getTransactionGroup();
        LineItems lineItems = request.getStatement().getLineItems();
        boolean hasLineItems = false;
        if(null != lineItems ) {
            List<LineItem> lineItemList = lineItems.getLineItem();
            if(null != lineItemList) {
                if(null == transactionGroup) {
                    transactionGroup = new TransactionGroup();
                }

                for(LineItem lineItem : lineItemList) {
                    hasLineItems = true;
                    Transaction transaction = new Transaction();
                    transaction.setAmount(lineItem.getAmount()*(-1));
                    transaction.setAmountWithVat(lineItem.getAmountWithVat()*(-1));
                    transaction.setVatAmount(lineItem.getVatAmount());
                    if(lineItem.getLineItemCategory().indexOf(";") != -1) {
                        transaction.setTransactionCategory(lineItem.getLineItemCategory().substring(lineItem.getLineItemCategory().indexOf(";")+1));
                    } else {
                        transaction.setTransactionCategory(lineItem.getLineItemCategory());
                    }
                    transactionGroup.getTransaction().add(transaction);
                    transactionGroup.setTotalTransactions(transactionGroup.getTotalTransactions() + 1);
                }
                request.getStatement().setHasLineItems(hasLineItems);
                request.getStatement().setTransactionGroup(transactionGroup);
            }
        }
    }
}
