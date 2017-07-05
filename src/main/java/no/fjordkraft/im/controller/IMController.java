package no.fjordkraft.im.controller;

/**
 * Created by bhavik on 4/28/2017.
 */

import no.fjordkraft.im.model.*;
import no.fjordkraft.im.preprocess.services.PreprocessorEngine;
import no.fjordkraft.im.preprocess.services.PreprocessorService;
import no.fjordkraft.im.repository.InvoicePdfRepository;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.repository.TransferFileRepository;
import no.fjordkraft.im.services.TransferFileArchiveService;
import no.fjordkraft.im.services.TransferFileService;
import no.fjordkraft.im.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.Unmarshaller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.*;

@RestController
public class IMController {

    private static final Logger logger = LoggerFactory.getLogger(IMController.class);

    @Autowired
    DataSource dataSource;

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

    @Autowired
    TransferFileArchiveService transferFileArchiveService;

    @Autowired
    TransferFileService transferFileService;

    static int index=10;

    static String baseDirectory = "E:\\FuelKraft\\bigfile";

    @Autowired
    InvoicePdfRepository invoicePdfRepository;

    /*@PostConstruct
    public void test() throws IOException {
        this.loadIF320Files("E:\\FuelKraft\\DataForIM\\newdata\\toloadfiles", null);
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
                transferFile.setTransferStatus(TransferStatusEnum.Stored);
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
    public void updateTransferFileStatusToPending(){
        TransferFile transferFile = transferFileService.getOneTransferFileWithEmptyIMStatus();

        if(null != transferFile) {
            logger.debug("Updating status of Transfer file with filename " + transferFile.getFilename() + " brand " + transferFile.getBrand() + " batch id" + transferFile.getEkBatchJobId());
            transferFile.setImStatus("PENDING");
            transferFileService.saveTransferFile(transferFile);
        } else {
            logger.debug(" no transferfile with empty IMSTatus found ");
        }
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
                query = "select * from eacprod.transferfile where filename = ?";
            } else if (StringUtils.isEmpty(batchId)) {
                query = "select * from eacprod.transferfile where ekbatchjobid = ?";
            } else {
                query = "select * from eacprod.transferfile where filename = ? and ekbatchjobid = ?";
            }

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,fileName);
            stmt.setString(2,batchId);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                filename = rs.getString("FILENAME");
                logger.debug("file is"+filename);
                Clob clob = rs.getClob("FILECONTENT");
                Reader reader = clob.getCharacterStream();
                File file = new File(path+fileName);
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
            logger.error("Erorr parsing file "+filename,e);
        }
    }


    @RequestMapping(value = "controlfile")
    public void getControlFiles(@RequestParam(value = "filePath",required = false) String filePath,@RequestParam(value = "host",required = false) String host,
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
            String query = "select * from eacprod.SEGMENTFILE";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                filename = rs.getString("FILENAME");
                logger.debug("Reading control file "+filename);
                String content = rs.getString("FILECONTENT");
                byte[] decoded = Base64.decodeBase64(content.getBytes());

                File f = new File(filePath);
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



}