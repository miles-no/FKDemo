package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.services.InvoiceGenerator;
import no.fjordkraft.im.services.PDFGenerator;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.task.PDFGeneratorTask;
import no.fjordkraft.im.util.IMConstants;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.io.File;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by miles on 5/12/2017.
 */
@Service
public class PDFGeneratorImpl implements PDFGenerator,ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(PDFGeneratorImpl.class);

    @Autowired
    private ConfigService configService;

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    IReportEngine reportEngine;

    @Autowired
    StatementService statementService;

    @Autowired
    @Qualifier("PDFGeneratorExecutor")
    TaskExecutor taskExecutor;

    @Autowired
    InvoiceGenerator invoiceGenerator;

    private String outputDirectoryPath;
    private String pdfGeneratedFolderName;
    private String xmlFolderName;

    private ApplicationContext applicationContext;

    private String birtRPTPath;

    public PDFGeneratorImpl(ConfigService configService) {
        this.configService = configService;
    }

    @PostConstruct
    public void initIt() throws Exception {
        outputDirectoryPath = configService.getString(IMConstants.BASE_DESTINATION_FOLDER_PATH);
        pdfGeneratedFolderName = configService.getString(IMConstants.GENERATED_PDF_FOLDER_NAME);
        xmlFolderName = configService.getString(IMConstants.PROCESSED_XML_FOLDER_NAME);
        birtRPTPath = configService.getString(IMConstants.BIRT_RPTDESIGN_PATH);
    }

    @Override
    @Transactional
    public void generateInvoicePDF() throws InterruptedException {
        Long numOfThreads = configService.getLong(IMConstants.NUM_OF_THREAD_PDFGENERATOR);
        List<Statement> statements = statementRepository.readStatements(numOfThreads, StatementStatusEnum.PRE_PROCESSED.getStatus());
        logger.debug("Generate invoice pdf for "+ statements.size() + " statements");
        for(Statement statement:statements) {
            statement.getSystemBatchInput().getFilename();
            statement.setStatus(StatementStatusEnum.PDF_PROCESSING.getStatus());
            statement.setUdateTime(new Timestamp(System.currentTimeMillis()));
            statementService.updateStatement(statement);
            PDFGeneratorTask pdfGeneratorTask = applicationContext.getBean(PDFGeneratorTask.class,statement);
            taskExecutor.execute(pdfGeneratorTask);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void generateInvoicePDFSingleStatement(Statement statement) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("PDF Generator for statement "+ statement.getId());
        try {

            String systemBatchInputFileName = "";
            String subFolderName = "";
            systemBatchInputFileName = statement.getSystemBatchInput().getFilename();
            subFolderName = systemBatchInputFileName.substring(0, systemBatchInputFileName.indexOf('.'));
            birtEnginePDFGenerator(statement.getId(), outputDirectoryPath, subFolderName, statement.getInvoiceNumber(), pdfGeneratedFolderName, xmlFolderName);
            statement.setStatus(StatementStatusEnum.PDF_PROCESSED.getStatus());
            statement.setUdateTime(new Timestamp(System.currentTimeMillis()));
            statementService.updateStatement(statement);
            invoiceGenerator.generateInvoice(statement);
        } catch (Exception e) {
            logger.debug("Exception in PDF Generator for statement"+ statement.getId(),e);
            statement.setStatus(StatementStatusEnum.PDF_PROCESSING_FAILED.getStatus());
            statement.setUdateTime(new Timestamp(System.currentTimeMillis()));
            statementService.updateStatement(statement);
        }
        stopWatch.stop();
        logger.debug("PDF Generator for statement completed statementId"+ statement.getId());
        logger.debug(stopWatch.prettyPrint());
    }

    public void birtEnginePDFGenerator(Long id, String outPutDirectoryPath, String statementFolderName, String invoiceNumber,
                                       String pdfGeneratedFolderName, String xmlFolderName) throws EngineException {
        logger.debug("Generating Invoice PDF for Statement ID: " + id);

        long startTime = System.currentTimeMillis();
        try {
            //String xmlFilePath = "D:\\XMLTOPDF\\new_pdf\\multipleAttachmentsWithChartData.xml";
            String basePath = outPutDirectoryPath + File.separator + statementFolderName + File.separator + invoiceNumber + File.separator ;
            String xmlFilePath =  basePath + xmlFolderName + File.separator + "statement.xml";
            //String reportDesignFilePath = "E:\\FuelKraft\\invoice_manager\\statementReport.rptdesign";
            String reportDesignFilePath = birtRPTPath + File.separator + "statementReport.rptdesign";

            String campaignFilePath = configService.getString(IMConstants.CAMPAIGN_FILE_PATH) ;
            IReportRunnable runnable = reportEngine.openReportDesign(reportDesignFilePath);
            IRunAndRenderTask task  = reportEngine.createRunAndRenderTask(runnable);
            task.setParameterValue("sourcexml", xmlFilePath);
            task.setParameterValue("campaignImage", campaignFilePath);
            //task.setParameterValue("imageurl", "file:///D:/XMLTOPDF/Screenshot_1.png");
            PDFRenderOption options = new PDFRenderOption();
            options.setEmbededFont(true);
            options.setOutputFormat("pdf");
            options.setOutputFileName(basePath + pdfGeneratedFolderName + File.separator + invoiceNumber + ".pdf");

            task.setRenderOption(options);
            task.run();

        } catch (BirtException e) {
            throw e;
        }

        //engine.destroy();
        //Platform.shutdown();
        //RegistryProviderFactory.releaseDefault();
        long endTime = System.currentTimeMillis();
        System.out.println("Time to execute report "+ (endTime-startTime)+ " milli seconds " + (endTime-startTime)/1000+ "  seconds ");

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
