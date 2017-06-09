package no.fjordkraft.im.controller;

import no.fjordkraft.im.model.BrandConfig;
import no.fjordkraft.im.services.impl.IMBrandServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miles on 6/5/2017.
 */
@Controller
@RequestMapping("/brand/config")
public class IMBrandController {

    @Autowired
    IMBrandServiceImpl brandService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    List<BrandConfig> getBrandConfigs() {
        List<BrandConfig> brandConfigs = new ArrayList<BrandConfig>();
        brandConfigs = brandService.getBrandConfigs();
        return brandConfigs;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    List<BrandConfig> saveBrandConfig(@RequestBody BrandConfig brandConfig) {
        List<BrandConfig> brandConfigList = new ArrayList<>();
        brandService.saveBrandConfig(brandConfig);
        brandConfigList = getBrandConfigs();
        return brandConfigList;
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    List<BrandConfig> updateBrandConfig(@RequestBody BrandConfig brandConfig) {
        List<BrandConfig> brandConfigList = new ArrayList<>();
        brandService.updateBrandConfig(brandConfig);
        brandConfigList = getBrandConfigs();
        return brandConfigList;
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
}
