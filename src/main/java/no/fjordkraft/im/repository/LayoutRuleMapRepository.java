package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.LayoutRuleMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by miles on 6/27/2017.
 */
@Repository
public interface LayoutRuleMapRepository extends JpaRepository<LayoutRuleMap, Long>{
}
