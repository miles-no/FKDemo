package no.fjordkraft.im.ui.services.impl;

import no.fjordkraft.im.model.LayoutContent;
import no.fjordkraft.im.repository.LayoutContentRepository;
import no.fjordkraft.im.ui.services.UILayoutContentService;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by miles on 8/10/2017.
 */
@Service
public class UILayoutContentServiceImpl implements UILayoutContentService {

    @Autowired
    LayoutContentRepository layoutContentRepository;

    @Override
    public void activateLayoutTemplate(Long layoutId, Integer version) {
        List<LayoutContent> layoutContentList = layoutContentRepository.getLayoutContentById(layoutId);
        for(LayoutContent layoutContent:layoutContentList) {
            layoutContent.setActive(Boolean.FALSE);
            layoutContentRepository.saveAndFlush(layoutContent);
        }
        LayoutContent layoutContent = layoutContentRepository.getLayoutContentByIdAndVersion(layoutId, version);
        layoutContent.setActive(Boolean.TRUE);
        layoutContentRepository.saveAndFlush(layoutContent);
    }

    @Override
    public void deActivateLayoutTemplate(Long layoutId, Integer version) {
        LayoutContent layoutContent = layoutContentRepository.getLayoutContentByIdAndVersion(layoutId, version);
        layoutContent.setActive(Boolean.FALSE);
        layoutContentRepository.saveAndFlush(layoutContent);
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
        LayoutContent layoutContent = new LayoutContent();
        layoutContent.setLayoutId(layoutId);
        layoutContent.setFileContent(file);
        layoutContent.setVersion(IMConstants.ONE);
        layoutContent.setActive(Boolean.FALSE);
        layoutContentRepository.saveAndFlush(layoutContent);
    }

    @Override
    public void updateLayoutContent(Long layoutId, String file) {
        LayoutContent layoutContent = layoutContentRepository.getLayoutContentByLayoutId(layoutId);
        layoutContent.setFileContent(file);
        layoutContent.setActive(Boolean.FALSE);
        layoutContentRepository.saveAndFlush(layoutContent);
    }

    @Override
    public void updateLayoutVersion(Long id, String template) {
        List<Integer> versionList = layoutContentRepository.getVersionNumbersForLayout(id);
        Integer version;
        if(null != versionList && IMConstants.ZERO != versionList.size()) {
            version = Collections.max(versionList);
        } else {
            version = 0;
        }
        LayoutContent layoutContent = new LayoutContent();
        layoutContent.setLayoutId(id);
        layoutContent.setFileContent(template);
        layoutContent.setVersion(++version);
        layoutContent.setActive(Boolean.FALSE);
        layoutContentRepository.saveAndFlush(layoutContent);
    }

    @Override
    public void deleteLayoutContent(Long id) {
        layoutContentRepository.delete(id);
    }

    @Override
    public List<LayoutContent> getAllLayoutContentByLayoutId(Long layoutId) {
        return layoutContentRepository.getLayoutContentById(layoutId);
    }
}
