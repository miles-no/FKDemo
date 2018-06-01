package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.util.IMConstants;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.OrganizationDepartment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormatSymbols;
import java.time.Month;
import java.time.format.TextStyle;
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
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request)  {
        if(request.getStatement().getLegalPartClass().equals("Organization"))   {
        List<Transaction> transactions = request.getStatement().getTransactions().getTransaction();
        List<Attachment> attachments = request.getStatement().getAttachments().getAttachment();
        List<Distribution> distributions = null;
        Distribution distribution = null;
        if(request.getStatement().getTotalAttachment() ==1)  {
            logger.debug("Statement "+request.getEntity().getId() + " has only one meter " );
            request.getStatement().setOneMeter(true);
            request.getStatement().setStatementPeriod("Strøm for "+attachments.get(0).getStartMonthYear());
        }
        Map<Float,List<Transaction>> vatVsListOfTransactions = new HashMap<Float,List<Transaction>>();
        List<Transaction> listOfOtherTrans = new ArrayList<Transaction>();
        List<Transaction> listOfTransactions = null;
        for(Transaction transaction: transactions) {

            if (null != transaction.getDistributions()) {
                distributions = transaction.getDistributions().getDistribution();
                if(!distributions.isEmpty())  {
                    distribution = distributions.get(0);
                    if (IMConstants.KRAFT.equals(distribution.getName())) {
                        for (Attachment attachment : attachments) {
                            if (transaction.getReference().equals(attachment.getFAKTURA().getFAKTURANR())) {
                                attachment.setTransactionName(transaction.getTransactionCategory().substring(3));
                                float transVat = Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
                                Map<Float,Float> vatAndAmtOfLineItem = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getMapOfVatSumOfGross();
                                if(!vatAndAmtOfLineItem.isEmpty() && vatAndAmtOfLineItem.containsKey(transVat) && vatAndAmtOfLineItem.size()==1) {
                                    transaction.setVatRate(transVat);
                                    if(Math.round(vatAndAmtOfLineItem.get(transVat)) == Math.round(Math.abs(transaction.getAmount()))) {
                                        if(!vatVsListOfTransactions.containsKey(transVat)) {
                                            listOfTransactions = new ArrayList<Transaction>();
                                        } else {
                                            listOfTransactions = vatVsListOfTransactions.get(transVat);
                                        }
                                        transaction.setTransactionType(IMConstants.KRAFT);
                                        listOfTransactions.add(transaction);
                                        vatVsListOfTransactions.put(transVat,listOfTransactions);
                                    }
                                }
                            }
                        }
                    } else if(IMConstants.NETT.equals(distribution.getName())) {
                        for(Attachment attachment:attachments) {
                            if(attachment!=null && attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder()!=null && attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie()!=null && attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getFakturanr().equals(transaction.getReference())) {
                                attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().setTransactionName(transaction.getTransactionCategory().substring(3));
                                float transVat = Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
                                Map<Float,Float> vatAndAmtOfLineItem = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getMapOfVatSumOfGross();
                                if(!vatAndAmtOfLineItem.isEmpty() && vatAndAmtOfLineItem.containsKey(transVat) && vatAndAmtOfLineItem.size()==1) {
                                    transaction.setVatRate(transVat);
                                    if(Math.round(vatAndAmtOfLineItem.get(transVat))== Math.round(Math.abs(transaction.getAmount()))){
                                        if(!vatVsListOfTransactions.containsKey(transVat)) {
                                            listOfTransactions = new ArrayList<Transaction>();
                                        } else {
                                            listOfTransactions = vatVsListOfTransactions.get(transVat);
                                        }
                                        transaction.setTransactionType(IMConstants.NETT);
                                        listOfTransactions.add(transaction);
                                        vatVsListOfTransactions.put(transVat,listOfTransactions);
                                    }
                                }
                            }
                        }
                    }
                    else {

                        if(transaction.getTransactionCategory().toUpperCase().contains("KR;"))   {
                            float vatRate = Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
                            List<Transaction> list = vatVsListOfTransactions.get(vatRate);
                            if(list ==null || list.isEmpty())
                            {
                                list = new ArrayList<Transaction>();
                            }
                            list.add(transaction);
                            vatVsListOfTransactions.put(vatRate,list);
                        }
                        int vatRate = Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
                        transaction.setVatRate(vatRate);
                        listOfOtherTrans.add(transaction);
                    }
                }
                else {
                    float vatRate = Math.round(transaction.getVatAmount()/transaction.getAmount()*100);
                    List<Transaction> list = vatVsListOfTransactions.get(vatRate);
                    if(list ==null || list.isEmpty())
                    {
                        list = new ArrayList<Transaction>();
                    }
                    list.add(transaction);
                    vatVsListOfTransactions.put(vatRate,list);
                    transaction.setVatRate(vatRate);

                    listOfOtherTrans.add(transaction);
                    //transactionSummary.setSumOfBelop();

                }

            }
        }

        boolean isStromStartDate = true;
        boolean isNettStartDate = true;
        String startMonthYear = "";
        for(Attachment attachment:attachments) {
            if(attachment!=null) {
            Map<Float,Float> stromVatAndSum = new HashMap<Float,Float>();
            Map<Float,Float> nettVatAndSum = new HashMap<Float,Float>();
            Set<Float> vatSet = new HashSet<Float>();
            String stromStartMonthYear = null;
            String nettStartMonthYear = null;
            if((attachment.getDisplayStromData()!=null && attachment.getDisplayStromData()) || attachment.getDisplayStromData() == null)   {
                stromVatAndSum =  attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getMapOfVatSumOfGross();
                stromStartMonthYear = attachment.getStartMonthYear();
            }
            float sumOfNettBelop = 0.0f;
            //in case of only strom nettlie is null
            if(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie()!=null) {
                nettVatAndSum = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getMapOfVatSumOfGross();
                nettStartMonthYear = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getStartMonthAndYear();
            }
            vatSet.addAll(stromVatAndSum.keySet());
            vatSet.addAll(nettVatAndSum.keySet());
            float sumOfStrom = 0.0f;
            float sumOfNett = 0.0f;
            List<TransactionSummary> transactionSummaryList = new ArrayList<TransactionSummary>();
            float sumInklMVA = 0.0f;
            for(Float vat : vatSet)  {
                if(stromVatAndSum.containsKey(vat)) {
                    sumOfStrom =  stromVatAndSum.get(vat);
                }
                if(nettVatAndSum.containsKey(vat)) {
                    sumOfNett = nettVatAndSum.get(vat);
                }
                TransactionSummary attachmentSummary = new TransactionSummary();
                attachmentSummary.setMvaValue(vat);
                attachmentSummary.setSumOfNettStrom(sumOfStrom+sumOfNett);
                attachmentSummary.setSumOfBelop((sumOfStrom+sumOfNett)*(vat/100));
                sumInklMVA+= attachmentSummary.getSumOfNettStrom()+attachmentSummary.getSumOfBelop();
                if(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie()!=null) {
                    attachmentSummary.setTotalVatAmount(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getTotalVatAmount());
                }
                transactionSummaryList.add(attachmentSummary);
            }
            attachment.setTransactionSummary(transactionSummaryList);
            attachment.setSumInklMVA(sumInklMVA);
            if(stromStartMonthYear==null && nettStartMonthYear!=null ) {

                if(nettStartMonthYear.equals(startMonthYear) || (startMonthYear==null && isNettStartDate && isStromStartDate) ||(startMonthYear!=null && startMonthYear.equals(""))) {
                    startMonthYear = nettStartMonthYear;
                }else {
                    startMonthYear = null;
                }
                isStromStartDate = false;
                isNettStartDate = true;

            } else if(stromStartMonthYear!=null && nettStartMonthYear==null) {
                if(stromStartMonthYear.equals(startMonthYear) /*||  (startMonthYear==null && isNettStartDate && isStromStartDate)*/ || (startMonthYear!=null && startMonthYear.equals(""))) {
                    startMonthYear = stromStartMonthYear;
                } else {
                    startMonthYear = null;
                }
                isNettStartDate = false;
                isStromStartDate = true;
            }else if(stromStartMonthYear!=null && nettStartMonthYear!=null && stromStartMonthYear.equals(nettStartMonthYear) ) {
                if((startMonthYear!=null && stromStartMonthYear.equals(startMonthYear)) || startMonthYear==null)  {
                    startMonthYear = stromStartMonthYear;
                } else {
                    request.getStatement().setStatementPeriod("Strom og Nettlie");
                }
                isStromStartDate = true;
                isNettStartDate = true;
            } else if(stromStartMonthYear!=null && nettStartMonthYear!=null && !stromStartMonthYear.equals(nettStartMonthYear)) {
                isStromStartDate = false;
                isNettStartDate = false;
                startMonthYear = null;
            }
            }
        }

        if(!request.getStatement().isOneMeter())   {
            if(isNettStartDate && isStromStartDate && startMonthYear!=null)
            {
                request.getStatement().setStatementPeriod("Strom og Nettlie av " + startMonthYear);
            }else if((isNettStartDate || isStromStartDate) && startMonthYear !=null ) {
                if(isNettStartDate) {
                    request.getStatement().setStatementPeriod("Nettlie for " + startMonthYear);
                }
                if(isStromStartDate) {
                    request.getStatement().setStatementPeriod("Strom for " + startMonthYear);
                }
            } else {
                request.getStatement().setStatementPeriod("Strom og Nettlie");
            }
        }

        request.getStatement().getTransactionGroup().setTransaction(null);
        String stromName = null;
        String nettName = null;
        float sumOfTransAmount = 0.0f;
        List<TransactionSummary> listOfTranSummary = new ArrayList<TransactionSummary>();
        Map<String,Float> mapOfNameAndAmt = new HashMap<String,Float>();
        Map<String,Float>  mapOfNameAndVat = new HashMap<String,Float>();
        Set<Transaction> processedOtherTrans = new HashSet<Transaction>();
        for(float vatAmount:vatVsListOfTransactions.keySet()) {
            TransactionSummary transactionSummary = new TransactionSummary();
            float sumOfKraftTrans = 0.0f;
            float sumOfNettTrans = 0.0f;
            float sumOfOtherTrans = 0.0f;
            List<Transaction> listOfNettStrom = vatVsListOfTransactions.get(vatAmount);
            if(listOfNettStrom!=null && !listOfNettStrom.isEmpty())   {
                for(Transaction tran :listOfNettStrom)  {
                    if(IMConstants.KRAFT.equals(tran.getTransactionType())) {
                        stromName = tran.getTransactionCategory();
                        sumOfKraftTrans+=tran.getAmount()*IMConstants.NEGATIVE;
                        mapOfNameAndVat.put(stromName,vatAmount);
                        mapOfNameAndAmt.put(stromName,sumOfKraftTrans);
                    }else {
                        if(!listOfOtherTrans.contains(tran))   {
                            nettName = tran.getTransactionCategory();
                            sumOfNettTrans +=tran.getAmount()*IMConstants.NEGATIVE;
                            mapOfNameAndVat.put(nettName,vatAmount);
                            mapOfNameAndAmt.put(nettName,sumOfNettTrans);
                        }
                        else
                        {
                            tran.setAmount(tran.getAmount()*IMConstants.NEGATIVE);
                            processedOtherTrans.add(tran);
                            sumOfOtherTrans+=tran.getAmount()*IMConstants.NEGATIVE;
                        }
                    }
                }
                transactionSummary.setMvaValue(vatAmount);
                if(vatAmount!=0.0){
                    transactionSummary.setSumOfNettStrom(request.getStatement().getTotalVatAmount()*IMConstants.NEGATIVE);
                }
                transactionSummary.setSumOfBelop(sumOfKraftTrans+sumOfNettTrans+sumOfOtherTrans);
                sumOfTransAmount +=transactionSummary.getSumOfBelop();
                // transactionSummary.setSumOfNettStrom(transactionSummary.getSumOfNettStrom()+sumOfKraftTrans+sumOfNettTrans);
                listOfTranSummary.add(transactionSummary);
            }
        }
        request.getStatement().getTransactionGroup().setTransactionSummary(listOfTranSummary);
        request.getStatement().getTransactionGroup().setSumOfTransactions(sumOfTransAmount);
        List<Transaction> processedTransaction = new ArrayList();
        for(String transactionName:mapOfNameAndAmt.keySet())
        {
            Transaction newTransaction = new Transaction();
            // newTransaction.setTransactionType(IMConstants.KRAFT);
            newTransaction.setTransactionCategory(transactionName.substring(3));
            newTransaction.setAmount(mapOfNameAndAmt.get(transactionName));
            newTransaction.setVatRate(mapOfNameAndVat.get(transactionName));
            processedTransaction.add(newTransaction);
        }

        processedTransaction.addAll(processedOtherTrans);
        request.getStatement().getTransactionGroup().setTransaction(processedTransaction);
        }
    }

    private String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
}