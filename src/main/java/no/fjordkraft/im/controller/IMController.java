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
import no.fjordkraft.im.util.IMConstants;
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
import java.sql.Timestamp;
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

    /*@RequestMapping(value = "getStatementCountByStatus", method = RequestMethod.GET)
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
            if(statusCount.getName().equals(StatementStatusEnum.PENDING.getStatus())) {
                statusMap.put(UIStatementStatusEnum.PENDING.getStatus(), statusCount.getValue());
            } else if(statusCount.getName().equals(StatementStatusEnum.PRE_PROCESSING.getStatus())){
                statusMap.put(UIStatementStatusEnum.PRE_PROCESSING.getStatus(), statusCount.getValue());
            } else if(statusCount.getName().equals(StatementStatusEnum.PRE_PROCESSED.getStatus())
                    || statusCount.getName().equals(StatementStatusEnum.PDF_PROCESSING.getStatus())){
                sum = statusMap.get(UIStatementStatusEnum.PROCESSING.getStatus());
                sum += statusCount.getValue();
                statusMap.put(UIStatementStatusEnum.PROCESSING.getStatus(), sum);
            } else if(statusCount.getName().equals(StatementStatusEnum.PDF_PROCESSED.getStatus())
                    || statusCount.getName().equals(StatementStatusEnum.INVOICE_PROCESSING.getStatus())) {
                sum = statusMap.get(UIStatementStatusEnum.MERGING.getStatus());
                sum += statusCount.getValue();
                statusMap.put(UIStatementStatusEnum.MERGING.getStatus(), sum);
            } else if(statusCount.getName().equals(StatementStatusEnum.INVOICE_PROCESSED.getStatus())
                    || statusCount.getName().equals(StatementStatusEnum.DELIVERY_PENDING.getStatus())) {
                sum = statusMap.get(UIStatementStatusEnum.READY.getStatus());
                sum += statusCount.getValue();
                statusMap.put(UIStatementStatusEnum.READY.getStatus(), sum);
            } else if(statusCount.getName().equals(StatementStatusEnum.PRE_PROCESSING_FAILED.getStatus()) ||
                    statusCount.getName().equals(StatementStatusEnum.PDF_PROCESSING_FAILED.getStatus()) ||
                    statusCount.getName().equals(StatementStatusEnum.INVOICE_PROCESSING_FAILED.getStatus()) ||
                    statusCount.getName().equals(StatementStatusEnum.DELIVERY_FAILED.getStatus())) {

                sum = statusMap.get(UIStatementStatusEnum.FAILED.getStatus());
                sum += statusCount.getValue();
                statusMap.put(UIStatementStatusEnum.FAILED.getStatus(), sum);
            }
        }

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.PENDING.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.PENDING.getStatus()));
        uiStatusCount.add(0, status);

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.PRE_PROCESSING.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.PRE_PROCESSING.getStatus()));
        uiStatusCount.add(1, status);

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.PROCESSING.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.PROCESSING.getStatus()));
        uiStatusCount.add(2, status);

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.MERGING.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.MERGING.getStatus()));
        uiStatusCount.add(3, status);

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.READY.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.READY.getStatus()));
        uiStatusCount.add(4, status);

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.FAILED.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.FAILED.getStatus()));
        uiStatusCount.add(5, status);

        status = new StatusCount();
        status.setName("Total");
        status.setValue(statementRepository.getTotalInvoiceCount());
        uiStatusCount.add(6, status);

        return uiStatusCount;
    }

    @RequestMapping(value = "getStatementCountByCity/{fromTime}/{toTime}", method = RequestMethod.GET)
    @ResponseBody
    public List<StatusCount> getStatusByCity(@PathVariable("fromTime") Timestamp fromTime, @PathVariable("toTime") Timestamp toTime) {
        return statementRepository.getStatusByCity(fromTime, toTime);
    }

    @RequestMapping(value = "getStatementCountByBrand/{fromTime}/{toTime}", method = RequestMethod.GET)
    @ResponseBody
    public List<StatusCount> getStatusByBrand(@PathVariable("fromTime") Timestamp fromTime, @PathVariable("toTime") Timestamp toTime) {
        return statementRepository.getStatusByBrand(fromTime, toTime);
    }

    @RequestMapping(value = "getInvoiceCountByTime/{fromTime}/{toTime}",method = RequestMethod.GET)
    public StatusCount getInvoiceCountByTime(@PathVariable("fromTime") Timestamp fromTime, @PathVariable("toTime") Timestamp toTime) {
        Long count = statementRepository.getInvoiceCountByTime(fromTime, toTime);
        StatusCount statusCount = new StatusCount();
        statusCount.setName("Total");
        statusCount.setValue(count);
        return statusCount;
    }

    @RequestMapping(value = "getOverviewData/{fromTime}/{toTime}", method = RequestMethod.GET)
    public Map<String, List<StatusCount>> getOverviewData(@PathVariable("fromTime") Timestamp fromTime, @PathVariable("toTime") Timestamp toTime) {
        Map<String, List<StatusCount>> map = new HashMap<String, List<StatusCount>>();
        List<StatusCount> totalInvoice = new ArrayList<>();
        totalInvoice.add(getInvoiceCountByTime(fromTime, toTime));
        List<StatusCount> statusByCity = getStatusByCity(fromTime, toTime);
        List<StatusCount> statusByBrand = getStatusByBrand(fromTime, toTime);

        map.put(IMConstants.TOTAL, totalInvoice);
        map.put(IMConstants.STATUS_BY_CITY, statusByCity);
        map.put(IMConstants.STATUS_BY_BRAND, statusByBrand);

        return map;
    }*/

}