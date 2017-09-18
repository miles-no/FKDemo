package no.fjordkraft.im.services;

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by bhavi on 5/9/2017.
 */
public interface StatementService {

   // public void splitSystemBatchInputFile();

    public Statement updateStatement(Statement statement);

    public List<Statement> getStatementsByStatus(StatementStatusEnum statementStatusEnum);

//    public void fetchAndSplit();

    Statement saveIMStatementinDB(File statementFile, Statement imStatement) throws IOException;

    public Statement saveIMStatementinDB(String xml, Statement imStatement) throws IOException;

    public Statement updateStatement(Statement statement, StatementStatusEnum status);

   // public void splitAndSave(TransferFile transferFile);

    public Statement getStatement(Long statementId);

}
