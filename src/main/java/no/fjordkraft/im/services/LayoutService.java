package no.fjordkraft.im.services;

import no.fjordkraft.im.domain.NameValuePair;
import no.fjordkraft.im.domain.RestLayout;
import no.fjordkraft.im.domain.RestLayoutTemplate;
import no.fjordkraft.im.model.Layout;
import no.fjordkraft.im.model.LayoutContent;

import java.util.List;

/**
 * Created by miles on 6/27/2017.
 */
public interface LayoutService {

    Layout getLayoutDesignById(Long id);
    String getRptDesignFile(Long id);
    Layout saveLayout(RestLayoutTemplate restLayoutTemplate);
    Layout updateLayout(Long id, RestLayoutTemplate restLayoutTemplate);
    List<RestLayout> getLayoutByBrand(String brand);
    List<RestLayout> getAllLayout();
    List<NameValuePair> getLayoutList();
    void deleteLayout(Long id);
    Layout updateLayoutVersion(Long id, String template);
}
