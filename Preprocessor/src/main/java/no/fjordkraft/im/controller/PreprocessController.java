package no.fjordkraft.im.controller;

import no.fjordkraft.im.preprocess.services.impl.UtilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bhavi on 9/14/2017.
 */

@RestController
public class PreprocessController {

    private static final Logger logger = LoggerFactory.getLogger(PreprocessController.class);

    @Autowired
    UtilityService utilityService;

    @RequestMapping(value = "/preprocess",method= RequestMethod.POST)
    public void preprocessXml(@RequestParam("srcpath") String srcpath,@RequestParam(value = "destpath") String destPath){
        try {
            utilityService.decodeEHFE2B(srcpath,destPath);
        } catch (Exception e) {
            logger.debug("exception ",e);
        }
    }
}
