package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.AccountAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 11/2/18
 * Time: 5:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface AccountAttachmentRepository extends JpaRepository<AccountAttachment,Long> {

    @Query("Select a from AccountAttachment a where a.id = :attachmentID" )
    AccountAttachment get(@Param("attachmentID") String attachmentID);



}
