package no.fjordkraft.im.services;

import no.fjordkraft.im.domain.NameValuePair;
import no.fjordkraft.im.domain.RestLayout;
import no.fjordkraft.im.domain.RestLayoutTemplate;
import no.fjordkraft.im.model.Layout;

import java.util.List;

/**
 * Created by miles on 8/10/2017.
 */
public interface UILayoutService {

    Layout saveLayout(RestLayoutTemplate restLayoutTemplate);
    List<RestLayout> getLayoutByBrand(String brand);
    List<RestLayout> getAllLayout();
    List<NameValuePair> getLayoutList();
    void deleteLayout(Long id);
    Layout updateLayoutVersion(Long id, String template);
    Layout updateLayout(Long id, RestLayoutTemplate restLayoutTemplate);
    Layout getLayoutDesignById(Long id);
}
