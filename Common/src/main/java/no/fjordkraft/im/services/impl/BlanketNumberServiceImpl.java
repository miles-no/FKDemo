package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.BlanketNumber;
import no.fjordkraft.im.repository.BlanketNumberRepository;
import no.fjordkraft.im.services.BlanketNumberService;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.lang.time.DateUtils;
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
    public void extractBlanketNumber(Integer validityPeriod) {
        Date today = new Date(System.currentTimeMillis());
        Date tillValid =  DateUtils.addMonths(today, -validityPeriod);


        blanketNumberRepository.activeBlanketNumber(true, tillValid);
    }
}
