package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.AttachmentConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/10/18
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface AttachmentConfigRepository extends JpaRepository<AttachmentConfig,String> {

  //AttachmentConfig  getAttachmentContentByAttachmentId(Long id, boolean active);
  @Query("select c from AttachmentConfig c where name= :name ")
  List<AttachmentConfig> getAttachmentConfigByName(@Param("name") String configName);

    @Query("select c from AttachmentConfig c where id = :id")
    AttachmentConfig findAttachmentConfigById(@Param("id") long id );

    @Query("select c from AttachmentConfig c ")
    List<AttachmentConfig> getAllAttachmentConfigs();

}
