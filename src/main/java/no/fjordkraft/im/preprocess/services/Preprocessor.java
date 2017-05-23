package no.fjordkraft.im.preprocess.services;

import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by bhavi on 5/8/2017.
 */
public interface Preprocessor extends Comparable<Preprocessor> {

    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request);
}
