package no.fjordkraft.im.services;

import no.fjordkraft.im.domain.RestLayout;
import no.fjordkraft.im.model.Layout;

import java.util.List;

/**
 * Created by miles on 6/27/2017.
 */
public interface LayoutService {

    List<Layout> getAllLayoutDesign();
    Layout getLayoutDesignById(Long id);
    void saveLayoutDesign(Layout layout);
    void updateLayoutDesign(Layout layout);
    void deleteLayoutDesign(Long id);
    String getRptDesignFile(Long id);
    List<RestLayout> getLayoutByBrand(String brand);
}
