package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/10/18
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Long> {

    @Query("select c from Attachment c where brand= :brand and c.attachmentConfig.id = :id ")
    List<Attachment> getAttachmentByBrandAndAttachmentName(@Param("brand") String brand,@Param("id") long Id);

    @Query("select c from Attachment c where brand= :brand")
    List<Attachment> getAttachmentByBrand(@Param("brand") String brand);

    @Query("select c from Attachment c where c.attachmentID  = :id ")
    Attachment getContentById(@Param("id") Long id);

}
