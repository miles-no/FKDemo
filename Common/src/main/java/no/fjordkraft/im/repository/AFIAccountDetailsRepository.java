package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.AFIAccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 10/5/18
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface AFIAccountDetailsRepository extends JpaRepository<AFIAccountDetails,Long> {

  @Query("select c.invoiceLayout from AFIAccountDetails c where c.accountNo= :accountID ")
  String getInvoiceLayoutBasedOnAccountDetails(@Param("accountID")String accountID);
}
