package no.fjordkraft.im.controller;

import no.fjordkraft.im.domain.RestAttachment;
import no.fjordkraft.im.model.Attachment;
import no.fjordkraft.im.model.AttachmentConfig;
import no.fjordkraft.im.ui.services.UIAttachmentService;
import no.fjordkraft.im.ui.services.UIBrandService;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/18/18
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/api/attachment")
public class IMAttachmentController {

    private static final Logger logger = LoggerFactory.getLogger(IMAttachmentController.class);

    @Autowired
    UIAttachmentService attachmentService;

    @Autowired
    UIBrandService brandService;

    @RequestMapping(value="attachment/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void deleteAttachment(@PathVariable("id") Long id) {
        logger.debug(" in deleteLayoutConfig method.");
        attachmentService.deleteAttachment(id);
    }

    @RequestMapping(value="content/{id}", method=RequestMethod.PUT,consumes = {"multipart/form-data"},produces="application/json")
    void updateAttachment(@PathVariable("id") Long id,
                          @RequestParam(value = "file", required = false) MultipartFile file) throws Exception{
        logger.debug(" in update attachment method.");
        Attachment attachment = attachmentService.getAttachmentContentById(id);
        String content = null;
        if(null != file) {
            content   = Base64.encodeBase64String(file.getBytes());
        }
        String fileExtension =  file.getOriginalFilename().split("\\.")[1];
        if("pdf".equalsIgnoreCase(fileExtension.toLowerCase()) || "jpg".equalsIgnoreCase(fileExtension.toLowerCase()) || "png".equalsIgnoreCase(fileExtension.toLowerCase())) {

            attachment.setFileContent(content);
            attachment.setFileType(fileExtension);
        }
        attachment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        attachmentService.updateAttachment(id,attachment);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    List<RestAttachment> getAllAttachment(){
        logger.debug("Gets All Attachments  ");
        return  attachmentService.getAllAttachments();
    }

    @RequestMapping(value="config/all", method = RequestMethod.GET)
    List<AttachmentConfig> getAllAttachmentConfig() {
        return attachmentService.getAllAttachmentConfig();
    }

    @RequestMapping(value="attachment/{brand}", method=RequestMethod.POST)
    List<RestAttachment> getAllAttachmentsByBrand(@PathVariable("brandName") String brandName){
        logger.debug("In get Attachments By Brand method");
        return attachmentService.getAllAttachmentByBrand(brandName);
    }

    @RequestMapping(value="attachment", method=RequestMethod.POST,consumes = {"multipart/form-data"},produces="application/json")
    @ResponseBody
    ResponseEntity saveAttachment(@RequestParam("name") String brandName,@RequestParam("type") String fileType,@RequestParam("attachmentConfigId") long attachmentConfigId,@RequestParam(value = "file",
            required = false) MultipartFile file)  throws AttachmentExistsException,IOException {
        logger.debug(" in save Attachment  method." );
        String template = null;


        Attachment attachmentFound = attachmentService.getAttachmentByFileType(fileType,brandName,attachmentConfigId);
        if(attachmentFound==null) {
            AttachmentConfig attachmentConfig = attachmentService.getAttachmentConfigById(attachmentConfigId);
            if(attachmentConfig!=null) {
                if(null != file) {
                    // template = IOUtils.toString(file.getBytes());
                    template= Base64.encodeBase64String(file.getBytes());


                }

                String fileExtension =  file.getOriginalFilename().split("\\.")[1];
                if("pdf".equalsIgnoreCase(fileExtension.toLowerCase()) || "jpg".equalsIgnoreCase(fileExtension.toLowerCase()) || "png".equalsIgnoreCase(fileExtension.toLowerCase())) {
                    logger.debug("multipart file extention " + fileExtension);
                    RestAttachment newAttachment = new RestAttachment();
                    newAttachment.setBrandName(brandName);
                    newAttachment.setAttachmentType(fileType);
                    newAttachment.setFileContent(template);
                    newAttachment.setFileExtension(fileExtension);
                    logger.debug("Writing file.........");
                    File testPDFFile = new File("E:/test/testPDF.pdf");
                    FileOutputStream fos = new FileOutputStream(testPDFFile);
                    fos.write(template.getBytes());
                    fos.close();
                   RestAttachment restAttachment =  attachmentService.saveAttachment(newAttachment,attachmentConfig);
                    if(restAttachment==null)
                    {
                        HttpHeaders httpHeaders = new HttpHeaders();


                        return new ResponseEntity("Attachment Already Exists",httpHeaders,HttpStatus.BAD_REQUEST);
                       // throw new AttachmentExistsException("Attachment Already Exists");
                    }
                }else {
                    logger.error("File Extension is not supported. " + fileExtension);
                }
            }

        }
        else
        {
            HttpHeaders httpHeaders = new HttpHeaders();
            return new ResponseEntity<String>("Attachment Already Exists",httpHeaders,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "content/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getFileContent(@PathVariable Long id) throws IOException {
        logger.debug("In get file Content method");
        Attachment attachment = attachmentService.getAttachmentContentById(id);
        String fileContent =  attachment.getFileContent();
        byte pdfFile[] = Base64.decodeBase64(fileContent.getBytes());
        logger.debug("File bytes is "+ pdfFile.length+ " file length of string "+ fileContent.length());
        HttpHeaders headers = new HttpHeaders();
        if(!"image".equalsIgnoreCase(attachment.getAttachmentType())){

            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(pdfFile.length);
            headers.set(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=sample.pdf");
        }
        else {
            if("jpg".equalsIgnoreCase(attachment.getFileType().toLowerCase()))  {
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
}
