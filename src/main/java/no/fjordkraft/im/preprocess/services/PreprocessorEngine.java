package no.fjordkraft.im.preprocess.services;

import no.fjordkraft.im.preprocess.models.PreprocessRequest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by bhavi on 5/8/2017.
 */
public interface PreprocessorEngine {

    public void registerPreprocessor();
    public void execute(PreprocessRequest request);
}
