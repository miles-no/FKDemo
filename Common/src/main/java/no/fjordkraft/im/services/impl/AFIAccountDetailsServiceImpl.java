package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.AFIAccountDetails;
import no.fjordkraft.im.repository.AFIAccountDetailsRepository;
import no.fjordkraft.im.services.AFIAccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 10/5/18
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class AFIAccountDetailsServiceImpl implements AFIAccountDetailsService {

    @Autowired
    AFIAccountDetailsRepository afiAccountDetailsRepository;


    @Override
    public String getInvoiceLayoutBasedOnAccountDetails(String accountID) {

        return afiAccountDetailsRepository.getInvoiceLayoutBasedOnAccountDetails(accountID);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
