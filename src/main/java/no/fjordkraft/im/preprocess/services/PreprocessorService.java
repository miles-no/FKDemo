package no.fjordkraft.im.preprocess.services;

import no.fjordkraft.im.if320.models.Statement;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bhavi on 5/8/2017.
 */
public interface PreprocessorService {

    public Statement unmarshallStatement(String path);

    public Statement unmarshallStatement(InputStream inputStream);

    public void preprocess() throws IOException;
}
