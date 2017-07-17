package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.LayoutContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by miles on 6/30/2017.
 */
@Repository
public interface LayoutContentRepository extends JpaRepository<LayoutContent, Long> {

    @Query("select c from LayoutContent c where c.layoutId = :layoutId and c.active = :active order by c.version desc")
    List<LayoutContent> getLayoutContentById(@Param("layoutId") Long layoutId, @Param("active") boolean active);

    @Query("select c from LayoutContent c where c.layoutId = :layoutId order by c.version desc")
    List<LayoutContent> getLayoutContentById(@Param("layoutId") Long layoutId);

    @Query(value = "select c.version from LayoutContent c where c.layoutId = :layoutId")
    List<Integer> getVersionNumbersForLayout(@Param("layoutId") Long layoutId);

    @Query("select c from LayoutContent c where c.layoutId = :layoutId and c.version = :version")
    LayoutContent getLayoutContentByIdAndVersion(@Param("layoutId") Long layoutId,
                                                 @Param("version") Integer version);

    @Query("select c from LayoutContent c where c.layoutId = :layoutId")
    LayoutContent getLayoutContentByLayoutId(@Param("layoutId") Long layoutId);
}
