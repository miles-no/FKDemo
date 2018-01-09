package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.CustomerDetailsView;
import no.fjordkraft.im.repository.CustomerDetailsViewRepository;
import no.fjordkraft.im.services.CustomerDetailsViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/8/18
 * Time: 1:04 PM
 * To change this template use File | Settings | File Templates.
 */
  @Service
public class CustomerDetailsViewServiceImpl implements CustomerDetailsViewService {

    @Autowired
    CustomerDetailsViewRepository customerDetailsViewRepository;

    @Override
    public CustomerDetailsView findByAccountNumber(String accountNumber) {
        return customerDetailsViewRepository.findByAccountNumber(accountNumber);
    }
}
