package no.fjordkraft.im.controller;

import no.fjordkraft.im.domain.RestAttachment;
import no.fjordkraft.im.model.AccountAttachment;
import no.fjordkraft.im.model.AccountAttachmentMapping;
import no.fjordkraft.im.model.Attachment;
import no.fjordkraft.im.model.AttachmentConfig;
import no.fjordkraft.im.services.AccountAttachmentService;
import no.fjordkraft.im.ui.services.UIAttachmentService;
import no.fjordkraft.im.ui.services.UIBrandService;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 11/5/18
 * Time: 10:19 AM
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/api/consumer")

public class IMAccountAttachmentController {

    private static final Logger logger = LoggerFactory.getLogger(IMAccountAttachmentController.class);

    @Autowired
    AccountAttachmentService accountAttachmentService;

    @Autowired
    UIBrandService brandService;

    @RequestMapping(value="mapping/{consumerNo}", method = RequestMethod.DELETE)
    @ResponseBody
    void deleteAttachmentForConsumer(@PathVariable("consumerNo") String consumerNo,@RequestParam(value="consumerNoType") String consumerNoType,@RequestParam(value="attachmentType") String attachmentType) {
        logger.debug(" in delete AttachmentForConsumer method.");
        AccountAttachmentMapping accountAttachmentMapping = null;
        if(consumerNoType!=null && IMConstants.INVOICE_ATTACHMENT_TYPE_FOR_ACCOUNT.contains(consumerNoType.toUpperCase())) {
             accountAttachmentMapping = accountAttachmentService.getAttachmentForAccountNo(consumerNo,attachmentType,true);
        } else if(consumerNoType!=null && IMConstants.INVOICE_ATTACHMENT_TYPE_FOR_CUSTOMER.contains(consumerNoType.toUpperCase()))   {
            accountAttachmentMapping = accountAttachmentService.getAttachmentForCustomerID(consumerNo,attachmentType,true);
        }
        if(accountAttachmentMapping!=null) {
            accountAttachmentMapping.setActive(false);
            accountAttachmentService.updateAttachmentMapping(accountAttachmentMapping);
        }
    }

    @RequestMapping(value="attachment/{attachmentID}", method = RequestMethod.DELETE)
    @ResponseBody
    void deleteAttachment(@PathVariable("attachmentID") Long attachmentID) {
        logger.debug(" in delete Attachment method.");
        List<AccountAttachmentMapping> attachmentMappings = accountAttachmentService.getMappingsForAttachment(attachmentID);
        accountAttachmentService.deleteMappingForAttachment(attachmentID);
        accountAttachmentService.deleteAttachment(attachmentID);
    }

    @RequestMapping(value="attachment/{consumerNo}", method=RequestMethod.PUT)
    void updateAttachmentForConsumer(@PathVariable("consumerNo") String consumerNo, @RequestParam(value="consumerNoType") String consumerNoType,
                          @RequestParam(value = "file", required = false) MultipartFile file,@RequestParam(value="attachmentID") Long attachmentID) throws Exception{
        logger.debug(" in update attachment for consumer method.");
        String fileExtension =  file.getOriginalFilename().split("\\.")[1];
        if("pdf".equalsIgnoreCase(fileExtension.toLowerCase()) || "jpg".equalsIgnoreCase(fileExtension.toLowerCase()) || "png".equalsIgnoreCase(fileExtension.toLowerCase()))
        {
            if(consumerNoType!=null && IMConstants.INVOICE_ATTACHMENT_TYPE_FOR_ACCOUNT.contains(consumerNoType.toUpperCase()))
            {
                StringTokenizer arrayOfAccountNos = new StringTokenizer(consumerNo,",");
                //First save File and then attach it to Accounts.
                String content = null;
                if(null != file) {
                    content   = Base64.encodeBase64String(file.getBytes());
                }
                AccountAttachment accountAttachment = new AccountAttachment();
                accountAttachment.setFileContent(content);
                accountAttachment.setFileType(fileExtension);
                if("pdf".equalsIgnoreCase(fileExtension.toLowerCase()))
                {
                    accountAttachment.setAttachmentType("PDF");
                }
                else{
                    accountAttachment.setAttachmentType("IMAGE");
                }
                accountAttachment.setDescription("For Account No " + consumerNo);
                accountAttachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
                accountAttachment = accountAttachmentService.saveAccountAttachment(accountAttachment);
                while(arrayOfAccountNos.hasMoreElements())
                {
                    String accountNo = arrayOfAccountNos.nextElement().toString();
                    AccountAttachmentMapping accountAttachmentMapping = accountAttachmentService.getAttachmentForAccountNo(accountNo,fileExtension,true);
                    if(accountAttachmentMapping==null)
                    {
                        accountAttachmentMapping = new AccountAttachmentMapping();
                        accountAttachmentMapping.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                        accountAttachmentMapping.setAccountAttachment(accountAttachment);
                        accountAttachmentMapping.setAccountNo(accountNo);
                        accountAttachmentMapping.setActive(true);
                        accountAttachmentService.saveAccountAttachmentMapping(accountAttachmentMapping);
                    }

                }
            }
            else if(consumerNoType!=null && IMConstants.INVOICE_ATTACHMENT_TYPE_FOR_CUSTOMER.contains(consumerNoType.toUpperCase()))
            {
                StringTokenizer arrayOfCustomerIds = new StringTokenizer(consumerNo,",");
                //First save File and then attach it to Accounts.
                String content = null;
                if(null != file) {
                    content   = Base64.encodeBase64String(file.getBytes());
                }
                AccountAttachment accountAttachment = new AccountAttachment();
                accountAttachment.setFileContent(content);
                accountAttachment.setAttachmentType(fileExtension);
                accountAttachment.setDescription("For Account No " + consumerNo);
                accountAttachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
                accountAttachment = accountAttachmentService.saveAccountAttachment(accountAttachment);
                while(arrayOfCustomerIds.hasMoreElements())
                {
                    String customerId = arrayOfCustomerIds.nextElement().toString();
                    AccountAttachmentMapping accountAttachmentMapping = accountAttachmentService.getAttachmentForCustomerID(customerId, fileExtension, true);
                    if(accountAttachmentMapping==null) {
                        accountAttachmentMapping = new AccountAttachmentMapping();
                    }
                    accountAttachmentMapping.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                    accountAttachmentMapping.setAccountAttachment(accountAttachment);
                    accountAttachmentMapping.setCustomerID(customerId);
                    accountAttachmentMapping.setActive(true);
                    accountAttachmentService.updateAttachmentMapping(accountAttachmentMapping);
                }
            }
        }
    }

    @RequestMapping(value="attachment", method=RequestMethod.POST)
    void saveAttachmentForConsumer(@RequestParam(value = "file", required = false) MultipartFile file)  throws AttachmentExistsException,IOException {
        logger.debug(" in save Attachment for Consumer method." );
        String fileExtension =  file.getOriginalFilename().split("\\.")[1];
        if("pdf".equalsIgnoreCase(fileExtension.toLowerCase()) || "jpg".equalsIgnoreCase(fileExtension.toLowerCase()) || "png".equalsIgnoreCase(fileExtension.toLowerCase())) {
        String content = null;
        if(null!=file) {
           content= Base64.encodeBase64String(file.getBytes());
        }
        AccountAttachment accountAttachment = new AccountAttachment();
        if("pdf".equalsIgnoreCase(fileExtension.toLowerCase())) {
            accountAttachment.setAttachmentType("PDF");
        }
        else {
            accountAttachment.setAttachmentType("IMAGE");
        }
        accountAttachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        accountAttachment.setFileType(fileExtension);
        accountAttachment.setDescription("Attachment " + file.getOriginalFilename());
        accountAttachment.setFileContent(content);
        accountAttachmentService.saveAccountAttachment(accountAttachment);
        }

    }

    @RequestMapping(value="mapping", method=RequestMethod.POST)
    void saveAttachmentMappingForConsumer(@RequestParam(value = "attachmentID") Long attachmentID,@RequestParam(value = "consumerID") String consumerID,@RequestParam(value="consumerNoType") String consumerNoTyp)  throws AttachmentExistsException,IOException {
        logger.debug(" in save Attachment Mapping for Consumer method." );
        AccountAttachment accountAttachment = accountAttachmentService.getAccountAttachment(attachmentID);
        if(accountAttachment!=null)
        {
            if(consumerNoTyp!=null && IMConstants.INVOICE_ATTACHMENT_TYPE_FOR_CUSTOMER.contains(consumerNoTyp.toUpperCase()))
            {
                StringTokenizer arrayOfCustomerIDs = new StringTokenizer(consumerID,",");
                List<AccountAttachmentMapping> listOfCustomerMappings = new ArrayList<AccountAttachmentMapping>();
                while(arrayOfCustomerIDs.hasMoreElements())
                {
                    AccountAttachmentMapping accountAttachmentMapping = new AccountAttachmentMapping();
                    accountAttachmentMapping.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    accountAttachmentMapping.setAccountAttachment(accountAttachment);
                    accountAttachmentMapping.setActive(true);
                    accountAttachmentMapping.setCustomerID(arrayOfCustomerIDs.nextElement().toString());
                    listOfCustomerMappings.add(accountAttachmentMapping);
                }
                if(listOfCustomerMappings!=null && !listOfCustomerMappings.isEmpty()) {
                    logger.debug("Size of customer Mappings " + listOfCustomerMappings.size());
                    accountAttachmentService.saveAllAccountAttachmentMapping(listOfCustomerMappings);
                }
            }
            else if(consumerNoTyp!=null && IMConstants.INVOICE_ATTACHMENT_TYPE_FOR_ACCOUNT.contains(consumerNoTyp.toUpperCase()))
            {
                StringTokenizer arrayOfAccountNos = new StringTokenizer(consumerID,",");
                List<AccountAttachmentMapping> listOfAccountMappings = new ArrayList<AccountAttachmentMapping>();
                while(arrayOfAccountNos.hasMoreElements())
                {
                    AccountAttachmentMapping accountAttachmentMapping = new AccountAttachmentMapping();
                    accountAttachmentMapping.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    accountAttachmentMapping.setAccountAttachment(accountAttachment);
                    accountAttachmentMapping.setActive(true);
                    accountAttachmentMapping.setAccountNo(arrayOfAccountNos.nextElement().toString());
                    listOfAccountMappings.add(accountAttachmentMapping);
                }
                if(listOfAccountMappings!=null && !listOfAccountMappings.isEmpty()) {
                    logger.debug("Size of account Mappings " + listOfAccountMappings.size());
                    accountAttachmentService.saveAllAccountAttachmentMapping(listOfAccountMappings);
                }
            }
        }
    }

    @RequestMapping(value = "content/{consumerID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getFileContentForConsumer(@PathVariable String consumerID,@RequestParam(value="consumerNoType")String consumerNoType,@RequestParam(value="attachmentType") String attachmentType) throws IOException {
        logger.debug("In get file Content for consumer method");
        AccountAttachmentMapping accountAttachmentMapping = null;
        if(IMConstants.INVOICE_ATTACHMENT_TYPE_FOR_ACCOUNT.contains(consumerNoType.toUpperCase())) {
             accountAttachmentMapping = accountAttachmentService.getAttachmentForAccountNo(consumerID, attachmentType, true);
        }
        if(accountAttachmentMapping==null) {
            accountAttachmentMapping = accountAttachmentService.getAttachmentForCustomerID(consumerID,attachmentType,true);
        }
        if(accountAttachmentMapping!=null) {
            AccountAttachment accountAttachment = accountAttachmentMapping.getAccountAttachment();
            String fileContent =  accountAttachment.getFileContent();
            byte pdfFile[] = Base64.decodeBase64(fileContent.getBytes());
            logger.debug("File bytes is "+ pdfFile.length+ " file length of string "+ fileContent.length());
            HttpHeaders headers = new HttpHeaders();
            if(!"image".equalsIgnoreCase(accountAttachment.getAttachmentType())){

                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentLength(pdfFile.length);
                headers.set(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=sample.pdf");
            }
            else {
                if("jpg".equalsIgnoreCase(accountAttachment.getFileType().toLowerCase()))  {
                    headers.setContentType(MediaType.IMAGE_JPEG);
                    headers.set(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=sample"+".jpg");
                }
                else
                {
                    headers.setContentType(MediaType.IMAGE_PNG);
                    headers.set(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=sample"+".png");
                }
                headers.setContentLength(pdfFile.length);

            }
            return new ResponseEntity<byte[]>(pdfFile, headers, HttpStatus.OK);
        }
        HttpHeaders headers = new HttpHeaders();
       return new ResponseEntity<byte[]>(null,headers,HttpStatus.OK);
    }
}
