package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.jobs.domain.Job;
import no.fjordkraft.im.jobs.domain.JobInfo;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.services.Preprocessor;
import no.fjordkraft.im.preprocess.services.PreprocessorEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by bhavi on 5/8/2017.
 */
@Service
public class PreprocessorEngineImpl implements PreprocessorEngine {

    private static final Logger logger = LoggerFactory.getLogger(PreprocessorEngineImpl.class);

    @Autowired
    private List<Preprocessor> preprocessorList;

    @PostConstruct
    public void init() {
       // System.out.println(preprocessorList);
       Collections.sort(preprocessorList);
       System.out.println(preprocessorList);
    }

    public List<Preprocessor> getPreprocessorList() {
        return preprocessorList;
    }

    public void setPreprocessorList(List<Preprocessor> preprocessorList) {
        this.preprocessorList = preprocessorList;
    }

    @Override
    public void registerPreprocessor() {

    }

    @Override
    public void execute(PreprocessRequest request) {
          for(Preprocessor preprocessor:preprocessorList){
              preprocessor.preprocess(request);
          }
    }
}
