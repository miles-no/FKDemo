package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.LayoutConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by miles on 6/12/2017.
 */
@Repository
public interface LayoutConfigRepository extends JpaRepository<LayoutConfig, Long> {

    @Query("select b from LayoutConfig b where b.brand = :brand")
    public LayoutConfig getLayoutConfigByBrand(@Param("brand") String brand);
}
