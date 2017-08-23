package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.LayoutContent;
import no.fjordkraft.im.repository.LayoutRepository;
import no.fjordkraft.im.services.LayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by miles on 6/27/2017.
 */
@Service
public class LayoutServiceImpl implements LayoutService {

    @Autowired
    LayoutRepository layoutRepository;

    @Autowired
    LayoutContentServiceImpl layoutContentService;

    @Autowired
    LayoutRuleServiceImpl layoutRuleService;

    @Override
    public String getRptDesignFile(Long layoutId) {
        LayoutContent layoutContent = layoutContentService.getLayoutContentByLayoutId(layoutId, Boolean.TRUE);
        if(null != layoutContent && null != layoutContent.getFileContent()) {
            return layoutContent.getFileContent();
        }
        return null;
    }
}
