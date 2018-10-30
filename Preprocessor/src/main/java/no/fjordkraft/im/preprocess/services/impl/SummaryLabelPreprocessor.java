package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Attachment;
import no.fjordkraft.im.if320.models.InvoiceOrder;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.if320.models.Transaction;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bhavi on 6/13/2018.
 */
@Service
@PreprocessorInfo(order=21)
public class SummaryLabelPreprocessor  extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(SummaryLabelPreprocessor.class);

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {

        String  brand =  request.getEntity().getSystemBatchInput().getBrand();
        if(request.getStatement().getLegalPartClass().equals(IMConstants.LEGAL_PART_CLASS_ORGANIZATION)  && (brand.equals("FKAS") || brand.equals("TKAS")))
        {
            logger.debug("SummaryLabelPreprocessor..........");
            boolean hasStrom = false;
            boolean hasNett = false;

            Statement stmt = request.getStatement();
            List<Attachment> attachmentList = stmt.getAttachments().getAttachment();

            for(Attachment attachment: attachmentList) {
                if(attachment.isOnlyGrid() == true && !attachment.getDisplayStromData()) {
                    attachment.setLabelStromNetSummary("Sum nettleie");
                    continue;
                }

                if(attachment.getFAKTURA()!=null && null != attachment.getFAKTURA().getNettleieList() && attachment.getFAKTURA().getNettleieList().size()>0) {
                    hasNett = true;
                }

                if(hasNett) {
                    attachment.setLabelStromNetSummary("Sum strøm og nettleie");
                } else {
                    attachment.setLabelStromNetSummary("Sum strøm");
                }
                hasNett = false;
                hasStrom = false;
            }

            for(Transaction transaction : stmt.getTransactionGroup().getTransaction()){
                if(transaction.getTransactionCategory().toLowerCase().contains("strøm") || transaction.getTransactionCategory().toLowerCase().contains("kreditnota") ) {
                    hasStrom = true;
                } else if(transaction.getTransactionCategory().toLowerCase().contains("nettleie") ){
                    hasNett = true;
                }
            }

            if(hasStrom && hasNett) {
                stmt.getTransactionGroup().setLabelKraftNettSummary("Strøm og nettleie");
            } else if(hasStrom && !hasNett) {
                stmt.getTransactionGroup().setLabelKraftNettSummary("Strøm");
            } else if(hasNett && !hasStrom){
                stmt.getTransactionGroup().setLabelKraftNettSummary("Nettleie");
            }
        }

    }
}