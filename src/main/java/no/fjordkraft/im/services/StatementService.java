package no.fjordkraft.im.services;

import no.fjordkraft.im.domain.RestStatement;
import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by bhavi on 5/9/2017.
 */
public interface StatementService {

    public void splitSystemBatchInputFile();

    public void updateStatement(Statement statement);

    public List<Statement> getStatementsByStatus(StatementStatusEnum statementStatusEnum);

    public void fetchAndSplit();

    public void splitAndSave(SystemBatchInput systemBatchInput);

    void saveIMStatementinDB(File statementFile, Statement imStatement) throws IOException;

    public void saveIMStatementinDB(String xml, Statement imStatement) throws IOException;

    public void updateStatement(Statement statement, StatementStatusEnum status);

    public List<RestStatement> getDetails(int page, int size, String status, Timestamp fromTime, Timestamp toTime,
                                          String brand, String customerID, String invoice);

    public Long getCountByStatus(String status);

}
