package no.fjordkraft.im.ui.services;

import no.fjordkraft.im.domain.RestAttachment;
import no.fjordkraft.im.model.Attachment;
import no.fjordkraft.im.model.AttachmentConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/18/18
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */

public interface UIAttachmentService {
    RestAttachment saveAttachment(RestAttachment attachment,AttachmentConfig attachmentConfig);
    List<RestAttachment> getAllAttachmentByBrand(String brandName);
    void deleteAttachment(Long id);
    RestAttachment updateAttachment(Long id,Attachment attachment);
    List<RestAttachment> getAllAttachments();
    AttachmentConfig getAttachmentConfigById(Long id);
    List<AttachmentConfig> getAllAttachmentConfig();
    Attachment getAttachmentContentById(Long id);
    Attachment getAttachmentByFileType(String fileType,String brandName, Long attachmentConfigId);


}
