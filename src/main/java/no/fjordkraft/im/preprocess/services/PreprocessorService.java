package no.fjordkraft.im.preprocess.services;

import no.fjordkraft.im.if320.models.Statement;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bhavi on 5/8/2017.
 */
public interface PreprocessorService {

    public Statement unmarshallStatement(String path) throws IOException;

    public Statement unmarshallStatement(InputStream inputStream) throws IOException;

    public void preprocess(no.fjordkraft.im.model.Statement statement);

    public void preprocess() throws IOException;

    //public void updateStatementEntity(no.fjordkraft.im.model.Statement statementEntity);
}
