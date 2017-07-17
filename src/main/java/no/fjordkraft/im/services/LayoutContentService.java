package no.fjordkraft.im.services;

import no.fjordkraft.im.model.LayoutContent;

import java.io.InputStream;
import java.util.List;

/**
 * Created by miles on 6/30/2017.
 */
public interface LayoutContentService {

    LayoutContent getLayoutContentByLayoutId(Long id, boolean active);
    LayoutContent getLayoutContentByLayoutId(Long id);
    void saveLayoutContent(Long layoutId, String file);
    void updateLayoutContent(Long id, String file);
    void activateLayoutTemplate(Long layoutId, Integer version);
    LayoutContent getLayoutContentById(Long id);
    String getLayoutContentByLayoutIdandVersion(Long layoutId, Integer version);
    void deleteLayoutContent(Long id);
    void deActivateLayoutTemplate(Long layoutId, Integer version);
    List<LayoutContent> getAllLayoutContentByLayoutId(Long layoutId);
    void updateLayoutVersion(Long id, String template);
}
