package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.AuditLogService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;


@Service
@PreprocessorInfo(order=4)
public class ExtractInvoiceOrderProcessor extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(ExtractInvoiceOrderProcessor.class);

    @Autowired
    @Qualifier("unmarshaller")
    private Unmarshaller unMarshaller;

    @Autowired
    @Qualifier("marshaller")
    private Marshaller marshaller;

    @Autowired
    private AuditLogService auditLogService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request)
    {
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("Check EMUXML Attachments with Multiple Invoice Order and create new Attachments");
            HashMap<Long,InvoiceOrder> malepunktVsOrder = new HashMap<Long,InvoiceOrder>();
            ArrayList<Attachment> listOfNewAttachments = new ArrayList<Attachment>();
            List<Attachment> attachments = request.getStatement().getAttachments().getAttachment();
            List<Transaction> listOfTransactions = new ArrayList<Transaction>();
            Attachment emuxmlAttachment  = null;

            for( Attachment attachment :request.getStatement().getAttachments().getAttachment())
            {
                FAKTURA faktura = attachment.getFAKTURA();
                if(faktura.getVEDLEGGFORMAT().equalsIgnoreCase("EMUXML") && faktura.getVEDLEGGEMUXML().getInvoice().getInvoiceOrder().size()>1) {
                        emuxmlAttachment = attachment;
                        for(InvoiceOrder invoiceOrder :faktura.getVEDLEGGEMUXML().getInvoice().getInvoiceOrder())
                        {
                            String message = "Found Multiple invoice orders";
                            auditLogService.saveAuditLog(request.getEntity().getId(),StatementStatusEnum.PRE_PROCESSING.getStatus(),message,IMConstants.INFO);
                            if( invoiceOrder.getSupplyPointInfo117()!=null)
                            {
                                long malepunktID= invoiceOrder.getSupplyPointInfo117().getObjectId();
                                malepunktVsOrder.put(Long.valueOf(malepunktID), invoiceOrder);
                                ArrayList listOfInvoice = new ArrayList();
                                if(Long.compare(malepunktID,faktura.getMAALEPUNKT())==0)
                                {
                                    listOfInvoice.add(invoiceOrder);
                                    attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().setInvoiceOrder(listOfInvoice);
                                    listOfTransactions.addAll(getKraftTransactionGroup(request.getStatement().getTransactions().getTransaction(), attachment));
                                }
                                else
                                {
                                    Attachment newAttachment = deepClone(emuxmlAttachment);
                                    newAttachment.getFAKTURA().setMAALEPUNKT(malepunktID);
                                    listOfInvoice.add(malepunktVsOrder.get(Long.valueOf(malepunktID)));
                                    newAttachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().setInvoiceOrder(listOfInvoice);
                                    listOfNewAttachments.add(newAttachment);
                                }
                            } else
                            {
                                String errorMessage = "Missing SupplyPointInfo117 in Invoice Order" ;
                                auditLogService.saveAuditLog(request.getEntity().getId(), StatementStatusEnum.PRE_PROCESSING.getStatus(), errorMessage, IMConstants.WARNING);
                            }
                        }

                    }
                    //IM-53 ; If freeText is not available then get the information from supplypointinfo-117.streetno
                    if(faktura.getVEDLEGGFORMAT().equalsIgnoreCase("EMUXML")){
                          if(attachment.getFaktura().getFreeText()==null )
                          {
                            Transaction transaction =  getKraftTransaction(request.getStatement().getTransactions().getTransaction(),attachment) ;
                             if(transaction!=null && (transaction.getFreeText()==null || (transaction.getFreeText()!=null && transaction.getFreeText().isEmpty()))&& attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getSupplyPointInfo117()!=null)
                             {
                                 String message = "Kraft Transaction "+transaction.getMaalepunktID()+" has no free text.Setting value " +attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getSupplyPointInfo117().getStreetNo() ;
                                 auditLogService.saveAuditLog(request.getEntity().getId(), StatementStatusEnum.PRE_PROCESSING.getStatus(), message, IMConstants.INFO);
                                 transaction.setFreeText(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getSupplyPointInfo117().getStreetNo());
                             }
                          }
                    }
                }


           addNewAttachmentsAndTransactions(listOfNewAttachments,request,listOfTransactions);
           stopWatch.stop();
           logger.debug("TIme taken for creating new attachment of statement with id  " + request.getEntity().getId() + stopWatch.prettyPrint());
        } catch (Exception e) {
            logger.error("Exception in Extract Invoice Order preprocessor",e);
            throw new PreprocessorException(e);
        }

    }

    private Transaction getKraftTransaction(List<Transaction> transactions, Attachment attachment) {
        List<Transaction> listOfNewTransactions = new ArrayList<Transaction>();
        for(Transaction transaction: transactions)
        {

            if(transaction.getReference().equals(attachment.getFAKTURA().getFAKTURANR()))
            {
                int k = 0;
                for(Distribution distribution : transaction.getDistributions().getDistribution())
                {

                    float invoiceGrossTotal =  attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getInvoiceOrderAmounts113().getGrossTotal();
                    float kraftDistAmt   =     distribution.getAmount();
                    if(Float.compare(invoiceGrossTotal, StrictMath.abs(kraftDistAmt))==0)
                    {
                        return transaction;
                    }
                }
            }
        }
        return null;
    }

    private List<Transaction>  getKraftTransactionGroup(List<Transaction> transactions,Attachment attachment) {

        List<Distribution> listOfDistribution =null ;
        List<Transaction> listOfNewTransactions = new ArrayList<Transaction>();
        Transaction newTransaction =null;
        for(Transaction transaction: transactions)
        {

            if(transaction.getReference().equals(attachment.getFAKTURA().getFAKTURANR()))
            {
                newTransaction = transaction;
                int k = 0;
                for(Distribution distribution : transaction.getDistributions().getDistribution())
                {
                    listOfDistribution = new ArrayList<Distribution>();
                   float invoiceGrossTotal =  attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getInvoiceOrderAmounts113().getGrossTotal();
                    float kraftDistAmt   =     distribution.getAmount();
                     if(Float.compare(StrictMath.abs(invoiceGrossTotal), StrictMath.abs(kraftDistAmt))==0)
                     {
                              listOfDistribution.add(distribution);
                              transaction.setAmountWithVat(kraftDistAmt);
                              transaction.getDistributions().setDistribution(listOfDistribution);
                     }
                    else

                     {
                            Transaction transactionNew = deepClone(newTransaction,k);
                            listOfDistribution.add(distribution);
                            transactionNew.setAmountWithVat(kraftDistAmt);
                            transactionNew.getDistributions().setDistribution(listOfDistribution);
                            listOfNewTransactions.add(transactionNew);
                     }
                    k++;
                }


            }


         }
        return listOfNewTransactions;
    }

    private void addNewAttachmentsAndTransactions(ArrayList<Attachment> listOfNewAttachments,PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request, List<Transaction> listOfTrans) {
        for(Attachment attachment:listOfNewAttachments )
        {
            for(Transaction trans : listOfTrans)
            {
               float invoiceGrossTotal =  attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getInvoiceOrderAmounts113().getGrossTotal();
                float kraftDistAmount = trans.getDistributions().getDistribution().get(0).getAmount();
                if(Float.compare(StrictMath.abs(kraftDistAmount),invoiceGrossTotal)==0 )
                {
                     trans.setFreeText(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getSupplyPointInfo117().getStreetNo().trim());
                     trans.setAmountWithVat(kraftDistAmount);
                     attachment.getFAKTURA().setFAKTURANR(trans.getReference());
                     request.getStatement().getAttachments().getAttachment().add(attachment);
                     request.getStatement().getTransactions().getTransaction().add(trans);
                }

            }
        }

    }

    private Attachment deepClone(Attachment stromAttachment) {
        try {
            logger.debug("cloning stromAttachment " + stromAttachment.getFAKTURA().getMAALEPUNKT());

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            marshaller.marshal(stromAttachment, new StreamResult(byteArrayOutputStream));

            StreamSource source = new StreamSource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            Attachment attachment = (Attachment) unMarshaller.unmarshal(source);
            //attachment.getFAKTURA().setFAKTURANR(String.valueOf(referenceNo));
            attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setNettleie(null);
            logger.debug("cloning stromAttachment " + stromAttachment.getFAKTURA().getMAALEPUNKT());
           // attachment.setDisplayStromData(false);
            return attachment;
        } catch (Exception e) {
            throw new PreprocessorException(e);
        }
    }

    private Transaction deepClone(Transaction kraftTransaction, int k) {
        try {
            logger.debug("cloning Transaction having reference " + kraftTransaction.getReference());

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            marshaller.marshal(kraftTransaction, new StreamResult(byteArrayOutputStream));

            StreamSource source = new StreamSource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            Transaction transaction = (Transaction) unMarshaller.unmarshal(source);
              Random random = new Random();
            String refNo = transaction.getReference()+"-" +k;
             transaction.setReference(refNo);
             transaction.getDistributions().getDistribution().clear();
            logger.debug("cloning kraftTransaction with Reference " + transaction.getReference());
            //attachment.setDisplayStromData(false);
            return transaction;
        } catch (Exception e) {
            throw new PreprocessorException(e);
        }
    }
}
