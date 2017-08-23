package no.fjordkraft.im.task;


import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.preprocess.services.PreprocessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by bhavi on 5/19/2017.
 */
@Service
@Scope("prototype")
public class PreprocessorTask implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(PreprocessorTask.class);

    Statement statement;

    @Autowired
    PreprocessorService preprocessorService;

    public PreprocessorTask(Statement statement){
        this.statement = statement;
    }

    @Override
    public void run() {
            preprocessorService.preprocess(statement);
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }
}