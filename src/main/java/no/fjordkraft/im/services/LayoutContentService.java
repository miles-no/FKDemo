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
    LayoutContent getLayoutContentById(Long id);
    String getLayoutContentByLayoutIdandVersion(Long layoutId, Integer version);
}
