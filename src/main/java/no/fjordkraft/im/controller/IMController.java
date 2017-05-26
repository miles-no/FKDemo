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

    @RequestMapping("/")
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
        /*Employee emp = new Employee();
        emp.setEmpId(4);
        emp.setEmpAge(30);
        emp.setEmpName("Aniket");
        emp.setEmpSalary(300000);
        emp.setEmpAddress("mumbai");
        employeeRepository.save(emp);*/

        return "Greetings from Spring Boot!";
    }

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

        Map<String, Long> failed = new HashMap<String, Long>();
        Iterator mapIterator = null;

        for(StatusCount statusCount:statusCounts) {
            status = new StatusCount();
            if(statusCount.getStatus().equals(StatementStatusEnum.PENDING.getStatus())) {
                status.setStatus(UIStatementStatusEnum.PENDING.getStatus());
                status.setCount(statusCount.getCount());
                uiStatusCount.add(status);
            } else if(statusCount.getStatus().equals(StatementStatusEnum.PRE_PROCESSING.getStatus())){
                status.setStatus(UIStatementStatusEnum.PRE_PROCESSING.getStatus());
                status.setCount(statusCount.getCount());
                uiStatusCount.add(status);
            } else if(statusCount.getStatus().equals(StatementStatusEnum.PRE_PROCESSED.getStatus())){
                status.setStatus(UIStatementStatusEnum.PROCESSING.getStatus());
                status.setCount(statusCount.getCount());
                uiStatusCount.add(status);
            } else if(statusCount.getStatus().equals(StatementStatusEnum.PDF_PROCESSED.getStatus())) {
                status.setStatus(UIStatementStatusEnum.MERGING.getStatus());
                status.setCount(statusCount.getCount());
                uiStatusCount.add(status);
            } else if(statusCount.getStatus().equals(StatementStatusEnum.INVOICE_PROCESSED.getStatus())) {
                status.setStatus(UIStatementStatusEnum.READY.getStatus());
                status.setCount(statusCount.getCount());
                uiStatusCount.add(status);
            } else if(statusCount.getStatus().equals(StatementStatusEnum.PRE_PROCESSING_FAILED.getStatus()) ||
                    statusCount.getStatus().equals(StatementStatusEnum.PDF_PROCESSING_FAILED.getStatus()) ||
                    statusCount.getStatus().equals(StatementStatusEnum.INVOICE_PROCESSING_FAILED.getStatus()) ||
                    statusCount.getStatus().equals(StatementStatusEnum.DELIVERY_FAILED.getStatus())) {

                if(failed.isEmpty()) {
                    failed.put(UIStatementStatusEnum.FAILED.getStatus(), statusCount.getCount());
                } else {
                    sum = failed.get(UIStatementStatusEnum.FAILED.getStatus());
                    sum += statusCount.getCount();
                    failed.put(UIStatementStatusEnum.FAILED.getStatus(), sum);
                }
            }
        }
        if(!failed.isEmpty()) {
            status = new StatusCount();
            mapIterator = failed.entrySet().iterator();
            while (mapIterator.hasNext()) {
                Map.Entry entry = (Map.Entry) mapIterator.next();
                status.setStatus(UIStatementStatusEnum.FAILED.getStatus());
                status.setCount((Long) entry.getValue());
            }
            uiStatusCount.add(status);
        }

        return uiStatusCount;
    }

}