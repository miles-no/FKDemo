package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.SegmentControlFileResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by miles on 6/21/2017.
 */
@Repository
public interface SegmentControlFileRepository extends JpaRepository<SegmentControlFileResult,Long>  {

    @Query("select s from SegmentControlFileResult s where s.accountNo = :accountNo and " +
            "s.fromDate <= to_date(:currentDate,'DD-MON-RR') " +
            "order by s.fromDate desc")
    List<SegmentControlFileResult> getSegmentControlFileByAccountNo(@Param("accountNo") String accountNo,
                                                              @Param("currentDate") String currentDate);

    @Query("select s from SegmentControlFileResult s where s.brand = :brand and s.accountNo is null and " +
            "s.fromDate <= to_date(:currentDate,'DD-MON-RR') order by s.fromDate desc")
    List<SegmentControlFileResult> getSegmentControlFileByBrand(@Param("brand") String brand,
                                                          @Param("currentDate") String currentDate);
}
