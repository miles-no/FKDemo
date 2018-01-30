package no.fjordkraft.im.services;

import no.fjordkraft.im.model.BlanketNumber;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/23/18
 * Time: 1:29 PM
 * To change this template use File | Settings | File Templates.
 */

public interface BlanketNumberService {

    BlanketNumber getLatestBlanketNumberByDate(Date today,boolean isActive);
    public void extractBlanketNumber();
}
