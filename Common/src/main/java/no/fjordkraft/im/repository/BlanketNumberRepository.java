package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.BlanketNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/23/18
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface BlanketNumberRepository extends JpaRepository<BlanketNumber,Long> {

    @Query("select b from BlanketNumber b where b.dateOfActivation <=:date and b.isActive = :active ")
    BlanketNumber getLatestBlanketNumberByDate(@Param("date")Date today, @Param("active") Boolean isActive);

    @Query("select b from BlanketNumber b where b.isActive = :active or b.isActive is null")
    List<BlanketNumber> getInactiveBlanketNumber(@Param("active") Boolean isActive);

}
