package no.fjordkraft.im.ui.services.impl;

import no.fjordkraft.im.repository.AttachmentRepository;
import no.fjordkraft.im.model.Attachment;
import no.fjordkraft.im.model.AttachmentConfig;
import no.fjordkraft.im.services.AttachmentConfigService;
import no.fjordkraft.im.ui.services.UIAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Attachment saveAttachment(Attachment attachment) {
        return attachmentRepository.save(attachment);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Attachment> getAllAttachmentByBrand(String brandName) {
        return attachmentRepository.getAttachmentByBrand(brandName);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteAttachment(Long id) {
        attachmentRepository.delete(id);
    }

    @Override
    public Attachment updateAttachment(Long id, Attachment attachment) {
         //To change body of implemented methods use File | Settings | File Templates.
        Attachment attachmentfound = attachmentRepository.findOne(attachment.getAttachmentID());
        if(null != attachmentfound) {
            attachmentfound.setAttachmentType(attachment.getAttachmentType());
            attachmentfound.setFileContent(attachment.getFileContent());
            attachmentfound.setBrand(attachment.getBrand());
            attachmentfound.setAttachmentConfig(attachment.getAttachmentConfig());
            attachmentRepository.saveAndFlush(attachmentfound);

        }
        return attachmentfound;
    }

    @Override
    public List<Attachment> getAllAttachments() {
        return attachmentRepository.findAll();  //To change body of implemented methods use File | Settings | File Templates.
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
}
