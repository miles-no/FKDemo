package no.fjordkraft.im.services;

import no.fjordkraft.im.model.AccountAttachment;
import no.fjordkraft.im.model.AccountAttachmentMapping;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 11/2/18
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AccountAttachmentService {

    public AccountAttachmentMapping getAttachmentForAccountNo(String accountNo,String attachmentType,boolean isActive);

    public AccountAttachmentMapping getAttachmentForCustomerID(String customerNo,String attachmentType, boolean isActive);

    public AccountAttachment getAccountAttachment(Long attachmentID);

    public List<AccountAttachmentMapping> getMappingsForAttachment(Long attachmentID);

    public AccountAttachmentMapping saveAccountAttachmentMapping(AccountAttachmentMapping accountAttachmentMapping);

    public AccountAttachment saveAccountAttachment(AccountAttachment accountAttachment);

    public List<AccountAttachmentMapping> saveAllAccountAttachmentMapping(List<AccountAttachmentMapping> listOfAccountAttachmentMap);

    public AccountAttachmentMapping updateAttachmentMapping(AccountAttachmentMapping accountAttachmentMapping);

    public AccountAttachment updateAttachment(AccountAttachment accountAttachment);

    public void deleteAttachmentForAccount(String accountNo);

    public void deleteAttachmentForCustomer(String customerNo);

    public void deleteMappingForAttachment(Long attachmentID);

    public void deleteAttachment(Long attachmentID);
}
