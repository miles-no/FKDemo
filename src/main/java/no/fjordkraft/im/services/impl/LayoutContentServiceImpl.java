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
    public void saveLayoutContent(Long layoutId, String file) {
        List<Integer> versionList = layoutContentRepository.getLatestVersionNumber(layoutId);
        Integer version;
        if(null != versionList && IMConstants.ZERO != versionList.size()) {
            version = Collections.max(versionList);
        } else {
            version = 0;
        }
        LayoutContent layoutContent = new LayoutContent();
        layoutContent.setLayoutId(layoutId);
        layoutContent.setFileContent(file);
        layoutContent.setVersion(++version);
        layoutContent.setActive(Boolean.FALSE);
        layoutContentRepository.saveAndFlush(layoutContent);
    }

    @Override
    public void updateLayoutContent(Long layoutId, String file) {
        LayoutContent layoutContent = layoutContentRepository.getLayoutContentByLayoutId(layoutId);
        layoutContent.setFileContent(file);
        layoutContentRepository.saveAndFlush(layoutContent);
    }

    @Override
    public void activateLayoutTemplate(Long layoutId, Integer version) {
        LayoutContent layoutContent = layoutContentRepository.getLayoutContentByIdAndVersion(layoutId, version);
        layoutContent.setActive(Boolean.TRUE);
        layoutContentRepository.saveAndFlush(layoutContent);
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
