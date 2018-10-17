package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.AFIAccountDetailsService;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 10/9/18
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
@PreprocessorInfo(order=12,legalPartClass = "Organization")
public class FetchInvoiceLayoutPreprocessor extends BasePreprocessor {

private static final Logger logger = LoggerFactory.getLogger(FetchInvoiceLayoutPreprocessor.class);

    @Autowired
    AFIAccountDetailsService afiAccountDetailsService;

    @Autowired
    StatementService statementService;


    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request)
    {
        try
        {
            String brand =  request.getEntity().getSystemBatchInput().getBrand();
            if(request.getStatement().getLegalPartClass().equals("Organization") && (brand.equals("FKAS") || brand.equals("TKAS")))
            {
                 //IM-169 : If AFI_ACCOUNTDETAILS.INVOICE_LAYOUT = DEFAULT or null, then it should show details and summary page.
                // If AFI_ACCOUNTDETAILS.INVOICE_LAYOUT = WITHOUT_DETAILS, then it should show only the summary page.
                String invoiceLayout =  afiAccountDetailsService.getInvoiceLayoutBasedOnAccountDetails(request.getEntity().getAccountNumber());
                logger.info("Getting Invoice Layout information from AFI ACCOUNTDETAILS for account number " + request.getEntity().getAccountNumber());

                if(invoiceLayout==null || (invoiceLayout!=null && !invoiceLayout.isEmpty() && IMConstants.INVOICE_LAYOUT_DEFAULT.equals(invoiceLayout)))
                {
                    invoiceLayout = IMConstants.INVOICE_LAYOUT_DEFAULT;
                    logger.debug("Invoice layout for invoice number " + request.getEntity().getInvoiceNumber() + " is " + invoiceLayout);
                    request.getStatement().setShowDetailsPage(true);
                }
                else
                {
                    logger.debug("Invoice layout for invoice number " +  request.getEntity().getInvoiceNumber() + " is " + invoiceLayout);
                    request.getStatement().setShowDetailsPage(false);
                }
                request.getEntity().setAccountInvoiceLayout(invoiceLayout);
               // statementService.updateStatement(request.getEntity());

            }
        }catch (Exception e)
        {
            e.printStackTrace();
            logger.error("Exception in Fetch Invoice Layout preprocessor",e);
            throw new PreprocessorException(e);
        }
    }

}
