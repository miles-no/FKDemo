package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.RuleAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by miles on 6/27/2017.
 */
@Repository
public interface RuleAttributesRepository extends JpaRepository<RuleAttributes,Long> {

    @Query("select r from RuleAttributes r where r.name = :name")
    RuleAttributes getAttributeByName(@Param("name") String name);
}
