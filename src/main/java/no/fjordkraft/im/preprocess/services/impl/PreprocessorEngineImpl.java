package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.services.Preprocessor;
import no.fjordkraft.im.preprocess.services.PreprocessorEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

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
       Collections.sort(preprocessorList);
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

    public List<Preprocessor> getPreprocessorList() {
        return preprocessorList;
    }

    public void setPreprocessorList(List<Preprocessor> preprocessorList) {
        this.preprocessorList = preprocessorList;
    }


}
