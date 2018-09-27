package no.fjordkraft.im.preprocess.services.impl;

import com.carfey.jdk.collection.CollectionUtil;
import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 5/17/18
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
@PreprocessorInfo(order=17,legalPartClass = "Organization")
public class TransactionSummaryPreprocessor extends BasePreprocessor {
    private static final Logger logger = LoggerFactory.getLogger(TransactionSummaryPreprocessor.class);

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request)
    {
        DecimalFormat df=new DecimalFormat("0.00");
        try{
       String brand =  request.getEntity().getSystemBatchInput().getBrand();
        if(request.getStatement().getLegalPartClass().equals("Organization") && (brand.equals("FKAS") || brand.equals("TKAS")))
        {
            logger.debug("Legal Part Class = Organization for statement " + request.getEntity().getId());
            List<Transaction> transactions = request.getStatement().getTransactions().getTransaction();
            List<Attachment> attachments = request.getStatement().getAttachments().getAttachment();
            List<Distribution> distributions = null;
            Distribution distribution = null;
            Map<Double,List<Transaction>> vatVsListOfTransactions = new HashMap<Double,List<Transaction>>();
            List<Transaction> listOfOtherTrans = new ArrayList<Transaction>();
            List<Transaction> listOfTransactions = null;
            for(Transaction transaction: transactions)
            {
                logger.debug("looping for each transaction " + transaction.getTransactionId());
                if (null != transaction.getDistributions())
                {
                    distributions = transaction.getDistributions().getDistribution();
                    if(!distributions.isEmpty())
                    {
                        distribution = distributions.get(0);
                        if (IMConstants.KRAFT.equals(distribution.getName()))
                        {
                            logger.debug("Kraft Transaction " + transaction.getTransactionCategory());
                            for (Attachment attachment : attachments)
                                {
                                    if((attachment.getDisplayStromData()!=null &&  attachment.getDisplayStromData())|| attachment.isOnlyGrid())
                                    {
                                        if (transaction.getReference().equals(attachment.getFAKTURA().getFAKTURANR()))
                                        {
                                            attachment.setTransactionName(transaction.getTransactionCategory().substring(3));
                                            double transVat =  Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
                                            logger.debug("Transaction's vat Rate  " + transVat);
                                            Map<Double,Double> vatAndAmtOfLineItem = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getMapOfVatSumOfGross();
                                            if(!vatAndAmtOfLineItem.isEmpty() /*&& vatAndAmtOfLineItem.containsKey(transVat)*/ && vatAndAmtOfLineItem.size()==1)
                                            {
                                                Double vat = Double.valueOf(vatAndAmtOfLineItem.keySet().toArray()[0].toString());
                                                logger.debug("Attachment's vat is matching with transaction Vat");
                                                transaction.setVatRate(String.valueOf(vat));
                                                if(Math.round(vatAndAmtOfLineItem.get(vat)) == Math.round(transaction.getAmount()*IMConstants.NEGATIVE))
                                                {
                                                    logger.debug("Attachment's amount is matching with transaction's amount ");
                                                    if(!vatVsListOfTransactions.containsKey(vat))
                                                    {
                                                        listOfTransactions = new ArrayList<Transaction>();
                                                    } else
                                                    {
                                                        listOfTransactions = vatVsListOfTransactions.get(vat);
                                                    }
                                                    transaction.setTransactionType(IMConstants.KRAFT);
                                                    listOfTransactions.add(transaction);
                                                    vatVsListOfTransactions.put(vat,listOfTransactions);
                                                    logger.debug("Adding Transaction"+ transaction.getFreeText() +" into vat Vs ListOfTransactions Map with Vat " + transVat);
                                                }
                                            }   else if(!vatAndAmtOfLineItem.isEmpty() && vatAndAmtOfLineItem.size()>1 ){
                                                logger.debug("Found multiple vat rate for attachment "+ attachment.getFAKTURA().getFAKTURANR());
                                                if(vatVsListOfTransactions.containsKey(null) && !vatVsListOfTransactions.get(null).isEmpty())  {
                                                    listOfTransactions =  vatVsListOfTransactions.get(null);
                                                    transaction.setMapOfVatVsAmount(vatAndAmtOfLineItem);
                                                    transaction.setTransactionType(IMConstants.KRAFT);
                                                    listOfTransactions.add(transaction);
                                                    vatVsListOfTransactions.put(null,listOfTransactions);
                                                } else {
                                                    listOfTransactions = new ArrayList<Transaction>();
                                                    transaction.setMapOfVatVsAmount(vatAndAmtOfLineItem);
                                                    transaction.setTransactionType(IMConstants.KRAFT);
                                                    listOfTransactions.add(transaction);
                                                    vatVsListOfTransactions.put(null,listOfTransactions);
                                                }
                                            }
                                        }
                                    }
                                }
                        }
                        else if(IMConstants.NETT.equals(distribution.getName()))
                        {
                            logger.debug("NETT Transaction " + transaction.getTransactionCategory());
                            boolean isAttachmentFound = false;
                            for(Attachment attachment:attachments)
                            {
                                if((attachment.getDisplayStromData()!=null && attachment.getDisplayStromData()) || attachment.isOnlyGrid())
                                {
                                    if(attachment!=null && attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder()!=null &&
                                       attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie()!=null &&
                                       transaction.getReference().equals(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getFakturanr()))
                                    {
                                        isAttachmentFound = true;
                                        transaction.setTransactionName("Nettleie fra " +  attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getGridName());
                                        attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().setTransactionName(transaction.getTransactionCategory().substring(3));
                                        double transVat = Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
                                        logger.debug("Transaction's vat Rate  " + transVat);
                                        Map<Double,Double> vatAndAmtOfLineItem = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getMapOfVatSumOfGross();
                                        if(!vatAndAmtOfLineItem.isEmpty() && vatAndAmtOfLineItem.size()==1)
                                        {
                                            Double vat = Double.valueOf(vatAndAmtOfLineItem.keySet().toArray()[0].toString());
                                            transaction.setVatRate(String.valueOf(vat));
                                            if(Math.round(vatAndAmtOfLineItem.get(vat))== Math.round(transaction.getAmount()*IMConstants.NEGATIVE))
                                            {
                                            logger.debug("Attachment's amount is matching with transaction amount  " + transVat);
                                            if(!vatVsListOfTransactions.containsKey(vat))
                                            {
                                                listOfTransactions = new ArrayList<Transaction>();
                                            }
                                            else
                                            {
                                                listOfTransactions = vatVsListOfTransactions.get(vat);
                                            }
                                            transaction.setTransactionType(IMConstants.NETT);
                                            listOfTransactions.add(transaction);
                                            vatVsListOfTransactions.put(vat,listOfTransactions);
                                            logger.debug("Adding Transaction"+ transaction.getFreeText() +" into vat Vs ListOfTransactions Map with Vat " + transVat);
                                        }
                                        } else if(!vatAndAmtOfLineItem.isEmpty() && vatAndAmtOfLineItem.size()>1 ){
                                            logger.debug("Found multiple vat rate for attachment "+ attachment.getFAKTURA().getFAKTURANR());
                                            if(vatVsListOfTransactions.containsKey(null) && !vatVsListOfTransactions.get(null).isEmpty())  {
                                                listOfTransactions =  vatVsListOfTransactions.get(null);
                                                    transaction.setMapOfVatVsAmount(vatAndAmtOfLineItem);
                                                transaction.setTransactionType(IMConstants.NETT);
                                                listOfTransactions.add(transaction);
                                                vatVsListOfTransactions.put(null,listOfTransactions);
                                            } else {
                                               listOfTransactions = new ArrayList<Transaction>();
                                                    transaction.setMapOfVatVsAmount(vatAndAmtOfLineItem);
                                                transaction.setTransactionType(IMConstants.NETT);
                                                listOfTransactions.add(transaction);
                                            vatVsListOfTransactions.put(null,listOfTransactions);
                                            }
                                        }
                                    }
                                }
                            }
                            if(!isAttachmentFound) {
                                    logger.debug("Attachment not found for transaction  " + transaction.getReference());
                                    double transVat = Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
                                    if(vatVsListOfTransactions.containsKey(transVat)) {
                                        listOfTransactions = vatVsListOfTransactions.get(transVat);
                                    } else {
                                        listOfTransactions = new ArrayList<Transaction>();
                                    }
                                    listOfTransactions.add(transaction);
                                    vatVsListOfTransactions.put(transVat,listOfTransactions);

                            }
                        }
                        else
                        {
                            logger.debug("Others Transaction "+ transaction.getTransactionCategory());
                            if(transaction.getTransactionCategory().toUpperCase().contains("KR;"))
                            {
                                logger.debug("in case transaction has KR; it should be treated as Kraft transaction " );
                                double vatRate = Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
                                List<Transaction> list = vatVsListOfTransactions.get(vatRate);
                                if(list ==null || list.isEmpty())
                                {
                                    list = new ArrayList<Transaction>();
                                }
                                list.add(transaction);
                                vatVsListOfTransactions.put(vatRate,list);
                                logger.debug("Adding Transaction"+ transaction.getFreeText() +" into vat Vs ListOfTransactions Map with Vat " + vatRate);
                            }
                            long vatRate = Math.round((transaction.getVatAmount()/transaction.getAmount())*100);
                            transaction.setVatRate(String.valueOf(vatRate));
                            listOfOtherTrans.add(transaction);
                            logger.debug("Adding Transaction"+ transaction.getFreeText() +" as other transaction ");
                        }
                }
                else
                {
                    logger.debug("No Distributions for transaction " + transaction.getReference());
                    double vatRate = Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
                    List<Transaction> list = vatVsListOfTransactions.get(vatRate);
                    if(list ==null || list.isEmpty())
                    {
                        list = new ArrayList<Transaction>();
                    }
                    list.add(transaction);
                    vatVsListOfTransactions.put(vatRate,list);
                    logger.debug("Adding Transaction"+ transaction.getFreeText() +" into vat Vs ListOfTransactions Map with Vat " + vatRate);
                    transaction.setVatRate(String.valueOf(vatRate));
                    listOfOtherTrans.add(transaction);
                    logger.debug("Adding Transaction"+ transaction.getFreeText() +" as other transaction ");
                }

            }
        }

        //Code to add lineitems in the transactions
       List<LineItem> lineItems =   request.getStatement().getLineItems().getLineItem();
        if(lineItems!=null && lineItems.size()>0) {
            for(LineItem lineItem : lineItems) {
                double transVat =  Math.round(lineItem.getVatAmount()/lineItem.getAmount() *100);

                Transaction transaction = new Transaction();
                transaction.setAmount(lineItem.getAmount());
                transaction.setAmountWithVat(lineItem.getAmountWithVat());
                transaction.setVatAmount(lineItem.getVatAmount());
                transaction.setVatRate(String.valueOf(transVat));
                if(lineItem.getLineItemCategory().indexOf(";") != -1) {
                    transaction.setTransactionCategory(lineItem.getLineItemCategory());
                } else {
                    transaction.setTransactionCategory(lineItem.getLineItemCategory());
                }
                 if(lineItem.getClosedClaimStatementSequenceNumber()!=0) {
                     transaction.setTransactionCategory(lineItem.getLineItemCategory()+ " fakturanr. "+request.getStatement().getAccountNumber()+lineItem.getClosedClaimStatementSequenceNumber());
                 }
                List<Transaction> listOfTrans = new ArrayList<Transaction>();
                if( vatVsListOfTransactions.containsKey(transVat))  {
                     listOfTrans = vatVsListOfTransactions.get(transVat);
                }
                listOfTrans.add(transaction);
                vatVsListOfTransactions.put(transVat,listOfTrans);
                listOfOtherTrans.add(transaction);

            }
        }
        boolean isStromStartDate = true;
        boolean isNettStartDate = true;
        String startMonthYear = "";
        for(Attachment attachment:attachments)
        {
            if((attachment.getDisplayStromData()!=null && attachment.getDisplayStromData()) || attachment.isOnlyGrid())
            {
                logger.debug("Looping for attachment to create attachment level transactionSummary");
                if(attachment!=null)
                {
                    Map<Double,Double> stromVatAndSum = new HashMap<Double,Double>();
                    Map<Double,Double> nettVatAndSum = new HashMap<Double,Double>();
                    Set<Double> vatSet = new HashSet<Double>();
                    String stromStartMonthYear = null;
                    String nettStartMonthYear = null;
                    if((attachment.getDisplayStromData()!=null && attachment.getDisplayStromData()) || attachment.getDisplayStromData() == null)
                    {
                        logger.debug("Getting map of Vat and its sum for strom attachment " );
                        stromVatAndSum =  attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getMapOfVatSumOfGross();
                        stromStartMonthYear = attachment.getStartMonthYear();
                    }
                    //in case of only strom nettlie is null
                    if(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie()!=null)
                    {
                        logger.debug("Getting map of Vat and its sum for Nett attachment " );
                        nettVatAndSum = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getMapOfVatSumOfGross();
                        nettStartMonthYear = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getStartMonthAndYear();
                    }

                    vatSet.addAll(stromVatAndSum.keySet());
                    vatSet.addAll(nettVatAndSum.keySet());
                    List<TransactionSummary> transactionSummaryList = new ArrayList<TransactionSummary>();
                    double sumInklMVA = 0.0;
                    double sumExclMVA = 0.0;
                    //this for loop calculates sum Exclusive MVA and inclusive MVA and creates transaction summary for first page.
                    for(Double vat : vatSet)
                    {
                        double sumOfStrom = 0.0;
                        double sumOfNett = 0.0;
                        if(stromVatAndSum.containsKey(vat))
                        {
                            sumOfStrom =  stromVatAndSum.get(vat);
                        }
                        if(nettVatAndSum.containsKey(vat))
                        {
                            sumOfNett = nettVatAndSum.get(vat);
                        }
                        TransactionSummary attachmentSummary = new TransactionSummary();
                        attachmentSummary.setMvaValue(vat);
                        attachmentSummary.setSumOfNettStrom(Double.valueOf(df.format(sumOfStrom + sumOfNett)));
                        attachmentSummary.setSumOfBelop(Double.valueOf(df.format(((sumOfStrom)+sumOfNett)*(vat/100))));
                        sumExclMVA +=sumOfNett+sumOfStrom;
                        sumInklMVA+= attachmentSummary.getSumOfNettStrom()+attachmentSummary.getSumOfBelop();
                        if(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie()!=null)
                        {
                            attachmentSummary.setTotalVatAmount(Double.valueOf(df.format(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getTotalVatAmount())));
                        }
                        transactionSummaryList.add(attachmentSummary);
                    }
                    attachment.setTransactionSummary(transactionSummaryList);
                    attachment.setSumInklMVA(Double.valueOf(df.format(sumInklMVA)));
                    attachment.setSumOfTransactions(Double.valueOf(df.format(sumExclMVA)));
                }
            }
        }
       // request.getStatement().getTransactionGroup().setTransaction(null);
        String stromName = null;
        String nettName = null;
        double sumOfTransAmount = 0.0;
        Map<Double,TransactionSummary> mapOfVatVsTransactionSummary = new HashMap<Double,TransactionSummary>();
        Map<String,Double> mapOfNameAndAmt = new HashMap<String,Double>();
        Map<String,Double>  mapOfNameAndVat = new HashMap<String,Double>();
        List<Transaction> processedOtherTrans = new ArrayList<Transaction>();

        for(Double vatAmount:vatVsListOfTransactions.keySet())
        {
            double sumOfKraftTrans = 0.0;
            double sumOfNettTrans = 0.0;
            double sumOfOtherTrans = 0.0;
            double sumOfKrafts = 0.0;
            double sumOfNetts = 0.0;
            List<Transaction> listOfNettStrom = vatVsListOfTransactions.get(vatAmount);
            if(listOfNettStrom!=null && !listOfNettStrom.isEmpty())
            {
                for(Transaction tran :listOfNettStrom)
                {
                    if(IMConstants.KRAFT.equals(tran.getTransactionType()))
                    {
                        stromName = tran.getTransactionCategory();
                        if(mapOfNameAndAmt.containsKey(stromName))
                        {
                            sumOfKraftTrans =  mapOfNameAndAmt.get(stromName);
                        }
                        else
                        {
                           sumOfKraftTrans = 0.0;
                        }
                        sumOfKraftTrans+=tran.getAmount()*IMConstants.NEGATIVE;
                        sumOfKrafts+=  tran.getAmount()*IMConstants.NEGATIVE;
                        mapOfNameAndVat.put(stromName,vatAmount);
                        mapOfNameAndAmt.put(stromName,sumOfKraftTrans);
                    }
                    else
                    {
                        if(!listOfOtherTrans.contains(tran))
                        {
                            if(tran.getTransactionName()!=null) {
                                nettName = tran.getTransactionName();
                            } else {
                            nettName = tran.getTransactionCategory().substring(3);
                            }
                            if(mapOfNameAndAmt.containsKey(nettName))
                            {
                                 sumOfNettTrans = mapOfNameAndAmt.get(nettName);
                                if(mapOfNameAndVat.containsKey(nettName)) {
                                   Double vatForNett = mapOfNameAndVat.get(nettName);
                                    if(vatAmount==null) {
                                        mapOfNameAndVat.put(nettName,vatAmount);
                                        mapOfNameAndAmt.put(nettName,sumOfNettTrans);
                                    }
                                }
                            } else
                            {
                                sumOfNettTrans = 0.0;
                            }
                            sumOfNettTrans +=tran.getAmount()*IMConstants.NEGATIVE;
                            sumOfNetts+=tran.getAmount()*IMConstants.NEGATIVE;
                            mapOfNameAndVat.put(nettName,vatAmount);
                            mapOfNameAndAmt.put(nettName,sumOfNettTrans);
                        }
                        else
                        {
                            tran.setAmount(tran.getAmount()*IMConstants.NEGATIVE);
                            processedOtherTrans.add(tran);
                            sumOfOtherTrans+=tran.getAmount();
                        }
                    }
                }
                if(vatAmount!=null)   {
                TransactionSummary transactionSummary = new TransactionSummary();
                transactionSummary.setMvaValue(vatAmount);
                if(vatAmount!=0.0)
                {
                    transactionSummary.setSumOfNettStrom(Double.valueOf(df.format(request.getStatement().getTotalVatAmount())));
                }
                transactionSummary.setSumOfBelop(Double.valueOf(df.format(sumOfKrafts+sumOfNetts+sumOfOtherTrans)));
                sumOfTransAmount +=transactionSummary.getSumOfBelop();
                            mapOfVatVsTransactionSummary.put(vatAmount, transactionSummary);
                        } else {
                    sumOfTransAmount+=sumOfKrafts+sumOfNetts+sumOfOtherTrans;
                }

            }
        }


                List<Transaction> listOfTrans = vatVsListOfTransactions.get(null);
                if(listOfTrans!=null && listOfTrans.size()>0) {
                    for(Transaction transaction: listOfTrans) {
                        for (Double vat :transaction.getMapOfVatVsAmount().keySet()) {
                            if(mapOfVatVsTransactionSummary.containsKey(vat)){
                                TransactionSummary tranSummary = mapOfVatVsTransactionSummary.get(vat);
                                tranSummary.setSumOfBelop(Double.valueOf(df.format(tranSummary.getSumOfBelop() + transaction.getAmountBasedOnVat(vat))));
                            }
                        }
                    }
                }

                request.getStatement().getTransactionGroup().setTransactionSummary(new ArrayList(mapOfVatVsTransactionSummary.values()));
        request.getStatement().getTransactionGroup().setSumOfTransactions(Double.valueOf(df.format(sumOfTransAmount)));
        Map<String,Transaction> mapOfTransaction = new HashMap<String,Transaction>();
        List<Transaction> processedTransaction = new ArrayList<Transaction>();
        double stromAmount = 0.0;
        double nettAmount = 0.0;
        for(String transactionName :mapOfNameAndAmt.keySet())
        {
            Transaction newTransaction = null;
            if(mapOfNameAndAmt.containsKey(transactionName)) {
                if(transactionName.contains("KR;") || transactionName.contains("KN;")) {
                    if(mapOfTransaction.containsKey(IMConstants.KRAFT)) {
                        newTransaction = mapOfTransaction.get(IMConstants.KRAFT);
                    }
                    else {
                        newTransaction = new Transaction();
                    }
                    stromAmount+=mapOfNameAndAmt.get(transactionName);
                    if(request.getEntity().getSystemBatchInput().getBrand().equals("TKAS")) {
                        newTransaction.setTransactionCategory("Strøm fra TrøndelagKraft");
                    }else {
                    newTransaction.setTransactionCategory("Strøm fra Fjordkraft");
                    }
                    newTransaction.setAmount(Double.valueOf(df.format(stromAmount)));
                    mapOfTransaction.put(IMConstants.KRAFT,newTransaction);
                } else
                {
                    if(mapOfTransaction.containsKey(IMConstants.NETT)) {
                        newTransaction = mapOfTransaction.get(IMConstants.NETT);
                        newTransaction.setTransactionCategory("Nettleie fra netteier");
                    }   else {
                        newTransaction = new Transaction();
                        newTransaction.setTransactionCategory(transactionName);
                    }
                    nettAmount+=mapOfNameAndAmt.get(transactionName);
                    newTransaction.setAmount(nettAmount);
                    mapOfTransaction.put(IMConstants.NETT,newTransaction);
                }
                if(mapOfNameAndVat.get(transactionName) == null) {
                    newTransaction.setVatRate("");
                }else {
                newTransaction.setVatRate(String.valueOf(mapOfNameAndVat.get(transactionName)));
                }
            }
        }

      for(String tranType:mapOfTransaction.keySet())  {
          if(tranType.equals(IMConstants.KRAFT)) {
              processedTransaction.add(0,mapOfTransaction.get(IMConstants.KRAFT));
          }else {
              processedTransaction.add(mapOfTransaction.get(IMConstants.NETT));
          }
      }
       processedTransaction.addAll(processedOtherTrans);
        request.getStatement().getTransactionGroup().setTransaction(processedTransaction);
        }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception in Transaction Summary preprocessor",e);
            throw new PreprocessorException(e);
        }
    }

    private String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
}