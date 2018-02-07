package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.model.BlanketNumber;
import no.fjordkraft.im.model.CustomerDetailsView;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.AuditLogService;
import no.fjordkraft.im.services.BlanketNumberService;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.services.CustomerDetailsViewService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/8/18
 * Time: 1:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
@PreprocessorInfo(order = 15)
public class SetGiroPreprocessor extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(SetGiroPreprocessor.class);
    @Autowired
    CustomerDetailsViewService customerDetailsViewService;

    @Autowired
    BlanketNumberService blanketNumberService;

    @Autowired
    ConfigService configService;

    @Autowired
    AuditLogService auditLogService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
         long accountNumber = request.getStatement().getAccountNumber();
        CustomerDetailsView customerDetailsView =customerDetailsViewService.findByAccountNumber(Long.toString(accountNumber));

        if(customerDetailsView!=null && customerDetailsView.getGiroEnabled())
        {
            String message = "Giro is enabled for account number "+ accountNumber;
            auditLogService.saveAuditLog(request.getEntity().getId(), StatementStatusEnum.PRE_PROCESSING.getStatus(),message,IMConstants.INFO);
            logger.debug("GIRO is enabled for account number " + Long.toString(accountNumber));
            request.getStatement().setGIROEnabled(true);

            logger.debug("Get latest blanket number");
            int validTill = configService.getInteger(IMConstants.BLANKETNUMBER_VALIDITY_PERIOD_MONTHS);
            logger.debug("Validity period is "+ validTill + " months ");
            BlanketNumber blanketNumber = blanketNumberService.getLatestBlanketNumberByDate(new Date(),true);

            if(blanketNumber!=null && getMonths(blanketNumber.getDateOfActivation(),new Date())<validTill)
            {
                  logger.debug("Blanket Number is valid as date of activation is " + blanketNumber.getDateOfActivation());

                  request.getStatement().setBlanketNumber(blanketNumber.getBlanketNumber());

            }
            else
            {

            }
            if(blanketNumber!=null)
            {
               request.getStatement().setBlanketNumber(blanketNumber.getBlanketNumber());
            }
           float openClaim =  request.getStatement().getTotalOpenClaim();
            String claim= Float.toString(openClaim) ;
            String totalclaim=claim.substring(0,claim.indexOf("."))+claim.substring(claim.indexOf(".")+1,claim.length());

            logger.debug("Total Open Claim " + openClaim + "For account number " + request.getStatement().getAccountNumber());
            String checksum = calculateCheckDigit(totalclaim);
             if(checksum!=null)
             {
                 logger.debug("Check sum value is " + checksum);
                 request.getStatement().setCheckSum(checksum);
             }
        }
        else
        {
            String message = "Giro is disabled for account number "+ accountNumber;
           // auditLogService.saveAuditLog(request.getEntity().getId(), StatementStatusEnum.PRE_PROCESSING.getStatus(),message,IMConstants.WARNING);
            logger.debug("GIRO is disabled for account number " + Long.toString(accountNumber));
            request.getStatement().setGIROEnabled(false);
        }
    }

    private long getMonths(Date startDate, Date endDate){
        Calendar sDate = Calendar.getInstance();
        Calendar eDate = Calendar.getInstance();
        sDate.setTime(startDate);
        eDate.setTime(endDate);
        int noOfMonths = sDate.get(Calendar.MONTH) - eDate.get(Calendar.MONTH);
        int noOfYear = sDate.get(Calendar.YEAR) - eDate.get(Calendar.YEAR);
        noOfMonths = (noOfYear*(12-sDate.get(Calendar.MONTH)))+noOfMonths;
        return noOfMonths;
    }

    private  String calculateCheckDigit(String totalClaim) {
        if (totalClaim == null)
            return null;
        String digit;
		/* convert to array of int for simplicity */
        totalClaim = totalClaim.replaceAll("-","");
        int[] digits = new int[totalClaim.length()];
        for (int i = 0; i < totalClaim.length(); i++) {
            digits[i] = Character.getNumericValue(totalClaim.charAt(i));
        }

		/* double every other starting from right - jumping from 2 in 2 */
        for (int i = digits.length - 1; i >= 0; i -= 2)	{
            digits[i] += digits[i];

			/* taking the sum of digits grater than 10 - simple trick by substract 9 */
            if (digits[i] >= 10) {
                digits[i] = digits[i] - 9;
            }
        }
        int sum = 0;
        for (int i = 0; i < digits.length; i++) {
            sum += digits[i];
        }
		/* multiply by 9 step */
        sum = sum * 9;

		/* convert to string to be easier to take the last digit */
        digit = sum + "";
        return digit.substring(digit.length() - 1);
    }

}
