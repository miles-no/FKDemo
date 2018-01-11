package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.model.CustomerDetailsView;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.CustomerDetailsViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(SetGiroPreprocessor.class);
   // @Autowired
    //CustomerDetailsViewService customerDetailsViewService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
         /*long accountNumber = request.getStatement().getAccountNumber();
        CustomerDetailsView customerDetailsView =customerDetailsViewService.findByAccountNumber(Long.toString(accountNumber));
        if(customerDetailsView!=null && customerDetailsView.getGiroEnabled())
        {
            logger.debug("GIRO is enabled for account number " + Long.toString(accountNumber));
            request.getStatement().setGIROEnabled(true);
        }
        else
        {
            logger.debug("GIRO is disabled for account number " + Long.toString(accountNumber));
            request.getStatement().setGIROEnabled(false);
        }*/
    }
}
