package no.fjordkraft.im.services;

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.model.StatusCount;
import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by bhavi on 5/9/2017.
 */
public interface StatementService {

   // public void splitSystemBatchInputFile();

    Statement updateStatement(Statement statement);
    List<Statement> getStatementsByStatus(StatementStatusEnum statementStatusEnum);
//    public void fetchAndSplit();

    Statement saveIMStatementinDB(File statementFile, Statement imStatement) throws IOException;
    Statement saveIMStatementinDB(String xml, Statement imStatement) throws IOException;
    Statement updateStatement(Statement statement, StatementStatusEnum status);
   // public void splitAndSave(TransferFile transferFile);

    Statement getStatement(Long statementId);
    Map<String, Integer> getStatementBySystemBatchId(Long systemBatchInputId);

    void deleteStatementBySiId(Long siId);

    int updateStatementsBySiId(Long siId,StatementStatusEnum status);
}
