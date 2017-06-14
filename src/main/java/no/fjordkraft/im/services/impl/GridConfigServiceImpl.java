package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.GridConfig;
import no.fjordkraft.im.repository.GridConfigRepository;
import no.fjordkraft.im.services.GridConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by miles on 6/12/2017.
 */
@Service
public class GridConfigServiceImpl implements GridConfigService {

    @Autowired
    GridConfigRepository gridConfigRepository;

    @Override
    public List<GridConfig> getGridConfigs() {
        return gridConfigRepository.findAll();
    }

    @Override
    public void saveGridConfig(GridConfig gridConfig) {
        gridConfigRepository.save(gridConfig);
    }

    @Override
    public void updateGridConfig(GridConfig gridConfig) {
        GridConfig grid = gridConfigRepository.findOne(gridConfig.getId());
        grid.setBrand(gridConfig.getBrand());
        grid.setEmail(gridConfig.getEmail());
        grid.setPhone(gridConfig.getPhone());

        gridConfigRepository.saveAndFlush(gridConfig);
    }

    @Override
    public void deleteGridConfig(Long id) {
        gridConfigRepository.delete(id);
    }

    @Override
    public GridConfig getGridConfigByBrand(String brand) {
        return gridConfigRepository.getGridConfigByBrand(brand);
    }
}
