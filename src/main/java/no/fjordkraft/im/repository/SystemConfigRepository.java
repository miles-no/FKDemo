package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by miles on 5/4/2017.
 */
@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig,Long> {

    @Query("select c.value from SystemConfig c where c.name = :name")
    String getConfigValue(@Param("name") String name);
}
