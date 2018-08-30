package no.fjordkraft.im.services;

import java.util.Date;

/**
 * Created by bhavi on 8/30/2018.
 */
public interface StatementPayloadService {

    int deleteStatementPayloadTillDate(Date tillDate);
}
