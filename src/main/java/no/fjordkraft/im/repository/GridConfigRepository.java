package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.GridConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by miles on 6/12/2017.
 */
@Repository
public interface GridConfigRepository extends JpaRepository<GridConfig, Long> {

    @Query("select g from GridConfig g where g.brand = :brand")
    GridConfig getGridConfigByBrand(@Param("brand") String brand);

    @Query("select count(g) from GridConfig g")
    Long getGridCount();

    @Query("select g.brand from GridConfig g")
    List<String> getAllBrands();
}
