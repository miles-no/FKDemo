package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.model.StatusCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by miles on 5/4/2017.
 */
@Repository
public interface StatementRepository extends JpaRepository<Statement,Long> {

    @Query("select s from Statement s where rownum <= 35 and s.status = :status order by s.createTime asc")
    List<Statement> readStatements(@Param("status") String status);

    @Query("select s from Statement s where s.statementId = :statementOcr")
    Statement readStatementByStatementOCR(@Param("statementOcr") String statementOcr);

    @Query("select s from Statement s where rownum <= :rownum and s.status = :status order by s.createTime asc")
    List<Statement> readStatements(@Param("rownum") Long rownum, @Param("status") String status);

    //@Query("select s.status as status, count(s.status) as count from Statement s group by s.status")
    @Query(value = "select new no.fjordkraft.im.model.StatusCount(s.status, count(s)) from Statement s group by s.status")
    List<StatusCount> getStatementStatus();

    @Query(value = "select new no.fjordkraft.im.model.StatusCount(s.city, count(s)) from Statement s where createTime between :fromTime and :toTime group by s.city")
    List<StatusCount> getStatusByCity(@Param("fromTime") Timestamp fromTime, @Param("toTime") Timestamp toTime);

    @Query(value = "select new no.fjordkraft.im.model.StatusCount(s.systemBatchInput.transferFile.brand, count(s)) FROM " +
            "Statement s JOIN s.systemBatchInput where s.createTime between :fromTime and :toTime GROUP BY s.systemBatchInput.transferFile.brand")
    List<StatusCount> getStatusByBrand(@Param("fromTime") Timestamp fromTime, @Param("toTime") Timestamp toTime);

    @Query("select count(s) from Statement s where createTime between :fromTime and :toTime")
    Long getInvoiceCountByTime(@Param("fromTime") Timestamp fromTime, @Param("toTime") Timestamp toTime);

    @Query("select count(s) from Statement s")
    Long getTotalInvoiceCount();

    @Query("select new no.fjordkraft.im.model.StatusCount(s.status, count(s)) from Statement s where s.systemBatchInput.id = :sbiId group by s.status")
    List<StatusCount> getStatementBySbiId(@Param("sbiId") Long sbiId);
}
