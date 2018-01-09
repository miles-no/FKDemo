package no.fjordkraft.im.services;

import no.fjordkraft.im.model.CustomerDetailsView;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/8/18
 * Time: 1:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CustomerDetailsViewService {
    CustomerDetailsView findByAccountNumber(String accountNumber);
}
