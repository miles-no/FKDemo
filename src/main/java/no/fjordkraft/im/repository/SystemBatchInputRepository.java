package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.SystemBatchInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by miles on 5/5/2017.
 */
@Repository
public interface SystemBatchInputRepository extends JpaRepository<SystemBatchInput,Long> {

    @Query("select s from SystemBatchInput s where rownum <= 1 and s.status = :status order by s.id asc")
    SystemBatchInput readSingleSystemBatchInputFile(@Param("status") String status);
}
