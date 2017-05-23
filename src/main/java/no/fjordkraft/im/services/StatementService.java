package no.fjordkraft.im.services;

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
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

}
