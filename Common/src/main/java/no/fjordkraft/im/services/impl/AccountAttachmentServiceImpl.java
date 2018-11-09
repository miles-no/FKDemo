package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.AccountAttachment;
import no.fjordkraft.im.model.AccountAttachmentMapping;
import no.fjordkraft.im.repository.AccountAttachmentMappingRepository;
import no.fjordkraft.im.repository.AccountAttachmentRepository;
import no.fjordkraft.im.services.AccountAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 11/2/18
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class AccountAttachmentServiceImpl implements AccountAttachmentService {

    @Autowired
    AccountAttachmentMappingRepository accountAttachmentMappingRepository;

    @Autowired
    AccountAttachmentRepository accountAttachmentRepository;
    @Override
    public AccountAttachmentMapping getAttachmentForAccountNo(String accountNo, String attachmentType, boolean isActive) {
        return accountAttachmentMappingRepository.getAttachmentForAccountNo(accountNo,attachmentType,isActive);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AccountAttachmentMapping getAttachmentForCustomerID(String customerNo, String attachmentType, boolean isActive) {
        return accountAttachmentMappingRepository.getAttachmentForCustomerNo(customerNo, attachmentType, isActive);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AccountAttachmentMapping saveAccountAttachmentMapping(AccountAttachmentMapping accountAttachmentMapping) {
        return accountAttachmentMappingRepository.saveAndFlush(accountAttachmentMapping);//To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public AccountAttachmentMapping updateAttachmentMapping(AccountAttachmentMapping accountAttachmentMapping) {
          return accountAttachmentMappingRepository.save(accountAttachmentMapping);
    }

    @Override
    public AccountAttachment updateAttachment(AccountAttachment accountAttachment) {
        return accountAttachmentRepository.saveAndFlush(accountAttachment);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteAttachmentForAccount(String accountNo) {
       // accountAttachmentMappingRepository.findOne(accountNo);//To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteAttachmentForCustomer(String customerNo) {
       //
    }

    @Override
    public void deleteMappingForAttachment(Long attachmentID) {
        accountAttachmentMappingRepository.deleteMappingForAttachment(attachmentID);
    }

    @Override
    public void deleteAttachment(Long attachmentID) {
       accountAttachmentRepository.delete(attachmentID);
    }

    @Override
    public List<AccountAttachmentMapping> getAllMappings() {
        return accountAttachmentMappingRepository.findAll();
    }

    @Override
    public AccountAttachment saveAccountAttachment(AccountAttachment accountAttachment) {
        return accountAttachmentRepository.save(accountAttachment);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AccountAttachment getAccountAttachment(Long attachmentID) {
        return accountAttachmentRepository.findOne(attachmentID);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<AccountAttachmentMapping> getMappingsForAttachment(Long attachmentID) {
        return accountAttachmentMappingRepository.getMappingForAccountAttachment(attachmentID);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional
    public List<AccountAttachmentMapping> saveAllAccountAttachmentMapping(List<AccountAttachmentMapping> listOfAccountAttachmentMap) {
        return accountAttachmentMappingRepository.save(listOfAccountAttachmentMap);

    }
}
