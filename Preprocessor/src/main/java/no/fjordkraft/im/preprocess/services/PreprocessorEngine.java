package no.fjordkraft.im.preprocess.services;

import no.fjordkraft.im.preprocess.models.PreprocessRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Created by bhavi on 5/8/2017.
 */
public interface PreprocessorEngine {

    public void registerPreprocessor();
    public void execute(PreprocessRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException;
    public List<Preprocessor> getPreprocessorList();
    public void setPreprocessorMap(Map<Preprocessor,Boolean> preprocessorMap);
}
