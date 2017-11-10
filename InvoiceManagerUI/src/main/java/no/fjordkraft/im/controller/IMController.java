package no.fjordkraft.im.controller;

/**
 * Created by bhavik on 4/28/2017.
 */

import no.fjordkraft.im.model.*;
import no.fjordkraft.im.repository.InvoicePdfRepository;
import no.fjordkraft.im.repository.TransferFileRepository;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.statusEnum.UIStatementStatusEnum;

import no.fjordkraft.im.ui.services.UIStatementService;
import no.fjordkraft.im.ui.services.UITransferFileArchiveService;
import no.fjordkraft.im.ui.services.UITransferFileService;
import no.fjordkraft.im.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class IMController {

    private static final Logger logger = LoggerFactory.getLogger(IMController.class);

    @Autowired
    DataSource dataSource;

    @Autowired
    TransferFileRepository transferFileRepository;

    @Autowired
    UITransferFileArchiveService transferFileArchiveService;

    @Autowired
    UITransferFileService transferFileService;

    @Autowired
    UIStatementService statementService;

    static int index=10;

    static String baseDirectory = "E:\\FuelKraft\\bigfile";

    @Autowired
    InvoicePdfRepository invoicePdfRepository;

    @RequestMapping(value="/error",method=RequestMethod.GET)
    public void handleError(HttpServletRequest request , HttpServletResponse response){
        logger.debug(" error handler response is "+response.getStatus());
    }

    /*@PostConstruct
    public void test() throws IOException {
        this.loadIF320Files("E:\\FuelKraft\\DataForIM\\newdata\\toloadfileRs", null);
        TransferFile transferFile = transferFileService.getOneTransferFileWithEmptyIMStatus();
        logger.debug("Updating status of Transfer file with filename "+ transferFile.getFilename() + " brand "+ transferFile.getBrand() + " batch id" +transferFile.getEkBatchJobId());
        transferFile.setImStatus("PENDING");
        transferFileService.saveTransferFile(transferFile);
    }*/


    @RequestMapping(value = "load/if320",method = RequestMethod.POST)
    public void loadIF320Files(@RequestParam("filepath") String filePath,@RequestParam(value = "encoding",required = false) String encoding) throws IOException {
        logger.debug("Directory path is "+ filePath);
        File f = new File(filePath);
        File[] files = f.listFiles();
        if(null != files) {
            for (File file : files) {
                if(file.isDirectory())
                    continue;
                String fileName = file.getName();
                logger.debug("loading file " + fileName);
                String name = fileName.substring(0, fileName.indexOf("."));
                String strArray[] = fileName.split("_");
                logger.debug("Filename " + name + " brand " + strArray[1]);
                TransferFile transferFile = new TransferFile(TransferTypeEnum.if320, strArray[1], fileName);
                File xmlFile = new File(f, fileName);
                if(StringUtils.isEmpty(encoding)) {
                    encoding = "UTF-8";
                }
                String xml = FileUtils.readFileToString(xmlFile, Charset.forName(encoding));
                //transferFile.setTransferStatus(TransferStatusEnum.Stored);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                transferFile.setCreated(timestamp);
                //transferFile.setImStatus("PENDING");
                transferFile.setUploadStarted(timestamp);
                transferFile.setUploadEnded(timestamp);
                transferFile.setCompelloUploadStarted(timestamp);
                transferFile.setCompelloUploadEnded(timestamp);
                transferFileRepository.save(transferFile);

                TransferFileArchive transferFileArchive = new TransferFileArchive(TransferTypeEnum.if320, strArray[1], fileName);
                transferFileArchive.setFileContent(xml);
                transferFileArchive.setFileSize(xmlFile.length());
                transferFileArchive.setFileStored(timestamp);
                transferFileArchiveService.save(transferFileArchive);
                logger.debug("Done loading file " + fileName);
            }
        }
    }

    @RequestMapping(value = "transferfile/process",method = RequestMethod.POST)
    @ResponseBody
    public TransferFile updateTransferFileStatusToPending(){
        TransferFile transferFile = transferFileService.getOneTransferFileWithEmptyIMStatus();

        if(null != transferFile) {
            logger.debug("Updating status of Transfer file with filename " + transferFile.getFilename() + " brand " + transferFile.getBrand() + " batch id" + transferFile.getEkBatchJobId());
            transferFile.setImStatus("PENDING");
            transferFileService.saveTransferFile(transferFile);
        } else {
            logger.debug(" no transferfile with empty IMSTatus found ");
        }

        return transferFile;

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
        return new ResponseEntity<byte[]>(invoicePdf.getPayload(), headers, HttpStatus.OK);
    }

    @RequestMapping(value="split",method=RequestMethod.POST)
    public void splitAndSaveStatementFiles(@RequestParam(value = "fileName",required = false) String fileName, @RequestParam(value="batchId",required = false) String batchId,
                                           @RequestParam(value = "path",required = false) String path,@RequestParam(value = "host",required = false) String host,
                                           @RequestParam(value = "userName",required = false) String userName,
                                           @RequestParam(value = "password",required = false) String password,@RequestParam(value = "service",required = false) String service,
                                           @RequestParam(value = "encoding",required = false) String encoding ,@RequestParam(value = "port",required = false) Integer port){
        String filename = null;
        Connection conn = null;
        try {

            if(StringUtils.isEmpty(host)) {
                conn = dataSource.getConnection();
            } else {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                String url = "jdbc:oracle:thin:@"+host+":"+port+"/"+service;
                logger.debug("url is "+url);
                conn = DriverManager.getConnection(url, userName, password);
            }

            String query = "";
            if(StringUtils.isEmpty(batchId)) {
                query = "select * from transferfile where filename = ?";
            } else if (StringUtils.isEmpty(filename)) {
                query = "select * from transferfile where ekbatchjobid = ?";
            } else {
                query = "select * from transferfile where filename = ? and ekbatchjobid = ?";
            }
            PreparedStatement stmt = conn.prepareStatement(query);
            if(StringUtils.isEmpty(batchId)) {
                stmt.setString(1, fileName);
            } else if (StringUtils.isEmpty(fileName)){
                stmt.setString(1,batchId);
            } else {
                stmt.setString(1, fileName);
                stmt.setString(2, batchId);
            }


            ResultSet rs = stmt.executeQuery();
            //int index=0;

            while(rs.next()) {
                filename = rs.getString("FILENAME");
                logger.debug("file is"+filename);
                String payload = rs.getString("FILECONTENT");

                InputStream inputStream = new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8));

                byte[] b = new byte[2];
                int i = inputStream.read(b);
                logger.debug("bytes read is "+ new String(b) + " "+ i);


                Util.parse(new InputStreamReader(inputStream), path, fileName);
            }
            if(rs!=null){
                rs.close();
            }
            if(conn!=null){
                conn.close();
            }

        }catch (Exception e){
            logger.error("Erorr parsing file "+filename,e);
        }
    }


    @RequestMapping(value="savefile",method=RequestMethod.POST)
    public void saveStatementFile(@RequestParam(required = false) String fileName, @RequestParam(required = false) String batchId,
                                  @RequestParam(value = "path",required = false) String path,@RequestParam(value = "host",required = false) String host,
                                  @RequestParam(value = "userName",required = false) String userName,
                                  @RequestParam(value = "password",required = false) String password,@RequestParam(value = "service",required = false) String service,
                                  @RequestParam(value = "encoding",required = false) String encoding ,@RequestParam(value = "port",required = false) Integer port ){
        String outFile = null;
        Connection conn = null;
        try {
            logger.debug("saving file fileName"+ fileName + " batchId "+ batchId);
            if(StringUtils.isEmpty(host)) {
                conn = dataSource.getConnection();
            } else {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                String url = "jdbc:oracle:thin:@"+host+":"+port+"/"+service;
                logger.debug("url is "+url);
                conn = DriverManager.getConnection(url, userName, password);
            }

            String query = "";
            if(StringUtils.isEmpty(batchId)) {
                query = "select * from eacprod.transferfile where filename = ?";
            } else if (StringUtils.isEmpty(fileName)) {
                query = "select * from eacprod.transferfile where ekbatchjobid = ?";
            } else {
                query = "select * from eacprod.transferfile where filename = ? and ekbatchjobid = ?";
            }
           
            PreparedStatement stmt = conn.prepareStatement(query);
            if(StringUtils.isEmpty(batchId)) {
                stmt.setString(1, fileName);
                batchId ="";
            } else if (StringUtils.isEmpty(fileName)){
                stmt.setString(1,batchId);
                File f = new File(path+batchId);
                f.mkdirs();
                logger.debug("directory created "+ f.getAbsolutePath());
            } else {
                stmt.setString(1, fileName);
                stmt.setString(2, batchId);
            }


            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                outFile = rs.getString("FILENAME");
                logger.debug("file is"+outFile);
                Clob clob = rs.getClob("FILECONTENT");
                Reader reader = clob.getCharacterStream();
                if(batchId.length() > 0 && batchId.indexOf("/") == -1) {
                    batchId = batchId + File.separator;
                }
                File file = new File(path+batchId+outFile);
                FileWriter writer = new FileWriter(file);

                int i = -1;
                while((i= reader.read()) != -1 ) {
                    writer.write(i);
                }
                reader.close();
                writer.close();

            }
            if(rs!=null){
                rs.close();
            }
            if(conn!=null){
                conn.close();
            }

        }catch (Exception e){
            logger.error("Erorr parsing file "+outFile,e);
        }
    }


    @RequestMapping(value = "controlfile")
    public void getControlFiles(@RequestParam(value = "filePath",required = false) String filePath,@RequestParam(value = "host",required = false) String host,
                                @RequestParam(value = "userName",required = false) String userName,
                                @RequestParam(value = "password",required = false) String password,@RequestParam(value = "service",required = false) String service,
                                @RequestParam(value = "encoding",required = false) String encoding ,@RequestParam(value = "port",required = false) Integer port,
                                @RequestParam(value= "type") String type, @RequestParam(value= "id",required = false) Long id)
    {
        String filename = null;
        Connection conn = null;
        try {
            if(StringUtils.isEmpty(host)) {
                conn = dataSource.getConnection();
            } else {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                String url = "jdbc:oracle:thin:@"+host+":"+port+"/"+service;
                logger.debug("url is "+url);
                conn = DriverManager.getConnection(url, userName, password);
            }

            String query = "";
            if(null != id) {
                query = "select * from eacprod.SEGMENTFILE where type=? and id =?";
            } else {
                query = "select * from eacprod.SEGMENTFILE where type=?";
            }
            PreparedStatement stmt = conn.prepareStatement(query);
            if(null != id) {
                stmt.setString(1, type);
                stmt.setLong(2, id);
            } else {
                stmt.setString(1, type);
            }
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                filename = rs.getString("FILENAME");
                logger.debug("Reading control file "+filename);
                String content = rs.getString("FILECONTENT");
                byte[] decoded = Base64.decodeBase64(content.getBytes());

                //File f = new File(filePath);
                File file = new File(filePath,filename);
                FileWriter writer = new FileWriter(file);
                Reader reader = new InputStreamReader(new ByteArrayInputStream(decoded));
                int i = -1;
                while((i= reader.read()) != -1 ) {
                    writer.write(i);
                }
                reader.close();
                writer.close();

            }
            if(rs!=null){
                rs.close();
            }
            if(conn!=null){
                conn.close();
            }

        }catch (Exception e){
            logger.error("Erorr getting control file "+filename,e);
        }
    }


    @RequestMapping(value = "transferfile/status", method=RequestMethod.GET)
    @ResponseBody
    public List<StatusCount> getTransferfileStatus(@RequestParam("filename") String filename) {
        return statementService.getStatusAndCountByTransferfile(filename);
    }

    @RequestMapping(value = "ekBatchJob/status", method=RequestMethod.GET)
    @ResponseBody
    public List<StatusCount> getTransferfileStatusByBatchId(@RequestParam("ekBatchJobId") Long ekBatchJobId) {
        return statementService.getStatusByTransferfileBatchId(ekBatchJobId);
    }

    private List<StatusCount> mapStatusToUIStatus(List<StatusCount> statusCounts) {
        Map<String, Long> statusMap = new HashMap<String, Long>();
        List<StatusCount> uiStatusCount = new ArrayList<StatusCount>();
        StatusCount status = null;
        Long sum = 0l;

        statusMap.put(UIStatementStatusEnum.PENDING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.PRE_PROCESSING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.PROCESSING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.MERGING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.READY.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.FAILED.getStatus(), 0l);

        for(StatusCount statusCount:statusCounts) {
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
        status.setName("Total Invoice Processed");
        status.setValue(Long.valueOf(statusCounts.size()));
        uiStatusCount.add(6, status);

        return uiStatusCount;
    }

}