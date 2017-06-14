package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.LayoutConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by miles on 6/12/2017.
 */
@Repository
public interface LayoutConfigRepository extends JpaRepository<LayoutConfig, Long> {

}
