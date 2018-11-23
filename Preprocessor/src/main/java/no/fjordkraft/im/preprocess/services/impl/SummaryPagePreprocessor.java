package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.AuditLogService;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 8/23/18
 * Time: 6:19 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
@PreprocessorInfo(order=23)
public class SummaryPagePreprocessor extends BasePreprocessor  {
    private static final Logger logger = LoggerFactory.getLogger(SummaryPagePreprocessor.class);

    @Autowired
    AuditLogService auditLogService;

    @Autowired
    StatementService statementService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request)
    {
        try
        {
            String brand =  request.getEntity().getSystemBatchInput().getBrand();
            if(request.getStatement().getLegalPartClass().equals(IMConstants.LEGAL_PART_CLASS_ORGANIZATION) && ("TKAS".equals(brand) || "FKAS".equals(brand)))
            {
                logger.info("In Save Summary Page Preprocessor");
                Statement stmt = request.getStatement();
                List<Attachment> attachmentList = stmt.getAttachments().getAttachment();
                MeterSummaryGroup meterSummaryGroup = new MeterSummaryGroup();
                List<MeterSummary> listOfMeterSummary = new ArrayList<MeterSummary>();
                if(attachmentList!=null && attachmentList.size()>=configService.getInteger(IMConstants.SUMMARY_PAGE_WITH_GREATER_OR_EQUAL_METER))
                {
                    stmt.setShowMeterSummary(true);
                    Double sumOfStrom = 0.0;
                    Double sumOfNett = 0.0;
                    Map<Double,Double> mapOfMvaVsBelop = new HashMap<Double, Double>();
                    int meterCounter = 0;
                    logger.info("No Of attachments " + attachmentList.size());
                    for(Attachment attachment:attachmentList)
                    {
                        logger.info("Creating all meter summary page.");
                        Double meterSumNettStrom = 0.0;
                        List<MeterDetails>  listOfMeterDetails = new ArrayList<MeterDetails>();

                        MeterSummary meterSummary = new MeterSummary();
                        meterSummary.setAddress(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getSupplyPointInfo117().getStreetNo());
                        meterSummary.setMålepunktID(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getSupplyPointInfo117().getObjectId());
                        meterSummary.setMålernummer(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getSupplyPointInfo117().getMeterId());
                        meterSummary.setReference(attachment.getFAKTURA().getFAKTURAMERKESO());
                        meterSummary.setSequenceNumber(++meterCounter);

                        //If Attachment is Strom create a metere Detail for Strom with same meter.Belop will be sum of all strom with same meter.
                        if(attachment.getDisplayStromData()!=null && attachment.getDisplayStromData().booleanValue())
                        {
                            logger.debug("Found Strom attachment.. ");


                            List<InvoiceOrder> listOfInvoiceOrder = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceOrder();
                            logger.debug("No Of Strom for meter " + meterSummary.getMålepunktID() + " is " + listOfInvoiceOrder.size());

                            MeterDetails stromMeterDetail = getMeterDetailOfStrom(listOfInvoiceOrder,mapOfMvaVsBelop);
                            if("TKAS".equals(brand)) {
                            stromMeterDetail.setMeterName(attachment.getTransactionName()+ " fra Trøndelag Kraft");
                            }else {
                                stromMeterDetail.setMeterName(attachment.getTransactionName());
                            }
                            stromMeterDetail.setStartDate(attachment.getStartDate());
                            stromMeterDetail.setEndDate(attachment.getEndDate());

                            sumOfStrom+=stromMeterDetail.getBeløp();
                            meterSumNettStrom += stromMeterDetail.getBeløp();
                            listOfMeterDetails.add(stromMeterDetail);
                        }
                        //If attachment is Grid create a meter Detail for Nett with same meter. Belop will be sum of all nettleie with same meter.
                        if(attachment.isOnlyGrid() || (attachment.getFAKTURA().getNettleieList()!=null && attachment.getFAKTURA().getNettleieList().size()>0))
                        {
                            logger.debug("Found nettleie attachment..");
                            List<Nettleie> listOfNett =  attachment.getFAKTURA().getNettleieList();


                            MeterDetails nettMeterDetails = getMeterDetailOfNettleie(listOfNett,mapOfMvaVsBelop);
                            if(listOfNett.size()>0 && listOfNett.get(0).getGrid().getName()==null ||
                                    (listOfNett.get(0).getGrid().getName()!=null &&
                                            listOfNett.get(0).getGrid().getName().isEmpty()))
                            {
                                nettMeterDetails.setMeterName("Nettleie fra netteier");
                            } else if(listOfNett.size()>0)
                            {
                                //If grid name is blank than in that case it should be "nettleie fra netteier".
                                nettMeterDetails.setMeterName("Nettleie fra "+listOfNett.get(0).getGrid().getName());
                            }
                            nettMeterDetails.setStartDate(attachment.getFAKTURA().getNettleieList().get(0).getStartDate());
                            nettMeterDetails.setEndDate(attachment.getFAKTURA().getNettleieList().get(0).getEndDate());
                            sumOfNett+=nettMeterDetails.getBeløp();
                            meterSumNettStrom += nettMeterDetails.getBeløp();
                            listOfMeterDetails.add(nettMeterDetails);
                        }

                        logger.debug("list of meterdetails " + listOfMeterDetails.size() + " for meter .. " + meterSummary.getMålepunktID());
                        meterSummary.setMeterNettStrom(meterSumNettStrom);
                        meterSummary.setMeterDetails(listOfMeterDetails);
                        listOfMeterSummary.add(meterSummary);
                    }
                    logger.debug("Set list of meter summary in meter group " + listOfMeterSummary.size());
                    meterSummaryGroup.setMeterSummary(listOfMeterSummary);
                    meterSummaryGroup.setAllMeterStromSum(sumOfStrom);
                    meterSummaryGroup.setAllMeterNettSum(sumOfNett);
                    meterSummaryGroup.setAllMeterSum(sumOfStrom+sumOfNett);
                    Double sumInklMVA = 0.0;
                    List<TransactionSummary> listOfTransactionSummary =  new ArrayList<TransactionSummary>();
                    for(Double mva :mapOfMvaVsBelop.keySet())
                    {
                        TransactionSummary transactionSummary = new TransactionSummary();
                        transactionSummary.setMvaValue(mva);
                        transactionSummary.setSumOfNettStrom(mapOfMvaVsBelop.get(mva));
                        transactionSummary.setSumOfBelop((mapOfMvaVsBelop.get(mva)*mva)/100);
                        listOfTransactionSummary.add(transactionSummary);
                        sumInklMVA+= transactionSummary.getSumOfNettStrom()+transactionSummary.getSumOfBelop();
                    }
                    meterSummaryGroup.setTransactionSummary(listOfTransactionSummary);
                    meterSummaryGroup.setSumInclMva(sumInklMVA);
                    request.getStatement().setMeterSummaryGroup(meterSummaryGroup);
                }
                else
                {
                    logger.debug("No Need to show Summary page");
                    stmt.setShowMeterSummary(false);
                }
                request.getEntity().setNoOfMeter(attachmentList.size());
               // statementService.updateStatement(request.getEntity());
            }
        }catch (Exception e)
        {
            String message = "Error While processing Summary page for Organization.";
            logger.debug("Exception in attachment preprocessor", e);
            auditLogService.saveAuditLog(request.getEntity().getId(), StatementStatusEnum.PRE_PROCESSING_FAILED.getStatus(),message,IMConstants.INFO,request.getEntity().getLegalPartClass());
            throw new PreprocessorException(e);
        }
    }

    private MeterDetails getMeterDetailOfNettleie(List<Nettleie> listOfNett, Map<Double, Double> mapOfMvaVsBelop)
    {
        Double meterNettAmt= 0.0;
        for(Nettleie nett : listOfNett)
        {
            logger.debug("Looping for Nettleie " + nett.getGridName());
            meterNettAmt+=nett.getSumOfNettAmount();
            Set<Double> mvaValues = nett.getMapOfVatSumOfGross().keySet();
            for(Double mva:mvaValues)
            {
                Double sumOfBelop = (Double)nett.getMapOfVatSumOfGross().get(mva);
                if(mapOfMvaVsBelop.containsKey(mva))
                {
                    mapOfMvaVsBelop.put(mva,mapOfMvaVsBelop.get(mva)+sumOfBelop);
                }
                else
                {
                    mapOfMvaVsBelop.put(mva,sumOfBelop);
                }

            }
        }
        MeterDetails meterDetails = new MeterDetails();
        //meterDetails.setForbruk(attachment.getFAKTURA().getNettleieList().get(0).getSumOfNettAmount());
       // meterDetails.setMeterName("Nettleie fra " +nett.getGridName());
       // meterDetails.setStartDate(attachment.getFAKTURA().getNettleieList().get(0).getStartDate());
       // meterDetails.setEndDate(attachment.getFAKTURA().getNettleieList().get(0).getEndDate());
        meterDetails.setBeløp(meterNettAmt);
       //
        return meterDetails;  //To change body of created methods use File | Settings | File Templates.
    }

    private MeterDetails getMeterDetailOfStrom(List<InvoiceOrder> listOfInvoiceOrder,Map<Double,Double> mapOfMvaVsBelop)
    {
        Double methodFinalCReading = 0.0;
        Double netTotal = 0.0;

        for(InvoiceOrder invoiceOrder:listOfInvoiceOrder)
        {
            logger.debug("looping for invoice order " + invoiceOrder.getInvoiceNo());
            //IM-140 : if there is any berenet invoice line then methodFinalCReading should be forbruk
            if(invoiceOrder.isBeregnetInvoiceLine())
            {
                methodFinalCReading = 0.0;
            } else
            {
                if(invoiceOrder.getReadingInfo111()!=null && invoiceOrder.getReadingInfo111().size()>0)
                {
                    logger.info("Found Reading info for Strom " + invoiceOrder.getInvoiceNo());
                    List<ReadingInfo111> listOfCReading =  invoiceOrder.getReadingInfo111();
                    for(ReadingInfo111 readingInfo111:listOfCReading)
                    {
                        methodFinalCReading+=  readingInfo111.getMethodFinalCReading();
                    }
                }
            }
            logger.debug("Nett Total " + invoiceOrder.getInvoiceOrderAmounts113().getNetTotal());
            netTotal+= invoiceOrder.getInvoiceOrderAmounts113().getNetTotal();
            Set<Double> mvaValues = invoiceOrder.getMapOfVatSumOfGross().keySet();
            for(Double mva:mvaValues)
            {
                Double sumOfBelop = (Double) invoiceOrder.getMapOfVatSumOfGross().get(mva);
                if(mapOfMvaVsBelop.containsKey(mva))
                {
                    mapOfMvaVsBelop.put(mva,mapOfMvaVsBelop.get(mva)+sumOfBelop);
                }
                else
                {
                    mapOfMvaVsBelop.put(mva,sumOfBelop);
                }
            }
        }
        MeterDetails meterDetails = new MeterDetails();
        meterDetails.setForbruk(methodFinalCReading);
        meterDetails.setBeløp(netTotal);
        return meterDetails;
    }
}
