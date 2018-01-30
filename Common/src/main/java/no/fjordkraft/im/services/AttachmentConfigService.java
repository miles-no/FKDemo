package no.fjordkraft.im.services;


import no.fjordkraft.im.model.Attachment;
import no.fjordkraft.im.model.AttachmentConfig;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/10/18
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
public interface AttachmentConfigService {

  List<AttachmentConfig> getAttachmentByBrand(String brand);
     void saveAttachmentConfig(AttachmentConfig config);

     void saveAttachment(Attachment attachment);

    List<Attachment> getAttachmentByBrandAndAttachmentName(String brand,long id);

    AttachmentConfig getAttachmentConfigByID(long id);

    List<AttachmentConfig> getAllAttachmentConfigs();
}
