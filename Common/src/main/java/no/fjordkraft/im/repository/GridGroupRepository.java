package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.GridGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/3/18
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface GridGroupRepository  extends JpaRepository<GridGroup, Long> {

    @Query("select g from GridGroup g left join g.gridConfig c where c.gridName = :gridName")
    List<GridGroup> getGridGroupByGridConfigName(@Param("gridName") String gridName);

}

