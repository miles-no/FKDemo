package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.GridConfig;
import no.fjordkraft.im.repository.GridConfigRepository;
import no.fjordkraft.im.services.GridConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by miles on 6/12/2017.
 */
@Service
public class GridConfigServiceImpl implements GridConfigService {

    @Autowired
    GridConfigRepository gridConfigRepository;

    @Override
    @Transactional(readOnly=true)
    public GridConfig getGridConfigByBrand(String brand) {
        return gridConfigRepository.getGridConfigByGrid(brand);
    }

}
