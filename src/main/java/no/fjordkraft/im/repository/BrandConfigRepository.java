package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.BrandConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by miles on 5/18/2017.
 */
@Repository
public interface BrandConfigRepository extends JpaRepository<BrandConfig, Long> {

    @Query("select b from BrandConfig b where b.brand = :brand")
    BrandConfig getBarcodeConfigByBrand(@Param("brand") String brand);

    @Query("select b from BrandConfig b")
    List<BrandConfig> getBrandConfigs();

    @Query("select b.brand from BrandConfig b")
    List<String> getAllBrands();
}
