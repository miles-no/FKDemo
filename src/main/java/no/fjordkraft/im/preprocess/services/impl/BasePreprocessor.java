package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.preprocess.services.Preprocessor;

import javax.annotation.processing.Processor;
import java.lang.annotation.Annotation;

/**
 * Created by bhavi on 5/8/2017.
 */
public abstract class BasePreprocessor implements Preprocessor {

    protected boolean canProcess(PreprocessRequest request){
        PreprocessorInfo annotationObj1 = this.getClass().getAnnotation(PreprocessorInfo.class);
        return true;
    }

    @Override
    public int compareTo(Preprocessor preprocessor){
        PreprocessorInfo annotationObj1 = this.getClass().getAnnotation(PreprocessorInfo.class);
        Integer order1 = annotationObj1.order();
        PreprocessorInfo annotationObj2 = preprocessor.getClass().getAnnotation(PreprocessorInfo.class);
        Integer order2 = annotationObj2.order();
        return order1.compareTo(order2);
    }

}
