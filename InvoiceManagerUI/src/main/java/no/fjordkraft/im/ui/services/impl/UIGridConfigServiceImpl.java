package no.fjordkraft.im.ui.services.impl;


import no.fjordkraft.im.model.GridConfig;
import no.fjordkraft.im.repository.GridConfigRepository;
import no.fjordkraft.im.ui.services.UIGridConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by miles on 8/10/2017.
 */
@Service
public class UIGridConfigServiceImpl implements UIGridConfigService {

    @Autowired
    GridConfigRepository gridConfigRepository;

    @Override
    @Transactional(readOnly=true)
    public List<GridConfig> getGridConfigs() {
        return gridConfigRepository.findAll();
    }

    @Override
    @Transactional
    public void saveGridConfig(GridConfig gridConfig) {
        gridConfigRepository.save(gridConfig);
    }

    @Override
    @Transactional
    public void updateGridConfig(GridConfig gridConfig) {
        GridConfig grid = gridConfigRepository.findOne(gridConfig.getId());
        grid.setGridName(gridConfig.getGridName());
        grid.setGridLabel(gridConfig.getGridLabel());
        grid.setEmail(gridConfig.getEmail());
        grid.setPhone(gridConfig.getPhone());

        gridConfigRepository.saveAndFlush(gridConfig);
    }

    @Override
    @Transactional
    public void deleteGridConfig(Long id) {
        gridConfigRepository.delete(id);
    }

    @Override
    public Long getGridCount() {
        return gridConfigRepository.getGridCount();
    }

    @Override
    public List<String> getAllGrids() {
        return gridConfigRepository.getAllGrids();
    }

    @Override
    @Transactional(readOnly=true)
    public GridConfig getGridConfigByBrand(String brand) {
        return gridConfigRepository.getGridConfigByGrid(brand);
    }
}
