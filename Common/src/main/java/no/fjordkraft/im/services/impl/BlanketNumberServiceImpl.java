package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.BlanketNumber;
import no.fjordkraft.im.repository.BlanketNumberRepository;
import no.fjordkraft.im.services.BlanketNumberService;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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



    @Override
    public BlanketNumber getLatestBlanketNumberByDate(Date today,boolean isActive) {
        return blanketNumberRepository.getLatestBlanketNumberByDate(today,isActive);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void extractBlanketNumber() {
        Date today = new Date();
       BlanketNumber blanketNumber = blanketNumberRepository.getLatestBlanketNumberByDate(today,true);
       int validTill = configService.getInteger(IMConstants.BLANKETNUMBER_VALIDITY_PERIOD_MONTHS);
        Date activationDate =null;
        int totalMonths = 0;
        if(blanketNumber!=null)  {
         activationDate = blanketNumber.getDateOfActivation();
        }
        if(activationDate!=null) {
        LocalDate activationLocalDate = activationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate todayLocalDate = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(activationLocalDate,todayLocalDate);
        int noOfYears = period.getYears();
        int noOfMonths = period.getMonths();
         totalMonths = noOfYears*12 + noOfMonths;
        }
        if(activationDate==null && blanketNumber!=null)
        {
            blanketNumber.setActive(true);
            blanketNumber.setDateOfActivation(today);
            blanketNumberRepository.saveAndFlush(blanketNumber);
        }
        else if((validTill<totalMonths && activationDate!=null) || blanketNumber==null)
        {
            List<BlanketNumber> listOfBlanketNumbers =  blanketNumberRepository.getInactiveBlanketNumber(false);
            if(listOfBlanketNumbers!=null && !listOfBlanketNumbers.isEmpty())
            {
            BlanketNumber reactivatedBlanketNumber = listOfBlanketNumbers.get(0);
            reactivatedBlanketNumber.setActive(true);
            reactivatedBlanketNumber.setDateOfActivation(today);
            blanketNumberRepository.saveAndFlush(reactivatedBlanketNumber);
             if(blanketNumber!=null)
             {
                blanketNumber.setActive(false);
                blanketNumber.setDateOfActivation(null);
                blanketNumberRepository.saveAndFlush(blanketNumber);
             }
            }
        }
    }
}
