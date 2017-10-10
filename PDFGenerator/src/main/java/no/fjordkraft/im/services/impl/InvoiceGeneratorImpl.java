package no.fjordkraft.im.services.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.scene.transform.Rotate;
import no.fjordkraft.im.model.InvoicePdf;
import no.fjordkraft.im.model.SegmentFile;
import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.services.*;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import no.fjordkraft.im.util.PDFUtil;
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

    private String outputDirectoryPath;
    private String pdfGeneratedFolderName;
    private String invoiceGeneratedFolderName;
    private String controlFileDirectory;
    private String controlFileName;

    @PostConstruct
    public void setConfig(){
        outputDirectoryPath = configService.getString(IMConstants.BASE_DESTINATION_FOLDER_PATH);
        //pdfGeneratedFolderName = configService.getString(IMConstants.GENERATED_PDF_FOLDER_NAME);
        //invoiceGeneratedFolderName = configService.getString(IMConstants.GENERATED_INVOICE_FOLDER_NAME);
        controlFileDirectory = configService.getString(IMConstants.CONTROL_FILE_PATH);
        controlFileName = configService.getString(IMConstants.CONTROL_FILE_NAME);
    }

    @Override
    @Transactional
    public void generateInvoice(Statement statement) throws IOException, DocumentException {
        String accountNo = statement.getAccountNumber();
        String brand = statement.getSystemBatchInput().getTransferFile().getBrand();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("PDF merging for statement with id "+ statement.getId());

        String systemBatchInputFileName = statement.getSystemBatchInput().getTransferFile().getFilename();
        String subFolderName = systemBatchInputFileName.substring(0, systemBatchInputFileName.indexOf('.'));
        String invoiceGeneratedFilePath = outputDirectoryPath + subFolderName + File.separator +
                statement.getInvoiceNumber() + File.separator +
                statement.getInvoiceNumber() + "_Merged.pdf";

        try {
            logger.info("Start of PDF merging for invoice number: " + statement.getInvoiceNumber() + " statement id "+ statement.getId());
            statement = statementService.updateStatement(statement, StatementStatusEnum.INVOICE_PROCESSING);

            logger.info("status updated invoice number: " + statement.getInvoiceNumber() + " statement id "+ statement.getId());
            String filePath = outputDirectoryPath + subFolderName + File.separator + statement.getInvoiceNumber() + File.separator +
                              statement.getInvoiceNumber() + ".pdf";

            Document pdfCombineUsingJava = new Document();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //PdfCopy copy = new PdfCopy(pdfCombineUsingJava, new FileOutputStream(invoiceGeneratedFilePath));
            PdfCopy copy = new PdfCopy(pdfCombineUsingJava, byteArrayOutputStream);
            pdfCombineUsingJava.open();
            PdfReader readInputPDF;
            int number_of_pages;

            readInputPDF = new PdfReader(filePath);
            number_of_pages = readInputPDF.getNumberOfPages();
            for (int page = 0; page < number_of_pages; ) {
                copy.addPage(copy.getImportedPage(readInputPDF, ++page));
            }

            if ( null != readInputPDF && number_of_pages % 2 != 0){
                copy.addPage(readInputPDF.getPageSize(1), readInputPDF.getPageRotation(1));
            }

            //String attachPDF = segmentFileService.getPDFContent(accountNo, brand);
            byte[] pdfBytes = getSegmentFile(accountNo, brand);
            if(null != pdfBytes) {
                readInputPDF = new PdfReader(pdfBytes);
                number_of_pages = readInputPDF.getNumberOfPages();
                for (int page = 0; page < number_of_pages; ) {
                    copy.addPage(copy.getImportedPage(readInputPDF, ++page));
                }
                logger.debug(" Current page number is " + copy.getCurrentPageNumber());
            } else {
                logger.warn(" Attach_PDF not found ");
                auditLogService.saveAuditLog(IMConstants.ATTACH_PDF, statement.getId(), StatementStatusEnum.INVOICE_PROCESSING.getStatus(),
                        "Attach_PDF not found", IMConstants.WARNING);
            }
            pdfCombineUsingJava.close();
            byte[] outputBytes =  byteArrayOutputStream.toByteArray();
            logger.debug(" save created pdf in database ");
            InvoicePdf invoicePdf = saveInvoicePDFs(outputBytes, statement);
            invoiceService.saveInvoicePdf(invoicePdf);
            logger.debug(" update statement status to  INVOICE_PROCESSED ");
            statement = statementService.updateStatement(statement, StatementStatusEnum.INVOICE_PROCESSED);
            stopWatch.stop();
            logger.debug(stopWatch.prettyPrint());
            logger.info("End of PDF merging for invoice number: " + statement.getInvoiceNumber()+ " statement id "+ statement.getId());

        } catch (Exception ex) {
            statement.setStatus(StatementStatusEnum.INVOICE_PROCESSING_FAILED.getStatus());
            statementService.updateStatement(statement);
            logger.error("Exception in pdf merging",ex);
            throw ex;
        }
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
        //File invoiceFile = new File(invoiceGeneratedFilePath);
        //byte[] byteArray = Files.readAllBytes(invoiceFile.toPath());
        InvoicePdf invoicePdf = new InvoicePdf();
        invoicePdf.setStatement(statement);
        invoicePdf.setPayload(outputBytes);
        invoicePdf.setType(IMConstants.INVOICE_PDF);
        return invoicePdf;
    }




    public static void main(String[] args) throws IOException {

        String str = IOUtils.toString(new FileReader("C:\\Users\\bhavi\\Desktop\\fk\\PROD\\TKAS_p_642.txt"));
        byte[] pdfBytes = Base64.decode(str);
        IOUtils.write(pdfBytes, new FileOutputStream("C:\\Users\\bhavi\\Desktop\\fk\\PROD\\TKAS_p_642.pdf"));
    }

}
