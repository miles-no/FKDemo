package no.fjordkraft.im.services.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.scene.transform.Rotate;
import no.fjordkraft.im.model.Attachment;
import no.fjordkraft.im.model.InvoicePdf;
import no.fjordkraft.im.model.SegmentFile;
import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.services.*;
import no.fjordkraft.im.statusEnum.AttachmentTypeEnum;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import no.fjordkraft.im.util.PDFUtil;
import no.fjordkraft.im.util.SetInvoiceASOnline;
import org.apache.axis.encoding.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

/**
 * Created by miles on 6/6/2017.
 */
@Service
public class InvoiceGeneratorImpl implements InvoiceGenerator {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceGeneratorImpl.class);

    @Autowired
    private ConfigService configService;

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    StatementService statementService;

    @Autowired
    SegmentFileService segmentFileService;

    @Autowired
    AuditLogServiceImpl auditLogService;

    @Autowired
    AttachmentConfigService attachmentConfigService;

    private String basePathCampaign;

    private boolean readAdvtPdfFileSystem;

    private boolean mergeRotatePDF;

    @PostConstruct
    public void initIt() throws Exception {

        basePathCampaign =  configService.getString(IMConstants.BASE_PATH_CAMPAIGN);
        readAdvtPdfFileSystem = configService.getBoolean(IMConstants.READ_ADVT_PDF_FILESYSTEM);
        mergeRotatePDF = configService.getBoolean(IMConstants.MERGE_ROTATE_ADVT_PDF);
    }

    @Override
    @Transactional
    public void mergeInvoice(Statement statement, byte[] generatedPdf) throws IOException, DocumentException {
        String accountNo = statement.getAccountNumber();
        int attachmentConfigId = statement.getAttachmentConfigId();
        logger.debug("Attachment Config ID in invoice generator " + attachmentConfigId);
        String brand = null;//statement.getBrand();
        if(SetInvoiceASOnline.get()==null || !SetInvoiceASOnline.get())
        {
            brand = statement.getSystemBatchInput().getTransferFile().getBrand();
        }
        else
        {
            brand = statement.getBrand();
        }
        //  statement.setAttachmentConfigId(attachmentConfigId);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("PDF merging for statement with id "+ statement.getId());

        try {
            logger.info("Start of PDF merging for invoice number: " + statement.getInvoiceNumber() + " statement id "+ statement.getId());
            if(SetInvoiceASOnline.get()==null || !SetInvoiceASOnline.get())   {
                statement = statementService.updateStatement(statement, StatementStatusEnum.INVOICE_PROCESSING);
            }
            else
            {
                statement.setStatus(StatementStatusEnum.INVOICE_PROCESSING.getStatus());
            }
            logger.info("status updated invoice number: " + statement.getInvoiceNumber() + " statement id "+ statement.getId());

            Document pdfCombine = new Document();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfCopy copy = new PdfCopy(pdfCombine, byteArrayOutputStream);
            pdfCombine.open();
            PdfReader readInputPDF;
            int number_of_pages;

            readInputPDF = new PdfReader(generatedPdf);
            number_of_pages = readInputPDF.getNumberOfPages();
            for (int page = 0; page < number_of_pages; ) {
                copy.addPage(copy.getImportedPage(readInputPDF, ++page));
            }

            if ( null != readInputPDF && number_of_pages % 2 != 0){
                copy.addPage(readInputPDF.getPageSize(1), readInputPDF.getPageRotation(1));
            }


            byte[] pdfBytes = null;
            logger.debug("readAdvtPdfFileSystem " + readAdvtPdfFileSystem);
            if(readAdvtPdfFileSystem ) {
                if(configService.getBoolean(IMConstants.READ_ATTACHMENT_FROM_DB))  {
                    pdfBytes = getDefaultSegmentFile(brand,attachmentConfigId);
                }
                if(pdfBytes==null) {
                    if(statement.getLegalPartClass()==null ||statement.getLegalPartClass().equals(IMConstants.LEGAL_PART_CLASS_INDIVIDUAL)) {
                    pdfBytes = getSegmentFileFromFS(brand);
                    } else {
                        if(!(statement.getSystemBatchInput().getTransferFile().getBrand().equals("FKAS")) && !(statement.getSystemBatchInput().getTransferFile().getBrand().equals("TKAS"))) {
                            pdfBytes = getSegmentFileFromFS(brand);
                        }
                    }
                }
            } else {
                pdfBytes = getSegmentFile(accountNo, brand);
            }

            if(null != pdfBytes) {
                readInputPDF = new PdfReader(pdfBytes);

                number_of_pages = readInputPDF.getNumberOfPages();
                for (int page = 0; page < number_of_pages; ) {
                    copy.addPage(copy.getImportedPage(readInputPDF, ++page));
                }
                if ( null != readInputPDF && number_of_pages % 2 != 0){
                    copy.addPage(readInputPDF.getPageSize(1), readInputPDF.getPageRotation(1));
                }
                logger.debug(" Current page number is " + copy.getCurrentPageNumber());
            } else {
                logger.warn(" Attach_PDF not found ");
                auditLogService.saveAuditLog(IMConstants.ATTACH_PDF, statement.getId(), StatementStatusEnum.INVOICE_PROCESSING.getStatus(),
                        "Attach_PDF not found", IMConstants.WARNING);
            }
            pdfCombine.close();
            if(SetInvoiceASOnline.get()==null || !SetInvoiceASOnline.get())
            {
                byte[] outputBytes =  byteArrayOutputStream.toByteArray();
                InvoicePdf invoicePdf = saveInvoicePDFs(outputBytes, statement);
                invoiceService.saveInvoicePdf(invoicePdf);
                statement = statementService.updateStatement(statement, StatementStatusEnum.INVOICE_PROCESSED);
            }
            else
            {
                byte[] outputBytes =  byteArrayOutputStream.toByteArray();
                statement.setGeneratedPDF(outputBytes);
                statement.setStatus(StatementStatusEnum.INVOICE_PROCESSED.getStatus());
            }
            logger.debug(" saved invoice pdf and update statement status to INVOICE_PROCESSED "+ statement.getId());
            stopWatch.stop();
            logger.info(stopWatch.prettyPrint());

        } catch (Exception ex) {
            statement.setStatus(StatementStatusEnum.INVOICE_PROCESSING_FAILED.getStatus());
            statementService.updateStatement(statement);
            logger.error("Exception in pdf merging",ex);
            throw ex;
        }
    }

    private byte[] getSegmentFileFromFS(String brand) throws IOException, DocumentException {
        String path = basePathCampaign+brand+File.separator+brand.toLowerCase()+".pdf";
        logger.debug(" reading campaign from FS path is "+ path);
        File f = new File(path);
        if(f.exists()) {
            logger.debug(" reading campaign from FS file exists");
            byte[] pdf = IOUtils.toByteArray(new FileInputStream(f));
            if(mergeRotatePDF) {
                pdf = PDFUtil.merge(pdf);
                pdf = PDFUtil.rotator(pdf);
            }
            return pdf;
        }
        return null;
    }

    private byte[] getDefaultSegmentFile(String brand,int attachmentConfigId)
    {
        String advPDF = null;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("Attachment Configuration Query in Invoice generator Impl");
        List<Attachment> listOfAttachments = attachmentConfigService.getAttachmentByBrandAndAttachmentName(brand, attachmentConfigId);
        stopWatch.stop();
        logger.info(stopWatch.prettyPrint());
        if(listOfAttachments!=null && !listOfAttachments.isEmpty())
        {
            logger.debug("No Of attachment configuration " + listOfAttachments.size());
            for(Attachment attachmentFile : listOfAttachments)
            {
                if("pdf".equalsIgnoreCase(attachmentFile.getAttachmentType().toLowerCase()))
                {    if(readAdvtPdfFileSystem)
                    advPDF =  attachmentFile.getFileContent();
                }
            }
            if(advPDF!=null)
            {
                logger.debug("Found advertisement PDF from attachment table ");
                File f = new File(basePathCampaign+brand+File.separator+brand.toLowerCase()+"_!.pdf");
                byte[] pdfBytes =  org.apache.commons.codec.binary.Base64.decodeBase64(advPDF);
                return pdfBytes;
            }
        }

        return null;
    }


    private byte[] getSegmentFile(String accountNo, String brand) throws IOException, DocumentException {
        SegmentFile segmentFile = segmentFileService.getSegmentFile(accountNo, brand);
        String basePath = configService.getString(IMConstants.CONTROL_FILE_PATH);
        String fileName = segmentFile.getFileType()+ "_" +segmentFile.getId() + "_" + segmentFile.getChanged().getTime()+".pdf";

        synchronized (InvoiceGeneratorImpl.class) {
            File f = new File(basePath + fileName);
            logger.debug(" SegmentFile " + segmentFile.getId() + "file is "+ f.getAbsolutePath() + " exists "+ f.exists());
            if (f.exists()) {
                return IOUtils.toByteArray(new FileInputStream(f));
            } else {
                String attachPDF = segmentFileService.getPDFContent(accountNo, brand);
                if (null != attachPDF) {
                    byte[] pdfBytes = Base64.decode(attachPDF);
                    pdfBytes = PDFUtil.merge(pdfBytes);
                    pdfBytes = PDFUtil.rotator(pdfBytes);
                    OutputStream oos = new FileOutputStream(f);
                    oos.write(pdfBytes);
                    oos.close();
                    return pdfBytes;
                }
            }
        }
        return null;
    }


    private InvoicePdf saveInvoicePDFs(String invoiceGeneratedFilePath, Statement statement) throws IOException {
        File invoiceFile = new File(invoiceGeneratedFilePath);
        byte[] byteArray = Files.readAllBytes(invoiceFile.toPath());
        InvoicePdf invoicePdf = new InvoicePdf();
        invoicePdf.setStatement(statement);
        invoicePdf.setPayload(byteArray);
        invoicePdf.setType(IMConstants.INVOICE_PDF);
        return invoicePdf;
    }

    private InvoicePdf saveInvoicePDFs(byte[] outputBytes, Statement statement) throws IOException {
        InvoicePdf invoicePdf = new InvoicePdf();
        invoicePdf.setStatement(statement);
        invoicePdf.setPayload(outputBytes);
        invoicePdf.setType(IMConstants.INVOICE_PDF);
        return invoicePdf;
    }
}
