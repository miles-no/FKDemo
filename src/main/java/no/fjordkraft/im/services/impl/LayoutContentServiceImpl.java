package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.LayoutContent;
import no.fjordkraft.im.model.LayoutRule;
import no.fjordkraft.im.repository.LayoutContentRepository;
import no.fjordkraft.im.services.LayoutContentService;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Collections;

/**
 * Created by miles on 6/30/2017.
 */
@Service
public class LayoutContentServiceImpl implements LayoutContentService {

    @Autowired
    LayoutContentRepository layoutContentRepository;

    @Autowired
    LayoutServiceImpl layoutService;

    @Override
    public LayoutContent getLayoutContentByLayoutId(Long layoutId, boolean active) {
        List<LayoutContent> layoutContentList = layoutContentRepository.getLayoutContentById(layoutId, active);
        if(null != layoutContentList && IMConstants.ZERO != layoutContentList.size()) {
            return layoutContentList.get(0);
        }
        return null;
    }

    @Override
    public LayoutContent getLayoutContentByLayoutId(Long layoutId) {
        List<LayoutContent> layoutContentList = layoutContentRepository.getLayoutContentById(layoutId);
        if(null != layoutContentList && IMConstants.ZERO != layoutContentList.size()) {
            return layoutContentList.get(0);
        }
        return null;
    }

    @Override
    public LayoutContent getLayoutContentById(Long id) {
        return layoutContentRepository.findOne(id);
    }

    @Override
    public String getLayoutContentByLayoutIdandVersion(Long layoutId, Integer version) {
        LayoutContent layoutContent = layoutContentRepository.getLayoutContentByIdAndVersion(layoutId, version);
        return layoutContent.getFileContent();
    }
}
