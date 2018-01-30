package no.fjordkraft.im.ui.services;

import no.fjordkraft.im.model.Attachment;
import no.fjordkraft.im.model.AttachmentConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/18/18
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */

public interface UIAttachmentService {
    Attachment saveAttachment(Attachment attachment);
    List<Attachment> getAllAttachmentByBrand(String brandName);
    void deleteAttachment(Long id);
    Attachment updateAttachment(Long id,Attachment attachment);
    List<Attachment> getAllAttachments();
    AttachmentConfig getAttachmentConfigById(Long id);
    List<AttachmentConfig> getAllAttachmentConfig();
    Attachment getAttachmentContentById(Long id);


}
