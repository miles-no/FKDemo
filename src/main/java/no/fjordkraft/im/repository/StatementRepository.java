package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.model.StatusCount;
import no.fjordkraft.im.model.SystemBatchInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by miles on 5/4/2017.
 */
@Repository("imStatementRepository")
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
}
