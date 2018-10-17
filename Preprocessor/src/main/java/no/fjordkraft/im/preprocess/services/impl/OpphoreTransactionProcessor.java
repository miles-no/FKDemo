package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Attachment;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.if320.models.Transaction;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 4/17/18
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
@PreprocessorInfo(order = 17)
public class OpphoreTransactionProcessor  extends BasePreprocessor {
    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
          List<Transaction> listOfTransactions =  request.getStatement().getTransactionGroup().getTransaction();
          List<Attachment> listOfAttachments = request.getStatement().getAttachments().getAttachment();
          for(Attachment attachment: listOfAttachments) {
             if(attachment.isOnlyGrid() || (attachment.getDisplayStromData()!=null && attachment.getDisplayStromData())) {
             if(IMConstants.EMUXML.equals(attachment.getFAKTURA().getVEDLEGGFORMAT())) {
                String fakturanr = attachment.getFAKTURA().getFAKTURANR();
                 for(Transaction transaction:listOfTransactions){
                     if(transaction.getReference()!=null && transaction.getReference().equals(fakturanr)) {
                         String invoiceTyp = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getInvoiceOrderInfo110().getInvoiceType();
                         if(IMConstants.INVOICE_TYPE_OPPHØR.equalsIgnoreCase(invoiceTyp) && !transaction.getTransactionCategory().contains(IMConstants.TRANSACTION_CATEGORY_OPPHØR_SUFFIX))
                         {
                            transaction.setTransactionCategory(transaction.getTransactionCategory()+" "+IMConstants.TRANSACTION_CATEGORY_OPPHØR_SUFFIX);
                         }
                     }
                 }
             }
             }
          }

    }
}
