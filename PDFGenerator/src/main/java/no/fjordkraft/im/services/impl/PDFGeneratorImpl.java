package no.fjordkraft.im.services.impl;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import no.fjordkraft.im.exceptions.PDFGeneratorException;
import no.fjordkraft.im.model.Attachment;
import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.services.*;
import no.fjordkraft.im.statusEnum.AttachmentTypeEnum;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.task.PDFGeneratorTask;
import no.fjordkraft.im.util.IMConstants;
import no.fjordkraft.im.util.SetInvoiceASOnline;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
//import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.Charset;
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
    SegmentFileService segmentFileService;

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

    @Autowired
    AttachmentConfigService attachmentConfigService;

    private String outputDirectoryPath;
    private String pdfGeneratedFolderName;
    private String xmlFolderName;

    private ApplicationContext applicationContext;

    private String birtRPTPath;
    private String birtResourcePath;
    private String sampleStatementFilePath;
    private String sampleCampaignImagePath;
    private String customPdfFontPath;
    private String basePathCampaign;
    private boolean readCampaignFilesystem;
    //private int attachmentConfigId;

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
        basePathCampaign =  configService.getString(IMConstants.BASE_PATH_CAMPAIGN);
        readCampaignFilesystem = configService.getBoolean(IMConstants.READ_CAMPAIGN_IMAGE_FILESYSTEM);
    }

    @Override
    @Transactional
    public void generateInvoicePDF() throws InterruptedException {
        Long numOfThreads = configService.getLong(IMConstants.NUM_OF_THREAD_PDFGENERATOR);

        List<Statement> statements = statementRepository.readStatements(numOfThreads, StatementStatusEnum.PRE_PROCESSED.getStatus());
        logger.debug("Generate invoice pdf for "+ statements.size() + " statements");

        for(Statement statement:statements) {
            logger.debug("PDF generation will start for statement with id "+ statement.getId());
            statementService.updateStatement(statement,StatementStatusEnum.PDF_PROCESSING);
            statement.getSystemBatchInput().getTransferFile().getFilename();
        }
    }

    public void processSingleStatement(Statement statement){
        if(taskExecutor instanceof ThreadPoolTaskExecutor) {
            ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor)taskExecutor;
            logger.debug("PDF generator thread queue count " + executor.getThreadPoolExecutor().getQueue().size() +" active threads "+ executor.getActiveCount() + "max pool size "+executor.getMaxPoolSize()+ " :: "+executor.getThreadPoolExecutor().getActiveCount());
        }
        PDFGeneratorTask pdfGeneratorTask = applicationContext.getBean(PDFGeneratorTask.class,statement);
        taskExecutor.execute(pdfGeneratorTask);
    }

    @Override
    @Transactional
    public void generateInvoicePDFSingleStatement(Statement statement) {
        StopWatch stopWatch = new StopWatch();
        String systemBatchInputFileName = "";
        String subFolderName = "";
        try {
            byte[] generatedPdf = null;
            if(SetInvoiceASOnline.get()==null || !SetInvoiceASOnline.get())
            {
                systemBatchInputFileName = statement.getSystemBatchInput().getTransferFile().getFilename();
                stopWatch.start("PDF generation for statement "+ statement.getId());
                subFolderName = systemBatchInputFileName.substring(0, systemBatchInputFileName.indexOf('.'));

                generatedPdf = birtEnginePDFGenerator(statement, outputDirectoryPath, subFolderName);
            }
            else
            {
                stopWatch.start("Online PDF generation for invoice number "+ statement.getInvoiceNumber());
                systemBatchInputFileName = statement.getFileName();
                generatedPdf =   birtEnginePDFGenerator(statement, systemBatchInputFileName, subFolderName);
            }
            if(SetInvoiceASOnline.get()==null || !SetInvoiceASOnline.get())
            {
                statement = statementService.updateStatement(statement, StatementStatusEnum.PDF_PROCESSED);
            }
            else
            {
                statement.setStatus(StatementStatusEnum.PDF_PROCESSED.getStatus());
            }
            if(generatedPdf !=null && generatedPdf.length >0 ){
                logger.debug("generated pdf bytes of length " + generatedPdf.length);
            }
            //auditLogService.saveAuditLog(statement.getId(), StatementStatusEnum.PDF_PROCESSED.getStatus(), null, IMConstants.SUCCESS);
            invoiceGenerator.mergeInvoice(statement, generatedPdf);
            //statementService.updateStatement(statement, StatementStatusEnum.INVOICE_PROCESSED);
            // auditLogService.saveAuditLog(statement.getId(), StatementStatusEnum.INVOICE_PROCESSED.getStatus(), null, IMConstants.SUCCESS);
            logger.debug("PDF generated for statement with statementId "+ statement.getId()+ "completed");
            String directoryPath = null;
            if(SetInvoiceASOnline.get()==null || !SetInvoiceASOnline.get())
            {
                directoryPath = outputDirectoryPath+ File.separator + subFolderName + File.separator + statement.getInvoiceNumber();
            }
            else
            {
                directoryPath =statement.getFileName().replace("\\"+IMConstants.PROCESSED_STATEMENT_XML_FILE_NAME,"");
            }
            logger.debug(" Directory to delete path "+ directoryPath);
            cleanUpFiles(directoryPath);

            stopWatch.stop();
            logger.info(stopWatch.prettyPrint());

        } catch (PDFGeneratorException e) {
            if(SetInvoiceASOnline.get()==null || !SetInvoiceASOnline.get())   {
            logger.error("Exception in PDF generation for statement" + statement.getId(), e);
            statementService.updateStatement(statement, StatementStatusEnum.PDF_PROCESSING_FAILED);
            auditLogService.saveAuditLog(statement.getId(), StatementStatusEnum.PDF_PROCESSING.getStatus(), getCause(e).getMessage(), IMConstants.ERROR);
            }
            else
            {
                logger.error("Exception in PDF generation for statement" + statement.getId(), e);
                statement.setStatus(StatementStatusEnum.PDF_PROCESSING_FAILED.getStatus());
                throw e;
            }
        } catch (Exception e) {
            logger.error("Exception in PDF generation for statement" + statement.getId(), e);
            statementService.updateStatement(statement, StatementStatusEnum.PDF_PROCESSING_FAILED);
        }


    }

    public byte[] birtEnginePDFGenerator(Statement statement, String outPutDirectoryPath, String statementFolderName) throws BirtException {
        logger.debug("Generating Invoice PDF for Statement ID: " + statement.getId());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("Birt report generation");
        String accountNo = statement.getAccountNumber();
        String brand = statement.getBrand();
        ByteArrayOutputStream baos = null;
        try {
            String xmlFilePath = null;
            if(SetInvoiceASOnline.get() ==null || !SetInvoiceASOnline.get())
            {
                String basePath = outPutDirectoryPath + File.separator + statementFolderName + File.separator + statement.getInvoiceNumber() + File.separator ;
                xmlFilePath =  basePath + File.separator + IMConstants.PROCESSED_STATEMENT_XML_FILE_NAME;
            }
            else
            {
                xmlFilePath = outPutDirectoryPath;
            }
            String reportDesignFilePath = birtRPTPath + File.separator + "statementReport.rptdesign";


            String campaignImage = null;
            logger.debug("readCampaignFilesystem " + readCampaignFilesystem);
            if (readCampaignFilesystem) {
                campaignImage = getDefaultCampaignImage(statement);
            } else {
                campaignImage = segmentFileService.getImageContent(accountNo, brand);
            }
            if (null == campaignImage) {
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
            baos = new ByteArrayOutputStream();
            IRunAndRenderTask task  = reportEngine.createRunAndRenderTask(runnable);
            task.setParameterValue("sourcexml", xmlFilePath);
            task.setParameterValue("campaignImage", campaignImage);
            PDFRenderOption options = new PDFRenderOption();
            options.setEmbededFont(true);
            options.setOutputFormat("pdf");
            options.setOutputStream(baos);

            task.setRenderOption(options);
            task.run();
            task.close();
            stopWatch.stop();
            logger.info(stopWatch.prettyPrint());
            //logger.debug("Time to generate PDF for statement id  " + statement.getId() + " "+(endTime - startTime) + " milli seconds " + (endTime - startTime) / 1000 + "  seconds ");
            return baos.toByteArray();
        } catch (Exception e) {
            throw new PDFGeneratorException(e.getMessage());
        }


    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public byte[] generatePreview(Long layoutId, Integer version) throws IOException, BirtException {
        ByteArrayOutputStream baos = null;
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("Layout content Query getLayoutContentByLayoutIdandVersion");
            String rptDesign = layoutContentService.getLayoutContentByLayoutIdandVersion(layoutId, version);
            stopWatch.stop();
            logger.info(stopWatch.prettyPrint());
            if(null != rptDesign) {
                String encoding = configService.getString("layout.encoding");
                encoding = encoding == null ? "UTF-8" : encoding;
                InputStream designStream = new ByteArrayInputStream(rptDesign.getBytes(Charset.forName(encoding)));
                String campaignImage = segmentFileService.getCampaignForPreview(sampleCampaignImagePath);
                IReportRunnable runnable = reportEngine.openReportDesign(designStream);
                baos = new ByteArrayOutputStream();
                IRunAndRenderTask task = reportEngine.createRunAndRenderTask(runnable);
                task.setParameterValue("sourcexml", sampleStatementFilePath);
                task.setParameterValue("campaignImage", campaignImage);
                PDFRenderOption options = new PDFRenderOption();
                logger.debug("sampleStatementFilePath: " + sampleStatementFilePath + " sampleCampaignImagePath " + sampleCampaignImagePath);
                //options.setFontDirectory(customPdfFontPath);
                options.setEmbededFont(true);
                options.setOutputFormat("pdf");
                options.setOutputStream(baos);
                task.setRenderOption(options);
                task.run();
                task.close();
            }
        } catch (Exception e) {
            logger.error("exception in preview generation ",e);
            throw new RuntimeException("Can not generate preview !");
        }
        return baos.toByteArray();
    }

    public void processStatement(Statement statement){
        if(taskExecutor instanceof ThreadPoolTaskExecutor) {
            ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor)taskExecutor;
            logger.debug("PDF generator thread queue count " + executor.getThreadPoolExecutor().getQueue().size() +" active threads "+ executor.getActiveCount() + "max pool size "+executor.getMaxPoolSize()+ " :: "+executor.getThreadPoolExecutor().getActiveCount());
        }
        PDFGeneratorTask pdfGeneratorTask = applicationContext.getBean(PDFGeneratorTask.class,statement);
        taskExecutor.execute(pdfGeneratorTask);
    }

    @Transactional
    public void processStatement(Long statementId){
        if(taskExecutor instanceof ThreadPoolTaskExecutor) {
            ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor)taskExecutor;
            logger.debug("PDF generator thread queue count " + executor.getThreadPoolExecutor().getQueue().size() +" active threads "+ executor.getActiveCount() + "max pool size "+executor.getMaxPoolSize()+ " :: "+executor.getThreadPoolExecutor().getActiveCount());
        }
        PDFGeneratorTask pdfGeneratorTask = applicationContext.getBean(PDFGeneratorTask.class,statementId);
        taskExecutor.execute(pdfGeneratorTask);
        logger.debug("exiting PDFGenerator processStatement for statement with ID "+ statementId);
    }

    private void cleanUpFiles(String outputDirectoryPath){
        Boolean deleteFiles = configService.getBoolean(IMConstants.DELETE_GENERATED_FILES);
        try {
            if(null == deleteFiles || deleteFiles) {
                FileUtils.deleteDirectory(new File(outputDirectoryPath));
            }
        } catch (IOException e) {
            logger.error("Exception while deleting directory "+outputDirectoryPath, e);

        }
    }


    private Throwable getCause(Throwable e) {
        Throwable cause = null;
        Throwable result = e;

        while(null != (cause = result.getCause())  && (result != cause) ) {
            result = cause;
        }
        return result;
    }

    private String getDefaultCampaignImage(Statement statement)
    {
        String campaignImage = null;
        try {

            String brand =  null;
            if(SetInvoiceASOnline.get()==null || !SetInvoiceASOnline.get())
            {
                brand = statement.getSystemBatchInput().getTransferFile().getBrand();
            }
            else
            {
                brand = statement.getBrand();
            }
            int attachmentConfigId =0;
            double creditLimit = statement.getCreditLimit();
            if(configService.getBoolean(IMConstants.READ_ATTACHMENT_FROM_DB))
            {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start("Attachment Configuration Query getProcessedStatementByAccountNumber");

                List<Statement>  listOfStatements = statementRepository.getProcessedStatementByAccountNumber(statement.getAccountNumber(),StatementStatusEnum.INVOICE_PROCESSED.getStatus());
                stopWatch.stop();
                logger.info(stopWatch.prettyPrint());
                if(statement.getLegalPartClass()==null || statement.getLegalPartClass().equals(IMConstants.LEGAL_PART_CLASS_INDIVIDUAL) )
                {
                    if(listOfStatements==null ||(listOfStatements!=null && listOfStatements.isEmpty())) {
                        if( !Double.valueOf(creditLimit).equals(Double.valueOf("0")))
                        {
                            attachmentConfigId = AttachmentTypeEnum.FULL_KONTROLL_ATTACHMENT.getStatus();
                        }
                        else
                        {
                            attachmentConfigId = AttachmentTypeEnum.FIRST_TIME_ATTACHMENT.getStatus();
                        }
                    }
                    else
                    {
                        attachmentConfigId = AttachmentTypeEnum.OTHER_ATTACHMENT.getStatus();
                    }
                }
                else
                {
                     if(statement.getSystemBatchInput().getTransferFile().getBrand().equals("FKAS") || statement.getSystemBatchInput().getTransferFile().getBrand().equals("TKAS")) {
                          attachmentConfigId = AttachmentTypeEnum.ORGANIZATION.getStatus();
                     } else {
                         if(listOfStatements==null ||(listOfStatements!=null && listOfStatements.isEmpty())) {
                             if( !Double.valueOf(creditLimit).equals(Double.valueOf("0")))
                             {
                                 attachmentConfigId = AttachmentTypeEnum.FULL_KONTROLL_ATTACHMENT.getStatus();
                             }
                             else
                             {
                                 attachmentConfigId = AttachmentTypeEnum.FIRST_TIME_ATTACHMENT.getStatus();
                             }
                         }
                         else
                         {
                             attachmentConfigId = AttachmentTypeEnum.OTHER_ATTACHMENT.getStatus();
                         }
                     }
                }
                logger.debug("Attachment Configuration ID " + attachmentConfigId + " For statement "+ statement.getStatementId() );

                statement.setAttachmentConfigId(attachmentConfigId);
                if(SetInvoiceASOnline.get() == null || !SetInvoiceASOnline.get()) {
                statementService.updateStatement(statement);
                }
                stopWatch = new StopWatch();
                stopWatch.start("Attachment Config Query getAttachmentByBrandAndAttachmentName ");
                List<Attachment> listOfAttachments = attachmentConfigService.getAttachmentByBrandAndAttachmentName(brand,attachmentConfigId);
                stopWatch.stop();
                logger.info(stopWatch.prettyPrint());
                if(listOfAttachments!=null && !listOfAttachments.isEmpty())
                {
                    logger.debug("list Of attachments found for brand " + brand+ " and attachment configuration ID " + attachmentConfigId + " = " + listOfAttachments.size() );
                    for(Attachment attachmentFile : listOfAttachments)
                    {
                        if("image".equalsIgnoreCase(attachmentFile.getAttachmentType().toLowerCase()))
                        {    if(readCampaignFilesystem)
                        {
                            campaignImage =  attachmentFile.getFileContent();
                            logger.debug(" reading campaign from database for statement "+statement.getId());
                            break;
                        }
                        }
                    }
                }

            }
            if(campaignImage==null)
            {
                String path = basePathCampaign+brand+File.separator+brand.toLowerCase()+".jpg";
                logger.debug(" reading campaign from FS for statement "+statement.getId() + " path is "+ path);
                File f = new File(path);
                if(f.exists()) {
                    logger.debug(" reading campaign from FS file exists");
                    byte[] image = IOUtils.toByteArray(new FileInputStream(f));
                    campaignImage = Base64.encodeBase64String(image);
                }
            }
        }catch (Exception e) {
            logger.error("Error while getting default Campaign Image ",e);
            throw new PDFGeneratorException(e);
        }
        return campaignImage;
    }
}
