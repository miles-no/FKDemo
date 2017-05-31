package no.fjordkraft.im.controller;

/**
 * Created by bhavik on 4/28/2017.
 */
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.model.Employee;
import no.fjordkraft.im.model.InvoicePdf;
import no.fjordkraft.im.model.StatusCount;
import no.fjordkraft.im.model.TransferFile;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.services.Preprocessor;
import no.fjordkraft.im.preprocess.services.PreprocessorEngine;
import no.fjordkraft.im.preprocess.services.PreprocessorService;
import no.fjordkraft.im.repository.EmployeeRepository;
import no.fjordkraft.im.repository.InvoicePdfRepository;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.repository.TransferFileRepository;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.statusEnum.UIStatementStatusEnum;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@RestController
public class IMController {

    @Autowired
    DataSource dataSource;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    Unmarshaller unMarshaller;

    @Autowired
    PreprocessorEngine preprocessorEngine;

    @Autowired
    PreprocessorService preprocessorService;

    @Autowired
    TransferFileRepository transferFileRepository;

    @Autowired
    StatementRepository statementRepository;

    static int index=10;

    //static String baseDirectory = "E:\\\\FuelKraft\\\\invoice_manager\\\\if320-samples";
    static String baseDirectory = "E:\\FuelKraft\\bigfile";

    @Autowired
    InvoicePdfRepository invoicePdfRepository;

    /*@RequestMapping("/")
    public String index() throws Exception {
        try {
            StreamSource source = new StreamSource(new File("E:\\\\Tutorial\\\\workspace\\\\birtworkspace\\\\Parser\\\\StatementFile1.xml"));
            Statement stmt = (Statement)unMarshaller.unmarshal(source);
            System.out.println(stmt);
        } catch(Exception e){
            e.printStackTrace();
        }

        Statement stmt = preprocessorService.unmarshallStatement("E:\\\\Tutorial\\\\workspace\\\\birtworkspace\\\\Parser\\\\StatementFile1.xml");
        PreprocessRequest<Statement, Object> request = new PreprocessRequest<Statement, Object>();
        request.setStatement(stmt);
        preprocessorEngine.execute(request);
        *//*Employee emp = new Employee();
        emp.setEmpId(4);
        emp.setEmpAge(30);
        emp.setEmpName("Aniket");
        emp.setEmpSalary(300000);
        emp.setEmpAddress("mumbai");
        employeeRepository.save(emp);*//*

        return "Greetings from Spring Boot!";
    }*/

    @RequestMapping(value = "savetransferfile",method = RequestMethod.GET)
    public void saveTransferFile(@RequestParam("filepath") String filePath) throws IOException {

        File f = new File(baseDirectory);
        String[] files = f.list();
        for(String fileName : files ) {
            TransferFile transferFile = new TransferFile();
            File xmlFile = new File(f,fileName);
            String xml = FileUtils.readFileToString(xmlFile, StandardCharsets.ISO_8859_1);

            transferFile.setId(((Integer) index++).longValue());
            transferFile.setFileName(xmlFile.getName());
            transferFile.setBrand("FKAS");
            transferFile.setFileContent(xml);
            transferFile.setTransferState("PENDING");

            transferFileRepository.save(transferFile);
        }
    }

    @RequestMapping(value = "transferfile",method = RequestMethod.GET)
    public void transferFile(@RequestParam("filepath") String filePath) throws IOException {

        File f = new File("E:\\\\FuelKraft\\\\invoice_manager\\\\if320-samples\\\\20170320_TKAS_1489996002682_statement_e2b_ehf.xml");
        String[] files = f.list();

        TransferFile transferFile = new TransferFile();
        //File xmlFile = new File(f,fileName);
        String xml = FileUtils.readFileToString(f, StandardCharsets.ISO_8859_1);

        transferFile.setId(((Integer) index++).longValue());
        transferFile.setFileName(f.getName());
        transferFile.setBrand("TKAS");
        transferFile.setFileContent(xml);
        transferFile.setTransferState("PENDING");

        transferFileRepository.save(transferFile);

    }

    @RequestMapping(value = "getinvoicepdf", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getInvoicePdf(@RequestParam("id") Long id) throws IOException {
        InvoicePdf invoicePdf = invoicePdfRepository.findOne(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(invoicePdf.getPayload().length);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=sample.pdf");
        return new ResponseEntity<>(invoicePdf.getPayload(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "getStatementCountByStatus", method = RequestMethod.GET)
    @ResponseBody
    public List<StatusCount> getStatementCountByStatus() {
        List<StatusCount> statusCounts = statementRepository.getStatementStatus();
        List<StatusCount> uiStatusCount = new ArrayList<StatusCount>();
        StatusCount status = null;
        Long sum = 0l;

        Map<String, Long> statusMap = new HashMap<String, Long>();
        //Initialze statusMap
        statusMap.put(UIStatementStatusEnum.PENDING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.PRE_PROCESSING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.PROCESSING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.MERGING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.READY.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.FAILED.getStatus(), 0l);

        for(StatusCount statusCount:statusCounts) {
            status = new StatusCount();
            if(statusCount.getStatus().equals(StatementStatusEnum.PENDING.getStatus())) {
                statusMap.put(UIStatementStatusEnum.PENDING.getStatus(), statusCount.getCount());
            } else if(statusCount.getStatus().equals(StatementStatusEnum.PRE_PROCESSING.getStatus())){
                statusMap.put(UIStatementStatusEnum.PRE_PROCESSING.getStatus(), statusCount.getCount());
            } else if(statusCount.getStatus().equals(StatementStatusEnum.PRE_PROCESSED.getStatus())
                    || statusCount.getStatus().equals(StatementStatusEnum.PDF_PROCESSING.getStatus())){
                sum = statusMap.get(UIStatementStatusEnum.PROCESSING.getStatus());
                sum += statusCount.getCount();
                statusMap.put(UIStatementStatusEnum.PROCESSING.getStatus(), sum);
            } else if(statusCount.getStatus().equals(StatementStatusEnum.PDF_PROCESSED.getStatus())
                    || statusCount.getStatus().equals(StatementStatusEnum.INVOICE_PROCESSING.getStatus())) {
                sum = statusMap.get(UIStatementStatusEnum.MERGING.getStatus());
                sum += statusCount.getCount();
                statusMap.put(UIStatementStatusEnum.MERGING.getStatus(), sum);
            } else if(statusCount.getStatus().equals(StatementStatusEnum.INVOICE_PROCESSED.getStatus())
                    || statusCount.getStatus().equals(StatementStatusEnum.DELIVERY_PENDING.getStatus())) {
                sum = statusMap.get(UIStatementStatusEnum.READY.getStatus());
                sum += statusCount.getCount();
                statusMap.put(UIStatementStatusEnum.READY.getStatus(), sum);
            } else if(statusCount.getStatus().equals(StatementStatusEnum.PRE_PROCESSING_FAILED.getStatus()) ||
                    statusCount.getStatus().equals(StatementStatusEnum.PDF_PROCESSING_FAILED.getStatus()) ||
                    statusCount.getStatus().equals(StatementStatusEnum.INVOICE_PROCESSING_FAILED.getStatus()) ||
                    statusCount.getStatus().equals(StatementStatusEnum.DELIVERY_FAILED.getStatus())) {

                sum = statusMap.get(UIStatementStatusEnum.FAILED.getStatus());
                sum += statusCount.getCount();
                statusMap.put(UIStatementStatusEnum.FAILED.getStatus(), sum);
            }
        }

        status = new StatusCount();
        status.setStatus(UIStatementStatusEnum.PENDING.getStatus());
        status.setCount(statusMap.get(UIStatementStatusEnum.PENDING.getStatus()));
        uiStatusCount.add(0, status);

        status = new StatusCount();
        status.setStatus(UIStatementStatusEnum.PRE_PROCESSING.getStatus());
        status.setCount(statusMap.get(UIStatementStatusEnum.PRE_PROCESSING.getStatus()));
        uiStatusCount.add(1, status);

        status = new StatusCount();
        status.setStatus(UIStatementStatusEnum.PROCESSING.getStatus());
        status.setCount(statusMap.get(UIStatementStatusEnum.PROCESSING.getStatus()));
        uiStatusCount.add(2, status);

        status = new StatusCount();
        status.setStatus(UIStatementStatusEnum.MERGING.getStatus());
        status.setCount(statusMap.get(UIStatementStatusEnum.MERGING.getStatus()));
        uiStatusCount.add(3, status);

        status = new StatusCount();
        status.setStatus(UIStatementStatusEnum.READY.getStatus());
        status.setCount(statusMap.get(UIStatementStatusEnum.READY.getStatus()));
        uiStatusCount.add(4, status);

        status = new StatusCount();
        status.setStatus(UIStatementStatusEnum.FAILED.getStatus());
        status.setCount(statusMap.get(UIStatementStatusEnum.FAILED.getStatus()));
        uiStatusCount.add(5, status);

        return uiStatusCount;
    }

}