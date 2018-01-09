package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.model.CustomerDetailsView;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.CustomerDetailsViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/8/18
 * Time: 1:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
@PreprocessorInfo(order = 14)
public class SetGiroPreprocessor extends BasePreprocessor {


    @Autowired
    CustomerDetailsViewService customerDetailsViewService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
         long accountNumber = request.getStatement().getAccountNumber();
        CustomerDetailsView customerDetailsView =customerDetailsViewService.findByAccountNumber(Long.toString(accountNumber));
        if(customerDetailsView!=null && customerDetailsView.getGiroEnabled())
        {
            request.getStatement().setGIROEnabled(true);
        }
        else
        {
            request.getStatement().setGIROEnabled(false);
        }
    }
}
