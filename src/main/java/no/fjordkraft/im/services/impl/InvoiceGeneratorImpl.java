package no.fjordkraft.im.services.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import no.fjordkraft.im.model.InvoicePdf;
import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.repository.InvoicePdfRepository;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.services.InvoiceGenerator;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by miles on 6/6/2017.
 */
@Service
public class InvoiceGeneratorImpl implements InvoiceGenerator {

    private static final Logger logger = LoggerFactory.getLogger(PDFGeneratorImpl.class);

    @Autowired
    private ConfigService configService;

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    InvoicePdfRepository invoicePdfRepository;

    @Autowired
    StatementService statementService;

    private String outputDirectoryPath;
    private String pdfGeneratedFolderName;
    private String invoiceGeneratedFolderName;
    private String controlFileDirectory;
    private String controlFileName;

    @Override
    @Transactional
    public void generateInvoice(Statement statement) {
        outputDirectoryPath = configService.getString(IMConstants.BASE_DESTINATION_FOLDER_PATH);
        pdfGeneratedFolderName = configService.getString(IMConstants.GENERATED_PDF_FOLDER_NAME);
        invoiceGeneratedFolderName = configService.getString(IMConstants.GENERATED_INVOICE_FOLDER_NAME);
        controlFileDirectory = configService.getString(IMConstants.CONTROL_FILE_PATH);
        controlFileName = configService.getString(IMConstants.CONTROL_FILE_NAME);

        String systemBatchInputFileName = statement.getSystemBatchInput().getFilename();
        String subFolderName = systemBatchInputFileName.substring(0, systemBatchInputFileName.indexOf('.'));
        String invoiceGeneratedFilePath = outputDirectoryPath + subFolderName + File.separator +
                statement.getInvoiceNumber() + File.separator + invoiceGeneratedFolderName + File.separator +
                statement.getInvoiceNumber() + ".pdf";

        try {
            logger.info("Start of Invoice generation for invoice number: " + statement.getInvoiceNumber() + " statement id "+ statement.getId());
            statementService.updateStatement(statement, StatementStatusEnum.INVOICE_PROCESSING);

            String files[] =
                    {
                            outputDirectoryPath + subFolderName + File.separator + statement.getInvoiceNumber() + File.separator +
                                    pdfGeneratedFolderName + File.separator + statement.getInvoiceNumber() + ".pdf",

                            controlFileDirectory + File.separator + controlFileName
                    };

            Document pdfCombineUsingJava = new Document();
            PdfCopy copy = new PdfCopy(pdfCombineUsingJava, new FileOutputStream(invoiceGeneratedFilePath));
            pdfCombineUsingJava.open();
            PdfReader ReadInputPDF;
            int number_of_pages;
            for (int i = 0; i < files.length; i++) {
                ReadInputPDF = new PdfReader(files[i]);
                number_of_pages = ReadInputPDF.getNumberOfPages();
                for (int page = 0; page < number_of_pages; ) {
                    copy.addPage(copy.getImportedPage(ReadInputPDF, ++page));
                }
            }
            pdfCombineUsingJava.close();

            saveInvoicePDFs(invoiceGeneratedFilePath, statement);
            /*statement.setStatus(StatementStatusEnum.INVOICE_PROCESSED.getStatus());
            statementService.updateStatement(statement);*/
            statementService.updateStatement(statement, StatementStatusEnum.INVOICE_PROCESSED);
            //updateStatementStatus(statement.getId(), StatementStatusEnum.INVOICE_PROCESSED.getStatus());
            logger.info("End of Invoice generation for invoice number: " + statement.getInvoiceNumber()+ " statement id "+ statement.getId());

        } catch (Exception ex) {
            statement.setStatus(StatementStatusEnum.INVOICE_PROCESSING_FAILED.getStatus());
            statementService.updateStatement(statement);
            logger.error("Exception in pdf merging",ex);
            //updateStatementStatus(statement.getId(), StatementStatusEnum.INVOICE_PROCESSING_FAILED.getStatus());
           // throw new RuntimeException(ex);
        }
    }

    /*private void updateStatementStatus(Long id, String status) {
        Statement statement = statementRepository.findOne(id);
        statement.setStatus(status);
        statement.setUdateTime(new Timestamp(System.currentTimeMillis()));
        statementService.updateStatement(statement);
    }*/

    private void saveInvoicePDFs(String invoiceGeneratedFilePath, Statement statement) throws IOException {
        File invoiceFile = new File(invoiceGeneratedFilePath);
        byte[] byteArray = Files.readAllBytes(invoiceFile.toPath());
        InvoicePdf invoicePdf = new InvoicePdf();
        invoicePdf.setStatement(statement);
        invoicePdf.setPayload(byteArray);
        invoicePdf.setType(IMConstants.INVOICE_PDF);
        invoicePdfRepository.saveAndFlush(invoicePdf);
    }
}
