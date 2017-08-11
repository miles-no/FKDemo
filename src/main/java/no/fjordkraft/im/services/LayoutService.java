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

    String getRptDesignFile(Long id);
}
