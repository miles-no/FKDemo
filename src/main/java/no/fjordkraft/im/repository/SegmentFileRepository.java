package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.SegmentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by miles on 6/21/2017.
 */
@Repository
public interface SegmentFileRepository extends JpaRepository<SegmentFile,Long> {

}
