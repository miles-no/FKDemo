package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.TransactionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by miles on 5/23/2017.
 */
@Repository
public interface TransactionGroupRepository extends JpaRepository<TransactionGroup,Long> {

    @Query("select t from TransactionGroup t where t.name = :name")
    List<TransactionGroup> queryTransactionGroupByName(@Param("name") String name);
}
