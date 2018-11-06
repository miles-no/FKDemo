package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.AccountAttachmentMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 11/5/18
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface AccountAttachmentMappingRepository extends JpaRepository<AccountAttachmentMapping,Long> {

    @Query("Select a from AccountAttachmentMapping a where a.accountNo = :accountNO  and a.isActive = :isActive and a.accountAttachment.attachmentType = :attachmentType" )
    AccountAttachmentMapping getAttachmentForAccountNo(@Param("accountNO") String accountNo,@Param("attachmentType") String attachmentType,@Param("isActive") boolean isActive);

    @Query("Select a from AccountAttachmentMapping a where a.customerID = :customerId and a.isActive = :isActive and a.accountAttachment.attachmentType = :attachmentType")
    AccountAttachmentMapping getAttachmentForCustomerNo(@Param ("customerId") String customerId,@Param("attachmentType") String attachmentType,@Param("isActive") boolean isActive);

    @Query("Select a from AccountAttachmentMapping a where a.accountAttachment.id = :attachmentID")
    List<AccountAttachmentMapping> getMappingForAccountAttachment(@Param("attachmentID") Long attachmentID);

    @Modifying
    @Transactional
    @Query("delete AccountAttachmentMapping a where a.accountAttachment.id = :attachmentID")
    void deleteMappingForAttachment(@Param("attachmentID") Long attachmentID);
}
