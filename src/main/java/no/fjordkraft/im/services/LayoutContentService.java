package no.fjordkraft.im.services;

import no.fjordkraft.im.model.LayoutContent;

import java.io.InputStream;

/**
 * Created by miles on 6/30/2017.
 */
public interface LayoutContentService {

    LayoutContent getLayoutContentByLayoutId(Long id, boolean active);
    void saveLayoutContent(Long layoutId, String file);
    void updateLayoutContent(Long id, String file);
    void activateLayoutTemplate(Long layoutId, Integer version);
    LayoutContent getLayoutContentById(Long id);
    String getLayoutContentByLayoutIdandVersion(Long layoutId, Integer version);
}
