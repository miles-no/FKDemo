package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.BlanketNumber;
import no.fjordkraft.im.repository.BlanketNumberRepository;
import no.fjordkraft.im.services.BlanketNumberService;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/23/18
 * Time: 1:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class BlanketNumberServiceImpl implements BlanketNumberService {

    @Autowired
    BlanketNumberRepository blanketNumberRepository;

    @Autowired
    ConfigService configService;

    private static final Logger logger = LoggerFactory.getLogger(BlanketNumberServiceImpl.class);

    @Override
    public BlanketNumber getLatestBlanketNumberByDate(Date today,boolean isActive) {
        return blanketNumberRepository.getLatestBlanketNumberByDate(isActive);  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateBlankettNumber(BlanketNumber blanketNumber) {
        blanketNumberRepository.saveAndFlush(blanketNumber);
    }

    @Override
    public void extractBlanketNumber() {
       /* Date today = new Date();
       BlanketNumber blanketNumber = blanketNumberRepository.getLatestBlanketNumberByDate(today,true);
        logger.debug("active blanket number for today " + blanketNumber);
       int validTill = configService.getInteger(IMConstants.BLANKETNUMBER_VALIDITY_PERIOD_MONTHS);
        logger.debug("validity period for blanket number is " + validTill + " months ");
        Date activationDate =null;
        int totalMonths = 0;
        if(blanketNumber!=null)  {
         activationDate = blanketNumber.getDateOfActivation();
            logger.debug("activation Date for blanket number "+blanketNumber.getBlanketNumber()+"is " + activationDate);
        }
        if(activationDate!=null) {
        LocalDate activationLocalDate = activationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate todayLocalDate = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(activationLocalDate,todayLocalDate);
        int noOfYears = period.getYears();
        int noOfMonths = period.getMonths();
         totalMonths = noOfYears*12 + noOfMonths;
         logger.debug("Total no of months for blanket number"+ blanketNumber.getBlanketNumber() +" is " + totalMonths);
        }
        if(activationDate==null && blanketNumber!=null)
        {
            logger.debug("activation date is blank " );
            blanketNumber.setActive(true);
            blanketNumber.setDateOfActivation(today);
            blanketNumberRepository.saveAndFlush(blanketNumber);
        }
        else if((validTill<totalMonths && activationDate!=null) || blanketNumber==null)
        {
            logger.debug("validity period for blanket number is  expired");
            List<BlanketNumber> listOfBlanketNumbers =  blanketNumberRepository.getInactiveBlanketNumber(false);
            if(listOfBlanketNumbers!=null && !listOfBlanketNumbers.isEmpty())
            {
            logger.debug("list of inactive blanket numbers " + listOfBlanketNumbers.size());
            BlanketNumber reactivatedBlanketNumber = listOfBlanketNumbers.get(0);

            reactivatedBlanketNumber.setActive(true);
            reactivatedBlanketNumber.setDateOfActivation(today);
            blanketNumberRepository.saveAndFlush(reactivatedBlanketNumber);
            logger.info("Reactivated blanket number is " + reactivatedBlanketNumber.getBlanketNumber());
             if(blanketNumber!=null)
             {
                blanketNumber.setActive(false);
                blanketNumber.setDateOfActivation(null);
                blanketNumberRepository.saveAndFlush(blanketNumber);
                logger.info("Deactivated blanket number is " + blanketNumber.getBlanketNumber());
             }
            }
        }*/
    }
}
