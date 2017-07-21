package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.exceptions.PDFGeneratorException;
import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.services.InvoiceGenerator;
import no.fjordkraft.im.services.PDFGenerator;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.task.PDFGeneratorTask;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.io.FileUtils;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
    SegmentFileServiceImpl segmentFileService;

    @Autowired
    IReportEngine reportEngine;

    @Autowired
    StatementService statementService;

    @Autowired
    LayoutServiceImpl layoutDesignService;

    @Autowired
    LayoutContentServiceImpl layoutContentService;

    @Autowired
    @Qualifier("PDFGeneratorExecutor")
    TaskExecutor taskExecutor;

    @Autowired
    InvoiceGenerator invoiceGenerator;

    @Autowired
    AuditLogServiceImpl auditLogService;

    private String outputDirectoryPath;
    private String pdfGeneratedFolderName;
    private String xmlFolderName;

    private ApplicationContext applicationContext;

    private String birtRPTPath;
    private String birtResourcePath;
    private String sampleStatementFilePath;
    private String sampleCampaignImagePath;

    public PDFGeneratorImpl(ConfigService configService) {
        this.configService = configService;
    }

    @PostConstruct
    public void initIt() throws Exception {
        outputDirectoryPath = configService.getString(IMConstants.BASE_DESTINATION_FOLDER_PATH);
        pdfGeneratedFolderName = configService.getString(IMConstants.GENERATED_PDF_FOLDER_NAME);
        xmlFolderName = configService.getString(IMConstants.PROCESSED_XML_FOLDER_NAME);
        birtRPTPath = configService.getString(IMConstants.BIRT_RPTDESIGN_PATH);
        birtResourcePath = configService.getString(IMConstants.BIRT_RESOURCE_PATH);
        sampleStatementFilePath = configService.getString(IMConstants.SAMPLE_STATEMENT_FILE_PATH);
        sampleCampaignImagePath = configService.getString(IMConstants.SAMPLE_CAMPAIGN_IMAGE_PATH);
    }

    @Override
    @Transactional
    public void generateInvoicePDF() throws InterruptedException {
        Long numOfThreads = configService.getLong(IMConstants.NUM_OF_THREAD_PDFGENERATOR);

        if(taskExecutor instanceof ThreadPoolTaskExecutor) {
            ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor)taskExecutor;
            logger.debug("PDF generator thread queue count " + executor.getThreadPoolExecutor().getQueue().size() +" active threads "+ executor.getActiveCount() + "max pool size "+executor.getMaxPoolSize()+ " :: "+executor.getThreadPoolExecutor().getActiveCount());
        }

        List<Statement> statements = statementRepository.readStatements(numOfThreads, StatementStatusEnum.PRE_PROCESSED.getStatus());
        logger.debug("Generate invoice pdf for "+ statements.size() + " statements");

        for(Statement statement:statements) {
            logger.debug("PDF generation will start for statement with id "+ statement.getId());
            statementService.updateStatement(statement,StatementStatusEnum.PDF_PROCESSING);
            statement.getSystemBatchInput().getTransferFile().getFilename();
            PDFGeneratorTask pdfGeneratorTask = applicationContext.getBean(PDFGeneratorTask.class,statement);
            taskExecutor.execute(pdfGeneratorTask);
        }
    }

    @Override
    @Transactional()
    public void generateInvoicePDFSingleStatement(Statement statement) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("PDF generation for statement "+ statement.getId());
        try {
            //statementService.updateStatement(statement,StatementStatusEnum.PDF_PROCESSING);
            String systemBatchInputFileName = "";
            String subFolderName = "";
            systemBatchInputFileName = statement.getSystemBatchInput().getTransferFile().getFilename();
            subFolderName = systemBatchInputFileName.substring(0, systemBatchInputFileName.indexOf('.'));
            birtEnginePDFGenerator(statement, outputDirectoryPath, subFolderName, pdfGeneratedFolderName, xmlFolderName);
            statementService.updateStatement(statement, StatementStatusEnum.PDF_PROCESSED);
            invoiceGenerator.generateInvoice(statement);
        } catch (PDFGeneratorException e) {
            logger.error("Exception in PDF generation for statement" + statement.getId(), e);
            statementService.updateStatement(statement, StatementStatusEnum.PDF_PROCESSING_FAILED);
            auditLogService.saveAuditLog(statement.getId(), StatementStatusEnum.PDF_PROCESSING.getStatus(), getCause(e).getMessage(), IMConstants.ERROR);
        } catch (Exception e) {
            logger.error("Exception in PDF generation for statement" + statement.getId(), e);
            statementService.updateStatement(statement, StatementStatusEnum.PDF_PROCESSING_FAILED);
        }
        stopWatch.stop();
        logger.debug("PDF generated for statement with statementId"+ statement.getId()+ "completed");
        logger.debug(stopWatch.prettyPrint());
    }

    private Throwable getCause(Throwable e) {
        Throwable cause = null;
        Throwable result = e;

        while(null != (cause = result.getCause())  && (result != cause) ) {
            result = cause;
        }
        return result;
    }

    public void birtEnginePDFGenerator(Statement statement, String outPutDirectoryPath, String statementFolderName,
                                       String pdfGeneratedFolderName, String xmlFolderName) throws BirtException {
        logger.debug("Generating Invoice PDF for Statement ID: " + statement.getId());

        long startTime = System.currentTimeMillis();
        String accountNo = statement.getAccountNumber();
        String brand = statement.getSystemBatchInput().getTransferFile().getBrand();
        try {
            /*EngineConfig engineConfig = new EngineConfig();\
            engineConfig.setResourcePath(birtResourcePath);
            Platform.startup(engineConfig);
            IReportEngineFactory factory = (IReportEngineFactory) Platform
                    .createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
            reportEngine = factory.createReportEngine(engineConfig);*/

            String basePath = outPutDirectoryPath + File.separator + statementFolderName + File.separator + statement.getInvoiceNumber() + File.separator ;
            String xmlFilePath =  basePath + xmlFolderName + File.separator + "statement.xml";
            String reportDesignFilePath = birtRPTPath + File.separator + "statementReport.rptdesign";

            String campaignImage = segmentFileService.getImageContent(accountNo, brand);
            if(null == campaignImage) {
                auditLogService.saveAuditLog(IMConstants.CAMPAIGN_IMAGE, statement.getId(), StatementStatusEnum.PDF_PROCESSING.getStatus(),
                        "Campaign Image not found", IMConstants.WARNING);
            }
            IReportRunnable runnable = null;

            Boolean readFromFile =  configService.getBoolean("read.layout.from.file");
            String encoding = configService.getString("layout.encoding");
            encoding = encoding == null ? "UTF-8":encoding;
            logger.debug("read layout from filesystem "+readFromFile+" encoding "+ encoding);
            if(readFromFile ) {
                runnable = reportEngine.openReportDesign(reportDesignFilePath);
            } else {
                String rptDesign = layoutDesignService.getRptDesignFile(statement.getLayoutID());
                if(null != rptDesign) {
                    logger.debug(" layout is "+ statement.getLayoutID());
                } else {
                    throw new PDFGeneratorException("Layout not found");
                }
                InputStream designStream = new ByteArrayInputStream(rptDesign.getBytes(Charset.forName(encoding)));
                runnable = reportEngine.openReportDesign(designStream);
            }

            IRunAndRenderTask task  = reportEngine.createRunAndRenderTask(runnable);
            task.setParameterValue("sourcexml", xmlFilePath);
            task.setParameterValue("campaignImage", campaignImage);
            PDFRenderOption options = new PDFRenderOption();
            options.setEmbededFont(true);
            options.setOutputFormat("pdf");
            options.setOutputFileName(basePath + pdfGeneratedFolderName + File.separator + statement.getInvoiceNumber() + ".pdf");

            task.setRenderOption(options);
            task.run();

        } catch (BirtException e) {
            throw new PDFGeneratorException(e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        logger.debug("Time to execute report " + (endTime - startTime) + " milli seconds " + (endTime - startTime) / 1000 + "  seconds ");

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public byte[] generatePreview(Long layoutId, Integer version) throws IOException, BirtException {
        String ouputFilePath = "src/main/resources/preview.pdf";
        try {
            String rptDesign = layoutContentService.getLayoutContentByLayoutIdandVersion(layoutId, version);
            String encoding = configService.getString("layout.encoding");
            encoding = encoding == null ? "UTF-8":encoding;
            InputStream designStream = new ByteArrayInputStream(rptDesign.getBytes(Charset.forName(encoding)));
            String campaignImage = segmentFileService.getCampaignForPreview(sampleCampaignImagePath);
            IReportRunnable runnable = reportEngine.openReportDesign(designStream);

            IRunAndRenderTask task = reportEngine.createRunAndRenderTask(runnable);
            task.setParameterValue("sourcexml", sampleStatementFilePath);
            task.setParameterValue("campaignImage", campaignImage);
            PDFRenderOption options = new PDFRenderOption();
            options.setEmbededFont(true);
            options.setOutputFormat("pdf");
            options.setOutputFileName(ouputFilePath);
            task.setRenderOption(options);
            task.run();
        } catch (Exception e) {
            throw new RuntimeException("Can not generate preview !");
        }

        File file = new File(ouputFilePath);
        return FileUtils.readFileToByteArray(file);
    }
}
