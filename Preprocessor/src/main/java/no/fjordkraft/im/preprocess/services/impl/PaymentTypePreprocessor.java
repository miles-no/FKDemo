package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by miles on 7/10/2017.
 */
@Service
@PreprocessorInfo(order=10)
public class PaymentTypePreprocessor extends BasePreprocessor {

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        String paymentType = request.getStatement().getPaymentType();
        String paymentTypeStatus = request.getStatement().getPaymentTypeStatus();

        if(IMConstants.UNDEFINED.equals(paymentType)) {
            request.getStatement().setDirectDebit(IMConstants.NO);
        } else if(IMConstants.DIRECT_DEBIT.equals(paymentType) && IMConstants.GRANTED.equals(paymentTypeStatus)) {
            request.getStatement().setDirectDebit(IMConstants.YES);
        } else if(IMConstants.DIRECT_DEBIT.equals(paymentType) && IMConstants.CANCELLED.equals(paymentTypeStatus)) {
            request.getStatement().setDirectDebit(IMConstants.NO);
        } else if(IMConstants.DIRECT_DEBIT.equals(paymentType) && IMConstants.INTERNALLY_GRANTED.equals(paymentTypeStatus)) {
            request.getStatement().setDirectDebit(IMConstants.YES);
        } else {
            request.getStatement().setDirectDebit(IMConstants.NO);
        }
    }
}
