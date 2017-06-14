package no.fjordkraft.im.controller;

import no.fjordkraft.im.model.GridConfig;
import no.fjordkraft.im.services.impl.GridConfigServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miles on 6/12/2017.
 */
@RestController
@RequestMapping("/grid/config")
public class IMGridController {
    
    @Autowired
    GridConfigServiceImpl gridConfigService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    List<GridConfig> getGridConfigs() {
        List<GridConfig> gridConfigs = new ArrayList<GridConfig>();
        gridConfigs = gridConfigService.getGridConfigs();
        return gridConfigs;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    List<GridConfig> saveGridConfig(@RequestBody GridConfig gridConfig) {
        List<GridConfig> gridConfigList = new ArrayList<>();
        gridConfigService.saveGridConfig(gridConfig);
        gridConfigList = getGridConfigs();
        return gridConfigList;
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    List<GridConfig> updateGridConfig(@RequestBody GridConfig gridConfig) {
        List<GridConfig> gridConfigList = new ArrayList<>();
        gridConfigService.updateGridConfig(gridConfig);
        gridConfigList = getGridConfigs();
        return gridConfigList;
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseBody
    void deleteGridConfig(@RequestParam(value="id") Long id) {
        gridConfigService.deleteGridConfig(id);
    }

    @RequestMapping(value = "{brandName}", method = RequestMethod.GET)
    @ResponseBody
    GridConfig getConfigByBrand(@PathVariable("brandName") String brand) {
        GridConfig gridConfig = new GridConfig();
        gridConfig = gridConfigService.getGridConfigByBrand(brand);
        return gridConfig;
    }
}
