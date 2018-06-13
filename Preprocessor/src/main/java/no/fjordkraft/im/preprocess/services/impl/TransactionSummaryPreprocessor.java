package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.text.DateFormatSymbols;
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
        try{
       String brand =  request.getEntity().getSystemBatchInput().getTransferFile().getBrand();
        if(request.getStatement().getLegalPartClass().equals("Organization") && (!brand.equals("SEAS") && !brand.equals("VKAS")))
        {
            logger.debug("Legal Part Class = Organization for statement " + request.getEntity().getId());
            List<Transaction> transactions = request.getStatement().getTransactions().getTransaction();
            List<Attachment> attachments = request.getStatement().getAttachments().getAttachment();
            List<Distribution> distributions = null;
            Distribution distribution = null;
            if(request.getStatement().getTotalAttachment() ==1)
            {
                logger.debug("Statement "+request.getEntity().getId() + " has only one meter " );
                request.getStatement().setOneMeter(true);
                if(attachments.get(0)!=null && attachments.get(0).getFAKTURA().getVEDLEGGEMUXML().getInvoice()!=null
                        && attachments.get(0).getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder()!=null
                        && attachments.get(0).getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie()!=null ) {
                    String nettlieStartMonth = null;
                    if(attachments.get(0).getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getStartMonthAndYear()!=null)   {
                        nettlieStartMonth = attachments.get(0).getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getStartMonthAndYear();
                    }
                    if(nettlieStartMonth != null && attachments.get(0) != null && attachments.get(0).getStartMonthYear() != null && attachments.get(0).getStartMonthYear().equals(nettlieStartMonth))  {
                        request.getStatement().setStatementPeriod("Strøm og nettleie "+attachments.get(0).getStartMonthYear());
                    }
                    else
                    {
                        request.getStatement().setStatementPeriod("Strøm og nettleie ");
                    }
                } else {
                    request.getStatement().setStatementPeriod("Strøm for "+attachments.get(0).getStartMonthYear());
                }
            }
            Map<Float,List<Transaction>> vatVsListOfTransactions = new HashMap<Float,List<Transaction>>();
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
                                if (transaction.getReference().equals(attachment.getFAKTURA().getFAKTURANR()))
                                {
                                    attachment.setTransactionName(transaction.getTransactionCategory().substring(3));
                                    float transVat =  Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
                                    logger.debug("Transaction's vat Rate  " + transVat);
                                    Map<Float,Float> vatAndAmtOfLineItem = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getMapOfVatSumOfGross();
                                    if(!vatAndAmtOfLineItem.isEmpty() /*&& vatAndAmtOfLineItem.containsKey(transVat)*/ && vatAndAmtOfLineItem.size()==1)
                                    {
                                        Float vat = Float.valueOf(vatAndAmtOfLineItem.keySet().toArray()[0].toString());
                                        logger.debug("Attachment's vat is matching with transaction Vat rate ");
                                        transaction.setVatRate(String.valueOf(vat));
                                        if(Math.round(vatAndAmtOfLineItem.get(vat)) == Math.round(transaction.getAmount()*IMConstants.NEGATIVE))
                                        {
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
                                if(attachment!=null && attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder()!=null &&
                                   attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie()!=null &&
                                   transaction.getReference().equals(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getFakturanr()))
                                {
                                    isAttachmentFound = true;
                                    transaction.setTransactionName("Nettleie fra " +  attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getGridName());
                                    attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().setTransactionName(transaction.getTransactionCategory().substring(3));
                                    float transVat = Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
                                    logger.debug("Transaction's vat Rate  " + transVat);
                                    Map<Float,Float> vatAndAmtOfLineItem = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getMapOfVatSumOfGross();
                                    if(!vatAndAmtOfLineItem.isEmpty() && vatAndAmtOfLineItem.size()==1)
                                    {
                                        Float vat = Float.valueOf(vatAndAmtOfLineItem.keySet().toArray()[0].toString());
                                        transaction.setVatRate(String.valueOf(vat));
                                        if(Math.round(vatAndAmtOfLineItem.get(vat))== Math.round(transaction.getAmount()*IMConstants.NEGATIVE))
                                        {
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
                                        if(vatVsListOfTransactions.containsKey(null) && !vatVsListOfTransactions.get(null).isEmpty())  {
                                            listOfTransactions =  vatVsListOfTransactions.get(null);
                                                transaction.setMapOfVatVsAmount(vatAndAmtOfLineItem);
                                            listOfTransactions.add(transaction);
                                            vatVsListOfTransactions.put(null,listOfTransactions);
                                        } else {
                                           listOfTransactions = new ArrayList<Transaction>();
                                                transaction.setMapOfVatVsAmount(vatAndAmtOfLineItem);
                                            listOfTransactions.add(transaction);
                                        vatVsListOfTransactions.put(null,listOfTransactions);
                                        }
                                    }
                                }
                            }
                            if(!isAttachmentFound) {
                                    float transVat = Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
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
                                float vatRate = Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
                                List<Transaction> list = vatVsListOfTransactions.get(vatRate);
                                if(list ==null || list.isEmpty())
                                {
                                    list = new ArrayList<Transaction>();
                                }
                                list.add(transaction);
                                vatVsListOfTransactions.put(vatRate,list);
                                logger.debug("Adding Transaction"+ transaction.getFreeText() +" into vat Vs ListOfTransactions Map with Vat " + vatRate);
                            }
                            int vatRate = Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
                            transaction.setVatRate(String.valueOf(vatRate));
                            listOfOtherTrans.add(transaction);
                            logger.debug("Adding Transaction"+ transaction.getFreeText() +" as other transaction ");
                        }
                }
                else
                {
                    logger.debug("No Distributions for statement " + request.getEntity().getId());
                    float vatRate = Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
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
                float transVat =  Math.round(lineItem.getVatAmount()/lineItem.getAmount() *100);

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
            logger.debug("Looping for attachment to create attachment level transactionSummary");
            if(attachment!=null)
            {
                Map<Float,Float> stromVatAndSum = new HashMap<Float,Float>();
                Map<Float,Float> nettVatAndSum = new HashMap<Float,Float>();
                Set<Float> vatSet = new HashSet<Float>();
                String stromStartMonthYear = null;
                String nettStartMonthYear = null;
                if((attachment.getDisplayStromData()!=null && attachment.getDisplayStromData()) || attachment.getDisplayStromData() == null)
                {
                    logger.debug("Getting map of Vate and its sum for strom attachment " );
                    stromVatAndSum =  attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getMapOfVatSumOfGross();
                    stromStartMonthYear = attachment.getStartMonthYear();
                }
                //in case of only strom nettlie is null
                if(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie()!=null)
                {
                    logger.debug("Getting map of Vate and its sum for Nett attachment " );
                    nettVatAndSum = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getMapOfVatSumOfGross();
                    nettStartMonthYear = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getStartMonthAndYear();
                }

                vatSet.addAll(stromVatAndSum.keySet());
                vatSet.addAll(nettVatAndSum.keySet());
                List<TransactionSummary> transactionSummaryList = new ArrayList<TransactionSummary>();
                float sumInklMVA = 0.0f;
                        float sumExclMVA = 0.0f;
                for(Float vat : vatSet)
                {
                    float sumOfStrom = 0.0f;
                    float sumOfNett = 0.0f;
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
                    attachmentSummary.setSumOfNettStrom((sumOfStrom+sumOfNett));
                    attachmentSummary.setSumOfBelop(((sumOfStrom)+sumOfNett)*(vat/100));
                            sumExclMVA +=sumOfNett+sumOfStrom;
                    sumInklMVA+= attachmentSummary.getSumOfNettStrom()+attachmentSummary.getSumOfBelop();
                    if(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie()!=null)
                    {
                        attachmentSummary.setTotalVatAmount(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getTotalVatAmount());
                    }
                    transactionSummaryList.add(attachmentSummary);
                }
                attachment.setTransactionSummary(transactionSummaryList);
                attachment.setSumInklMVA(sumInklMVA);
                        attachment.setSumOfTransactions(sumExclMVA);
                if(stromStartMonthYear==null && nettStartMonthYear!=null )
                {
                    if(nettStartMonthYear.equals(startMonthYear) || (startMonthYear==null && isNettStartDate && isStromStartDate) ||(startMonthYear!=null && startMonthYear.equals("")))
                    {
                        startMonthYear = nettStartMonthYear;
                    }
                    else
                    {
                        startMonthYear = null;
                    }
                    isStromStartDate = false;
                    isNettStartDate = true;

                } else if(stromStartMonthYear!=null && nettStartMonthYear==null)
                {
                    if(stromStartMonthYear.equals(startMonthYear) /*||  (startMonthYear==null && isNettStartDate && isStromStartDate)*/
                            || (startMonthYear!=null && startMonthYear.equals("")))
                    {
                        startMonthYear = stromStartMonthYear;
                    }
                    else
                    {
                        startMonthYear = null;
                    }
                    isNettStartDate = false;
                    isStromStartDate = true;
                }   else if(stromStartMonthYear!=null && nettStartMonthYear!=null && stromStartMonthYear.equals(nettStartMonthYear) )
                {
                    if((startMonthYear!=null && stromStartMonthYear.equals(startMonthYear)) || startMonthYear==null)
                    {
                        startMonthYear = stromStartMonthYear;
                    }
                    else
                    {
                        request.getStatement().setStatementPeriod("Strom og Nettlie");
                    }
                    isStromStartDate = true;
                    isNettStartDate = true;
                }
                else if(stromStartMonthYear!=null && nettStartMonthYear!=null && !stromStartMonthYear.equals(nettStartMonthYear))
                {
                    isStromStartDate = false;
                    isNettStartDate = false;
                    startMonthYear = null;
                }
            }
        }
        if(!request.getStatement().isOneMeter())
        {
            if(isNettStartDate && isStromStartDate && startMonthYear!=null)
            {
                request.getStatement().setStatementPeriod("Strom og Nettlie av " + startMonthYear);
            }
            else if((isNettStartDate || isStromStartDate) && startMonthYear !=null )
            {
                if(isNettStartDate)
                {
                    request.getStatement().setStatementPeriod("Nettlie for " + startMonthYear);
                }
                if(isStromStartDate)
                {
                    request.getStatement().setStatementPeriod("Strom for " + startMonthYear);
                }
            }
            else
            {
                request.getStatement().setStatementPeriod("Strom og Nettlie");
            }
        }
       // request.getStatement().getTransactionGroup().setTransaction(null);
        String stromName = null;
        String nettName = null;
        float sumOfTransAmount = 0.0f;
                Map<Float,TransactionSummary> mapOfVatVsTransactionSummary = new HashMap<Float,TransactionSummary>();
        Map<String,Float> mapOfNameAndAmt = new HashMap<String,Float>();
        Map<String,Float>  mapOfNameAndVat = new HashMap<String,Float>();
        List<Transaction> processedOtherTrans = new ArrayList<Transaction>();

        for(Float vatAmount:vatVsListOfTransactions.keySet())
        {
            float sumOfKraftTrans = 0.0f;
            float sumOfNettTrans = 0.0f;
            float sumOfOtherTrans = 0.0f;
            float sumOfKrafts = 0.0f;
            float sumOfNetts = 0.0f;
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
                           sumOfKraftTrans = 0.0f;
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
                            nettName = tran.getTransactionCategory();
                            if(mapOfNameAndAmt.containsKey(nettName))
                            {
                                 sumOfNettTrans = mapOfNameAndAmt.get(nettName);
                                if(mapOfNameAndVat.containsKey(nettName)) {
                                   Float vatForNett = mapOfNameAndVat.get(nettName);
                                    if(vatAmount==null) {
                                        mapOfNameAndVat.put(nettName,vatAmount);
                                        mapOfNameAndAmt.put(nettName,sumOfNettTrans);
                                    }
                                }
                            } else
                            {
                                sumOfNettTrans = 0.0f;
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
                    transactionSummary.setSumOfNettStrom(request.getStatement().getTotalVatAmount());
                }
                transactionSummary.setSumOfBelop(sumOfKrafts+sumOfNetts+sumOfOtherTrans);
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
                        for (Float vat :transaction.getMapOfVatVsAmount().keySet()) {
                            if(mapOfVatVsTransactionSummary.containsKey(vat)){
                                TransactionSummary tranSummary = mapOfVatVsTransactionSummary.get(vat);
                                tranSummary.setSumOfBelop(tranSummary.getSumOfBelop() + transaction.getAmountBasedOnVat(vat));
                            }
                        }
                    }
                }

                request.getStatement().getTransactionGroup().setTransactionSummary(new ArrayList(mapOfVatVsTransactionSummary.values()));
        request.getStatement().getTransactionGroup().setSumOfTransactions(sumOfTransAmount);
        List<Transaction> processedTransaction = new ArrayList<Transaction>();
        for(String transactionName :mapOfNameAndAmt.keySet())
        {
            Transaction newTransaction = new Transaction();
            if(mapOfNameAndAmt.containsKey(transactionName)) {
                newTransaction.setTransactionCategory(transactionName.substring(3));
                newTransaction.setAmount(mapOfNameAndAmt.get(transactionName));
                if(mapOfNameAndVat.get(transactionName) == null) {
                    newTransaction.setVatRate("");
                }else {
                newTransaction.setVatRate(String.valueOf(mapOfNameAndVat.get(transactionName)));
                }
           processedTransaction.add(newTransaction);
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