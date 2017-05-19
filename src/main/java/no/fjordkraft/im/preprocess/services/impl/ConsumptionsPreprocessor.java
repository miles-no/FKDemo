package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by miles on 5/18/2017.
 */
@Service
@PreprocessorInfo(order=4)
public class ConsumptionsPreprocessor extends BasePreprocessor{

    private static final Logger logger = LoggerFactory.getLogger(ConsumptionsPreprocessor.class);

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) throws IOException {

        Attachments attachments = request.getStatement().getAttachments();
        List<Attachment> processed = new ArrayList<Attachment>();

        Iterator attachmentIterator = attachments.getAttachment().listIterator();

        while(attachmentIterator.hasNext()) {
            Attachment singleAttachmentData = (Attachment) attachmentIterator.next();

            ConsumptionPillars132 consumptionPillars132 = singleAttachmentData.getFAKTURA().getVEDLEGGEMUXML().getInvoice()
                    .getInvoiceOrder().getConsumptionPillars132();
            Consumptions consumptionData = new Consumptions();
            List<Consumption> consumptions = new ArrayList<Consumption>(12);
            Consumption consumption = new Consumption();

            consumption.setConsumptionSequence(1);
            consumption.setPeriodDescription(consumptionPillars132.getPeriodDescription1());
            consumption.setLastYearConsumption(consumptionPillars132.getLastYearConsumption1());
            consumption.setThisYearConsumption(consumptionPillars132.getThisYearConsumption1());
            consumptions.add(consumption);

            consumption = new Consumption();
            consumption.setConsumptionSequence(2);
            consumption.setPeriodDescription(consumptionPillars132.getPeriodDescription2());
            consumption.setLastYearConsumption(consumptionPillars132.getLastYearConsumption2());
            consumption.setThisYearConsumption(consumptionPillars132.getThisYearConsumption2());
            consumptions.add(consumption);

            consumption = new Consumption();
            consumption.setConsumptionSequence(3);
            consumption.setPeriodDescription(consumptionPillars132.getPeriodDescription3());
            consumption.setLastYearConsumption(consumptionPillars132.getLastYearConsumption3());
            consumption.setThisYearConsumption(consumptionPillars132.getThisYearConsumption3());
            consumptions.add(consumption);

            consumption = new Consumption();
            consumption.setConsumptionSequence(4);
            consumption.setPeriodDescription(consumptionPillars132.getPeriodDescription4());
            consumption.setLastYearConsumption(consumptionPillars132.getLastYearConsumption4());
            consumption.setThisYearConsumption(consumptionPillars132.getThisYearConsumption4());
            consumptions.add(consumption);

            consumption = new Consumption();
            consumption.setConsumptionSequence(5);
            consumption.setPeriodDescription(consumptionPillars132.getPeriodDescription5());
            consumption.setLastYearConsumption(consumptionPillars132.getLastYearConsumption5());
            consumption.setThisYearConsumption(consumptionPillars132.getThisYearConsumption5());
            consumptions.add(consumption);

            consumption = new Consumption();
            consumption.setConsumptionSequence(6);
            consumption.setPeriodDescription(consumptionPillars132.getPeriodDescription6());
            consumption.setLastYearConsumption(consumptionPillars132.getLastYearConsumption6());
            consumption.setThisYearConsumption(consumptionPillars132.getThisYearConsumption6());
            consumptions.add(consumption);

            consumption = new Consumption();
            consumption.setConsumptionSequence(7);
            consumption.setPeriodDescription(consumptionPillars132.getPeriodDescription7());
            consumption.setLastYearConsumption(consumptionPillars132.getLastYearConsumption7());
            consumption.setThisYearConsumption(consumptionPillars132.getThisYearConsumption7());
            consumptions.add(consumption);

            consumption = new Consumption();
            consumption.setConsumptionSequence(8);
            consumption.setPeriodDescription(consumptionPillars132.getPeriodDescription8());
            consumption.setLastYearConsumption(consumptionPillars132.getLastYearConsumption8());
            consumption.setThisYearConsumption(consumptionPillars132.getThisYearConsumption8());
            consumptions.add(consumption);

            consumption = new Consumption();
            consumption.setConsumptionSequence(9);
            consumption.setPeriodDescription(consumptionPillars132.getPeriodDescription9());
            consumption.setLastYearConsumption(consumptionPillars132.getLastYearConsumption9());
            consumption.setThisYearConsumption(consumptionPillars132.getThisYearConsumption9());
            consumptions.add(consumption);

            consumption = new Consumption();
            consumption.setConsumptionSequence(10);
            consumption.setPeriodDescription(consumptionPillars132.getPeriodDescription10());
            consumption.setLastYearConsumption(consumptionPillars132.getLastYearConsumption10());
            consumption.setThisYearConsumption(consumptionPillars132.getThisYearConsumption10());
            consumptions.add(consumption);

            consumption = new Consumption();
            consumption.setConsumptionSequence(11);
            consumption.setPeriodDescription(consumptionPillars132.getPeriodDescription11());
            consumption.setLastYearConsumption(consumptionPillars132.getLastYearConsumption11());
            consumption.setThisYearConsumption(consumptionPillars132.getThisYearConsumption11());
            consumptions.add(consumption);

            consumption = new Consumption();
            consumption.setConsumptionSequence(12);
            consumption.setPeriodDescription(consumptionPillars132.getPeriodDescription12());
            consumption.setLastYearConsumption(consumptionPillars132.getLastYearConsumption12());
            consumption.setThisYearConsumption(consumptionPillars132.getThisYearConsumption12());
            consumptions.add(consumption);

            consumptionData.setConsumptionList(consumptions);

            singleAttachmentData.getFAKTURA().getVEDLEGGEMUXML().getInvoice()
                    .getInvoiceOrder().setConsumptions(consumptionData);
            singleAttachmentData.getFAKTURA().getVEDLEGGEMUXML().getInvoice()
                    .getInvoiceOrder().setConsumptionPillars132(null);

            processed.add(singleAttachmentData);

        }
        attachments.setAttachment(processed);
        request.getStatement().setAttachments(attachments);

    }
}

