package no.fjordkraft.im.controller;

import no.fjordkraft.im.model.GridConfig;
import no.fjordkraft.im.services.impl.GridConfigServiceImpl;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Map<String, Object> getGridConfigs() {
        Map<String, Object> map = new HashMap<>();
        List<GridConfig> gridConfigs = gridConfigService.getGridConfigs();
        Long count = gridConfigService.getGridCount();
        map.put(IMConstants.TOTAL, count);
        map.put(IMConstants.GRID, gridConfigs);
        return map;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    void saveGridConfig(@RequestBody GridConfig gridConfig) {
        gridConfigService.saveGridConfig(gridConfig);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    void updateGridConfig(@RequestBody GridConfig gridConfig) {
        gridConfigService.updateGridConfig(gridConfig);
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

    @RequestMapping(value = "brand", method = RequestMethod.GET)
    @ResponseBody
    List<String> getAllBrands() {
        return gridConfigService.getAllBrands();
    }
}
