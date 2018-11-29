package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.AuditLogService;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 11/27/18
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
@PreprocessorInfo(order=30)
public class AddUsageTransactionPreprocessor extends BasePreprocessor {

    @Autowired
    AuditLogService auditLogService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request)
    {
        try {
             String brand = request.getEntity().getSystemBatchInput().getBrand();
            if("VKAS".equalsIgnoreCase(brand)) {
            List<Transaction> transactions = request.getStatement().getTransactions().getTransaction();
            List<Attachment> attachmentList = request.getStatement().getAttachments().getAttachment();
            if(transactions!=null && transactions.size()>0) {
                for(Transaction transaction: transactions) {
                    if(transaction.getTransactionType()!=null && transaction.getTransactionType().equalsIgnoreCase(IMConstants.TRANSACTION_TYPE_USAGE_TRANSACTION))
                    {
                        Distributions distributions = transaction.getDistributions();
                        if(distributions == null) {
                            distributions = new Distributions();
                        }
                       List<Distribution> distributionList =  distributions.getDistribution();
                        if(distributionList ==null) {
                            distributionList = new ArrayList<>();
                            Distribution distribution = new Distribution();
                            distribution.setAmount(transaction.getAmountWithVat());
                            distributionList.add(distribution);
                        }
                        if(distributionList!=null && distributionList.size()>0 ) {
                            for(Distribution distribution : distributionList) {
                                distribution.setName(IMConstants.KRAFT);
                            }
                        }
                        else {
                            Distribution distribution = new Distribution();
                            distribution.setAmount(transaction.getAmountWithVat());
                            distribution.setName(IMConstants.KRAFT);
                            distributionList.add(distribution);
                        }
                        distributions.setDistribution(distributionList);
                        transaction.setDistributions(distributions);
                        if(transaction.getFreeText()==null ||(transaction.getFreeText()!=null &&  transaction.getFreeText().isEmpty())) {
                            Attachment attachment = getAttachmentForTransaction(transaction.getReference(),attachmentList);
                            if(attachment!=null) {
                                transaction.setFreeText(attachment.getLeveringsAdresse());
                            }
                        }
                    }
                }
            }

            }
        }catch(Exception e) {
            throw new PreprocessorException("Failed in Add Usage Transaction Pre-Processor with message: ",e );

    }

    }

    private Attachment getAttachmentForTransaction(String reference, List<Attachment> attachmentList) {
        if(attachmentList!=null && !attachmentList.isEmpty() && attachmentList.size()>0) {
            for(Attachment attachment:attachmentList) {
                if(attachment!=null && reference.equalsIgnoreCase(attachment.getFAKTURA().getFAKTURANR())) {
                    return attachment;
                }
            }
        }
        return null;
    }
}
