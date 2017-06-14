package no.fjordkraft.im.controller;

import no.fjordkraft.im.domain.RestLayoutConfig;
import no.fjordkraft.im.services.impl.LayoutConfigServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by miles on 6/13/2017.
 */
@RestController
@RequestMapping("/layout/config")
public class IMLayoutController {

    @Autowired
    LayoutConfigServiceImpl layoutConfigService;

    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    public void saveLayoutConfig(@RequestParam("brand") String brand,
                             @RequestParam("legalPartClass") String legalPartClass,
                             @RequestParam("accountCategory") String accountCategory,
                             @RequestParam("distributionMethod") String distributionMethod,
                             @RequestParam("creditLimit") boolean creditLimit,
                             @RequestParam("version") int version,
                             @RequestPart("file") MultipartFile file) throws IOException {
        RestLayoutConfig restLayoutConfig = new RestLayoutConfig();
        restLayoutConfig.setBrand(brand);
        restLayoutConfig.setLegalPartClass(legalPartClass);
        restLayoutConfig.setAccountCategory(accountCategory);
        restLayoutConfig.setDistributionMethod(distributionMethod);
        restLayoutConfig.setCreditLimit(creditLimit);
        restLayoutConfig.setVersion(version);
        restLayoutConfig.setInputStream(file.getInputStream());

        layoutConfigService.saveLayoutConfig(restLayoutConfig);
    }

    @RequestMapping(value = "{id}",method = RequestMethod.PUT)
    @ResponseBody
    public void updateLayoutConfig(@PathVariable("id") Long id,
                             @RequestParam("brand") String brand,
                             @RequestParam("legalPartClass") String legalPartClass,
                             @RequestParam("accountCategory") String accountCategory,
                             @RequestParam("distributionMethod") String distributionMethod,
                             @RequestParam("creditLimit") boolean creditLimit,
                             @RequestParam("version") int version,
                             @RequestPart("file") MultipartFile file) throws IOException {
        RestLayoutConfig restLayoutConfig = new RestLayoutConfig();
        restLayoutConfig.setId(id);
        restLayoutConfig.setBrand(brand);
        restLayoutConfig.setLegalPartClass(legalPartClass);
        restLayoutConfig.setAccountCategory(accountCategory);
        restLayoutConfig.setDistributionMethod(distributionMethod);
        restLayoutConfig.setCreditLimit(creditLimit);
        restLayoutConfig.setVersion(version);
        restLayoutConfig.setInputStream(file.getInputStream());

        layoutConfigService.updateLayoutConfig(restLayoutConfig);
    }

    @RequestMapping(value = "newVersion/{id}",method = RequestMethod.PUT)
    @ResponseBody
    public void updateNewLayoutVersion(@PathVariable("id") Long id,
                                   @RequestParam("brand") String brand,
                                   @RequestParam("legalPartClass") String legalPartClass,
                                   @RequestParam("accountCategory") String accountCategory,
                                   @RequestParam("distributionMethod") String distributionMethod,
                                   @RequestParam("creditLimit") boolean creditLimit,
                                   @RequestParam("version") int version,
                                   @RequestPart("file") MultipartFile file) throws IOException {
        RestLayoutConfig restLayoutConfig = new RestLayoutConfig();
        restLayoutConfig.setId(id);
        restLayoutConfig.setBrand(brand);
        restLayoutConfig.setLegalPartClass(legalPartClass);
        restLayoutConfig.setAccountCategory(accountCategory);
        restLayoutConfig.setDistributionMethod(distributionMethod);
        restLayoutConfig.setCreditLimit(creditLimit);
        restLayoutConfig.setVersion(version);
        restLayoutConfig.setInputStream(file.getInputStream());

        layoutConfigService.updateNewLayoutVersion(restLayoutConfig);
    }
}
