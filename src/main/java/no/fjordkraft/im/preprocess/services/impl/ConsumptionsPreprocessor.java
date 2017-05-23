package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miles on 5/18/2017.
 */
@Service
@PreprocessorInfo(order=4)
public class ConsumptionsPreprocessor extends BasePreprocessor{

    private static final Logger logger = LoggerFactory.getLogger(ConsumptionsPreprocessor.class);

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {

        Attachments attachments = request.getStatement().getAttachments();

        if(null != attachments && null != attachments.getAttachment()) {
            for(Attachment singleAttachmentData: attachments.getAttachment()){
                if (null != singleAttachmentData && null != singleAttachmentData.getFAKTURA() && null != singleAttachmentData.getFAKTURA().getVEDLEGGEMUXML()) {

                    ConsumptionPillars132 consumptionPillars132 = singleAttachmentData.getFAKTURA().getVEDLEGGEMUXML().getInvoice()
                            .getInvoiceOrder().getConsumptionPillars132();
                    if(null != consumptionPillars132) {
                        Consumptions consumptionData = new Consumptions();
                        List<Consumption> consumptions = new ArrayList<Consumption>(12);
                        for(int i=1; i<=12 ; i++) {
                            Consumption consumption = new Consumption();
                            try {
                                consumption.setConsumptionSequence(i);

                                Method periodDescriptionMethod = ConsumptionPillars132.class.getMethod("getPeriodDescription" + i);
                                consumption.setPeriodDescription(periodDescriptionMethod.invoke(consumptionPillars132).toString());

                                Method lastYearConsumptionMethod = ConsumptionPillars132.class.getMethod("getLastYearConsumption" + i);
                                Object lastYearConsumption = lastYearConsumptionMethod.invoke(consumptionPillars132);
                                consumption.setLastYearConsumption(null != lastYearConsumption ? Float.valueOf(lastYearConsumption.toString()) : 0.0f);

                                Method thisYearConsumptionMethod = ConsumptionPillars132.class.getMethod("getThisYearConsumption" + i);
                                Object thisYearConsumption = thisYearConsumptionMethod.invoke(consumptionPillars132);
                                consumption.setThisYearConsumption(null != thisYearConsumption ? Float.valueOf(thisYearConsumption.toString()) : 0.0f);
                                consumptions.add(consumption);

                            } catch (Exception e) {
                                logger.debug("Exception in consumption preprocessor",e);
                                throw new PreprocessorException(e);
                            }
                        }
                        consumptionData.setConsumptionList(consumptions);
                        singleAttachmentData.getFAKTURA().getVEDLEGGEMUXML().getInvoice()
                                .getInvoiceOrder().setConsumptions(consumptionData);

                    }
                }
            }
        }

    }
}

