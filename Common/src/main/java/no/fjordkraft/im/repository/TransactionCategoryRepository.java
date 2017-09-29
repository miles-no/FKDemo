package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by miles on 9/26/2017.
 */
public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory,Long> {

    @Query("select c from TransactionCategory c where c.type = :type")
    List<TransactionCategory> getTransactionCategoryByType(@Param("type") String type);
}
