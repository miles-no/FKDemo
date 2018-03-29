package no.fjordkraft.im.ui.services.impl;

import no.fjordkraft.im.domain.RestAttachment;
import no.fjordkraft.im.repository.AttachmentRepository;
import no.fjordkraft.im.model.Attachment;
import no.fjordkraft.im.model.AttachmentConfig;
import no.fjordkraft.im.services.AttachmentConfigService;
import no.fjordkraft.im.ui.services.UIAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/18/18
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UIAttachmentServiceImpl implements UIAttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentConfigService attachmentConfigService;

    @Override
    public RestAttachment saveAttachment(RestAttachment restAttachment,AttachmentConfig attachmentConfig) {
        Attachment attachment = new Attachment();
        attachment.setAttachmentConfig(attachmentConfig);
        attachment.setFileType(restAttachment.getFileExtension());
        attachment.setAttachmentType(restAttachment.getAttachmentType());
        attachment.setBrand(restAttachment.getBrandName());
        attachment.setFileContent(restAttachment.getFileContent());
        List<Attachment> attachmentList = attachmentRepository.getAttachmentByBrandAndAttachmentName(restAttachment.getBrandName(),attachmentConfig.getId());
        boolean isAlreadyExists = false;
        for(Attachment attachment1:attachmentList)
        {
            if(attachment1.getAttachmentConfig().getAttachmentName().equalsIgnoreCase(attachmentConfig.getAttachmentName()) && attachment1.getAttachmentType().equalsIgnoreCase(restAttachment.getAttachmentType())
            && attachment1.getFileType().equalsIgnoreCase(restAttachment.getAttachmentType()))
            {
                 isAlreadyExists = true;
            }
        }
        if(!isAlreadyExists)
        {
        Attachment savedAttachment = attachmentRepository.save(attachment);  //To change body of implemented methods use File | Settings | File Templates.
        RestAttachment newAttachment = new RestAttachment();
        newAttachment.setAttachmentId(savedAttachment.getAttachmentID());
        newAttachment.setAttachmentType(savedAttachment.getAttachmentType());
        newAttachment.setBrandName(savedAttachment.getBrand());
        newAttachment.setAttachmentTypeId(savedAttachment.getAttachmentConfig().getId());
        newAttachment.setAttachmentTypeName(savedAttachment.getAttachmentConfig().getAttachmentName());
        newAttachment.setFileExtension(savedAttachment.getFileType());

        return newAttachment;
        }
        else
        {
            return null;
        }
    }

    @Override
    public List<RestAttachment> getAllAttachmentByBrand(String brandName) {
        List<RestAttachment> listOfRestAttachments = new ArrayList<RestAttachment>();
        List<Attachment> listOfAttachment = attachmentRepository.getAttachmentByBrand(brandName);  //To change body of implemented methods use File | Settings | File Templates.
        if(listOfAttachment!=null && !listOfAttachment.isEmpty())
        {
            for(Attachment attachment:listOfAttachment)
            {
                RestAttachment attachmentToShow = new RestAttachment();
                attachmentToShow.setAttachmentId(attachment.getAttachmentID());
                attachmentToShow.setAttachmentType(attachment.getAttachmentType());
                attachmentToShow.setBrandName(attachment.getBrand());
                attachmentToShow.setAttachmentTypeId(attachment.getAttachmentConfig().getId());
                attachmentToShow.setAttachmentTypeName(attachment.getAttachmentConfig().getAttachmentName());
                attachmentToShow.setFileExtension(attachment.getFileType());
                listOfRestAttachments.add(attachmentToShow);
            }
        }
        return  listOfRestAttachments;
    }

    @Override
    public void deleteAttachment(Long id) {
        attachmentRepository.delete(id);
    }

    @Override
    public RestAttachment updateAttachment(Long id, Attachment attachment) {
        //To change body of implemented methods use File | Settings | File Templates.
        Attachment attachmentfound = attachmentRepository.findOne(attachment.getAttachmentID());
        if(null != attachmentfound) {
            attachmentfound.setAttachmentType(attachment.getAttachmentType());
            attachmentfound.setFileContent(attachment.getFileContent());
            attachmentfound.setBrand(attachment.getBrand());
            attachmentfound.setAttachmentConfig(attachment.getAttachmentConfig());
            attachmentRepository.saveAndFlush(attachmentfound);

        }
        RestAttachment attachmentToShow = new RestAttachment();
        attachmentToShow.setAttachmentId(attachmentfound.getAttachmentID());
        attachmentToShow.setAttachmentType(attachmentfound.getAttachmentType());
        attachmentToShow.setBrandName(attachmentfound.getBrand());
        attachmentToShow.setAttachmentTypeId(attachmentfound.getAttachmentConfig().getId());
        attachmentToShow.setAttachmentTypeName(attachmentfound.getAttachmentConfig().getAttachmentName());
        attachmentToShow.setFileExtension(attachmentfound.getFileType());
        return attachmentToShow;
    }

    @Override
    public List<RestAttachment> getAllAttachments() {

        List<RestAttachment> listOfRestAttachments = new ArrayList<RestAttachment>();
        List<Attachment> attachmentList = attachmentRepository.findAll();  //To change body of implemented methods use File | Settings | File Templates.
        if(attachmentList!=null && !attachmentList.isEmpty()) {
            for(Attachment attachment:attachmentList) {
                RestAttachment attachmentToShow = new RestAttachment();
                attachmentToShow.setAttachmentId(attachment.getAttachmentID());
                attachmentToShow.setAttachmentType(attachment.getAttachmentType());
                attachmentToShow.setBrandName(attachment.getBrand());
                attachmentToShow.setAttachmentTypeId(attachment.getAttachmentConfig().getId());
                attachmentToShow.setAttachmentTypeName(attachment.getAttachmentConfig().getAttachmentName());
                attachmentToShow.setFileExtension(attachment.getFileType());
                listOfRestAttachments.add(attachmentToShow);
            }
        }
        return listOfRestAttachments;
    }

    @Override
    public AttachmentConfig getAttachmentConfigById(Long id) {
        return attachmentConfigService.getAttachmentConfigByID(id);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<AttachmentConfig> getAllAttachmentConfig() {
        return attachmentConfigService.getAllAttachmentConfigs();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Attachment getAttachmentContentById(Long id) {
        return attachmentRepository.getContentById(id);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Attachment getAttachmentByFileType(String fileType, String brandName, Long attachmentConfigId) {

        List<Attachment> listOfAttachment =   attachmentRepository.getAttachmentByBrandAndAttachmentName(brandName,attachmentConfigId);  //To change body of implemented methods use File | Settings | File Templates.
        if(listOfAttachment!=null && !listOfAttachment.isEmpty())
        {
            for(Attachment attachment: listOfAttachment)
            {
                if( fileType.equalsIgnoreCase(attachment.getFileType()))
                {
                    return  attachment;
                }
            }
        }
        return null;
    }
}
