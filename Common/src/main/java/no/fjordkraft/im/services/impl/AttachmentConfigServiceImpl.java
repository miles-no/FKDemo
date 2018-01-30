package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.Attachment;
import no.fjordkraft.im.model.AttachmentConfig;
import no.fjordkraft.im.repository.AttachmentConfigRepository;
import no.fjordkraft.im.repository.AttachmentRepository;
import no.fjordkraft.im.services.AttachmentConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/10/18
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class AttachmentConfigServiceImpl implements AttachmentConfigService {

    @Autowired
    AttachmentConfigRepository attachmentConfigRepository;

    @Autowired
    AttachmentRepository attachmentRepository;


    @Override
    public List<AttachmentConfig> getAttachmentByBrand(String configName) {
        return attachmentConfigRepository.getAttachmentConfigByName(configName);
    }

    @Override
    public void saveAttachmentConfig(AttachmentConfig config) {
        attachmentConfigRepository.saveAndFlush(config);
    }


    public void saveAttachment(Attachment attachment)
    {
        attachmentRepository.saveAndFlush(attachment) ;
    }

    public List<Attachment> getAttachmentByBrandAndAttachmentName(String brand, long attachmentId)
    {
        return attachmentRepository.getAttachmentByBrandAndAttachmentName(brand,attachmentId);
    }

    @Override
    @Transactional
    public AttachmentConfig getAttachmentConfigByID(long id) {
        return attachmentConfigRepository.findAttachmentConfigById(id);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<AttachmentConfig> getAllAttachmentConfigs() {
        return attachmentConfigRepository.getAllAttachmentConfigs();  //To change body of implemented methods use File | Settings | File Templates.
    }


}
