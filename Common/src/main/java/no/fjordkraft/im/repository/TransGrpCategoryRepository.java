package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.TransactionGroupCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by miles on 9/25/2017.
 */
@Repository
public interface TransGrpCategoryRepository extends JpaRepository <TransactionGroupCategory,Long> {

}
