package no.fjordkraft.im.services.impl;

import com.itextpdf.text.FontFactory;
import no.fjordkraft.im.controller.IMController;
import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.repository.SystemConfigRepository;
import no.fjordkraft.im.services.PDFGenerator;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.*;
import org.eclipse.core.internal.registry.RegistryProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;

/**
 * Created by miles on 5/12/2017.
 */
@Service
public class PDFGeneratorImpl implements PDFGenerator {

    private static final Logger logger = LoggerFactory.getLogger(PDFGeneratorImpl.class);

    @Autowired
    SystemConfigRepository systemConfigRepository;

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    IMController imController;

    @Override
    @Transactional
    public void generateInvoicePDF() throws InterruptedException {
        String outputDirectoryPath = systemConfigRepository.getConfigValue(IMConstants.DESTINATION_PATH);
        String pdfGeneratedFolderName = systemConfigRepository.getConfigValue(IMConstants.PDF_GENERATED_FOLDER_NAME);
        String xmlFolderName = systemConfigRepository.getConfigValue(IMConstants.PROCESSED_XML_FOLDER_NAME);
        String systemBatchInputFileName = "";
        String subFolderName = "";

        List<Statement> statements = statementRepository.readStatements(StatementStatusEnum.PRE_PROCESSED.getStatus());
        Iterator statementIterator = statements.listIterator();
        while(statementIterator.hasNext()){
            Statement statement = (Statement) statementIterator.next();
            systemBatchInputFileName = statement.getSystemBatchInput().getFilename();
            updateStatementStatusInDB(StatementStatusEnum.PDF_PROCESSING.getStatus(), statement.getId());
            subFolderName = systemBatchInputFileName.substring(0, systemBatchInputFileName.indexOf('.'));
            birtEnginePDFGenerator(statement.getId(), outputDirectoryPath, subFolderName, statement.getInvoiceNumber(), pdfGeneratedFolderName, xmlFolderName);
            updateStatementStatusInDB(StatementStatusEnum.PDF_PROCESSED.getStatus(), statement.getId());
        }
    }

    private void updateStatementStatusInDB(String status, Long id) {
        Statement statement = statementRepository.findOne(id);
        statement.setStatus(status);
        statementRepository.save(statement);
    }

    public void birtEnginePDFGenerator(Long id, String outPutDirectoryPath, String statementFolderName, String invoiceNumber,
                                       String pdfGeneratedFolderName, String xmlFolderName) {
        logger.debug("Generating Invoice PDF for Statement ID: " + id);
        IReportEngine engine = null;
        CountDownLatch latch = new CountDownLatch(10);
        long startTime = System.currentTimeMillis();
        try {
            FontFactory.register("D:\\XMLTOPDF\\CSS\\TrueType_FjorkraftNeoSans\\FjordkraftNeoSan.ttf", "Fjordkraft Neo Sans");
            FontFactory.register("D:\\XMLTOPDF\\CSS\\TrueType_FjorkraftNeoSans\\FjordkraftNeoSanMed.ttf", "Fjordkraft Neo Sans Medium");
            FontFactory.register("D:\\XMLTOPDF\\CSS\\TrueType_FjorkraftNeoSans\\FjordNeoSanLigIta.ttf", "Fjordkraft Neo Sans Lt It");
            FontFactory.register("D:\\XMLTOPDF\\CSS\\TrueType_FjorkraftNeoSans\\FjordkraftNeoSanBol.ttf", "Fjordkraft Neo Sans Bd");
            FontFactory.register("D:\\XMLTOPDF\\CSS\\TrueType_FjorkraftNeoSans\\FjordNeoSanBolIta.ttf", "Fjordkraft Neo Sans Bd It");
            FontFactory.register("D:\\XMLTOPDF\\CSS\\TrueType_FjorkraftNeoSans\\FjordNeoSanMedIta.ttf", "Fjordkraft Neo Sans Med It");
            FontFactory.register("D:\\XMLTOPDF\\CSS\\TrueType_FjorkraftNeoSans\\FjordNeoSanIta.ttf", "Fjordkraft Neo Sans It");
            FontFactory.register("D:\\XMLTOPDF\\CSS\\TrueType_FjorkraftNeoSans\\FjordkraftNeoSanLig.ttf ", "Fjordkraft Neo Sans Lt");
            EngineConfig config = new EngineConfig();

            config.setEngineHome(IMConstants.BIRT_ENGINE_HOME_PATH);
            config.setLogConfig(IMConstants.BIRT_ENGINE_LOG_PATH, Level.FINE);

            Platform.startup(config);
            IReportEngineFactory factory = (IReportEngineFactory) Platform
                    .createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
            engine = factory.createReportEngine( config );
            engine.changeLogLevel( Level.WARNING );
            //String xmlFilePath = "D:\\XMLTOPDF\\new_pdf\\multipleAttachmentsWithChartData.xml";
            String xmlFilePath = outPutDirectoryPath + File.separator + statementFolderName + File.separator + invoiceNumber
                    + File.separator + xmlFolderName + File.separator + "statement.xml";
            String reportDesignFilePath = "D:\\XMLTOPDF\\eclipse\\workspace\\fjordkraft_sample1\\statementReport.rptdesign";

            IReportRunnable runnable = engine.openReportDesign(reportDesignFilePath);
            IRunAndRenderTask task  = engine.createRunAndRenderTask(runnable);
            task.setParameterValue("sourcexml", xmlFilePath);
            //task.setParameterValue("imageurl", "file:///D:/XMLTOPDF/Screenshot_1.png");
            PDFRenderOption options = new PDFRenderOption();
            options.setFontDirectory("C:\\Windows\\Fonts");
            options.setEmbededFont(true);
            options.setOutputFormat("pdf");
            options.setOutputFileName(outPutDirectoryPath + File.separator + statementFolderName + File.separator
                    + invoiceNumber + File.separator + pdfGeneratedFolderName + File.separator + invoiceNumber + ".pdf");

            task.setRenderOption(options);
            task.run();

        } catch (BirtException e) {
            updateStatementStatusInDB(StatementStatusEnum.PDF_PROCESSING_FAILED.getStatus(), id);
            e.printStackTrace();
        }

        engine.destroy();
        Platform.shutdown();
        RegistryProviderFactory.releaseDefault();
        long endTime = System.currentTimeMillis();
        System.out.println("Time to execute report "+ (endTime-startTime)+ " milli seconds " + (endTime-startTime)/1000+ "  seconds ");

    }
}
