package no.fjordkraft.im.task;


import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.preprocess.services.PreprocessorService;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by bhavi on 5/19/2017.
 */
@Service
@Scope("prototype")
public class PreprocessorTask implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(PreprocessorTask.class);

    Statement statement;

    @Autowired
    StatementService statementService;

    @Autowired
    PreprocessorService preprocessorService;

    public PreprocessorTask(StatementService statementService, Statement statement){
        this.statement = statement;
        this.statementService = statementService;
    }

    @Override
    @Transactional
    public void run() {
            preprocessorService.preprocess(statement);
            statement = statementService.updateStatement(statement, StatementStatusEnum.PRE_PROCESSED);
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public static Logger getLogger() {
        return logger;
    }
}