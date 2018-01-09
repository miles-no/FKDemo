package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.Config;
import no.fjordkraft.im.model.CustomerDetailsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/8/18
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface CustomerDetailsViewRepository extends JpaRepository<CustomerDetailsView, String> {
    CustomerDetailsView findByAccountNumber(String accountNumber);
}
