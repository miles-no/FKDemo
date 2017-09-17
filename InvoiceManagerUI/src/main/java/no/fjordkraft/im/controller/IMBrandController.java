package no.fjordkraft.im.controller;

import no.fjordkraft.im.model.BrandConfig;
import no.fjordkraft.im.ui.services.UIBrandService;
import no.fjordkraft.im.util.IMConstants;
import no.fjordkraft.security.jpa.domain.UserFunctionEnum;
import no.fjordkraft.security.springmvc.annotation.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by miles on 6/5/2017.
 */
@RestController
@RequestMapping("/api/brand/config")
public class IMBrandController {

    @Autowired
    UIBrandService brandService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    Map<String, Object> getBrandConfigs() {
        Map<String, Object> map = new HashMap<String, Object>();
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
    @Function(UserFunctionEnum.Afi_Rabatt)
    void updateBrandConfig(@RequestBody BrandConfig brandConfig) {
        brandService.updateBrandConfig(brandConfig);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Function(UserFunctionEnum.Admin_Jobber)
    void deleteBrandConfig(@PathVariable(value="id") Long id) {
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
