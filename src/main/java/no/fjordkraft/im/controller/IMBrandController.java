package no.fjordkraft.im.controller;

import no.fjordkraft.im.model.BrandConfig;
import no.fjordkraft.im.services.BrandService;
import no.fjordkraft.im.services.impl.BrandServiceImpl;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by miles on 6/5/2017.
 */
@RestController
@RequestMapping("/brand/config")
public class IMBrandController {

    @Autowired
    BrandService brandService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    Map<String, Object> getBrandConfigs() {
        Map<String, Object> map = new HashMap<>();
        List<BrandConfig> brandConfigs = brandService.getBrandConfigs();
        Long count = brandService.getBrandCount();
        map.put(IMConstants.TOTAL, count);
        map.put(IMConstants.BRAND, brandConfigs);
        return map;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    void saveBrandConfig(@RequestBody BrandConfig brandConfig) {
        brandService.saveBrandConfig(brandConfig);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    void updateBrandConfig(@RequestBody BrandConfig brandConfig) {
        brandService.updateBrandConfig(brandConfig);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseBody
    void deleteBrandConfig(@RequestParam(value="id") Long id) {
        brandService.deleteBrandConfig(id);
    }


    @RequestMapping(value = "{brandName}", method = RequestMethod.GET)
    @ResponseBody
    BrandConfig getConfigByBrand(@PathVariable("brandName") String brand) {
        BrandConfig brandConfig = new BrandConfig();
        brandConfig = brandService.getBrandConfigByName(brand);
        return brandConfig;
    }

    @RequestMapping(value = "brand", method = RequestMethod.GET)
    @ResponseBody
    List<String> getAllBrands() {
        return brandService.getAllBrands();
    }
}
