package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.BarcodeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by miles on 5/18/2017.
 */
@Repository
public interface BarcodeConfigRepository extends JpaRepository<BarcodeConfig, Long> {

    @Query("select b from BarcodeConfig b where b.brand = :brand")
    BarcodeConfig getBarcodeConfigBrand(@Param("brand") String brand);
}
