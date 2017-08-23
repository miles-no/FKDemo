package no.fjordkraft.im.services;

import no.fjordkraft.im.model.LayoutContent;

import java.util.List;

/**
 * Created by miles on 8/10/2017.
 */
public interface UILayoutContentService {

    void activateLayoutTemplate(Long layoutId, Integer version);
    void deActivateLayoutTemplate(Long layoutId, Integer version);
    LayoutContent getLayoutContentByLayoutId(Long id);
    void saveLayoutContent(Long layoutId, String file);
    void updateLayoutContent(Long id, String file);
    void updateLayoutVersion(Long id, String template);
    void deleteLayoutContent(Long id);
    List<LayoutContent> getAllLayoutContentByLayoutId(Long layoutId);
}
