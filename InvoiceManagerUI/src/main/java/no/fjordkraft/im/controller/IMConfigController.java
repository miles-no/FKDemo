package no.fjordkraft.im.controller;

import no.fjordkraft.im.model.Config;
import no.fjordkraft.im.ui.services.UIConfigService;
import no.fjordkraft.im.util.IMConstants;
import no.fjordkraft.security.jpa.domain.UserFunctionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by miles on 6/5/2017.
 */
@RestController
@RequestMapping("/api/config")
public class IMConfigController {

    @Autowired
    UIConfigService configService;

    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    @ResponseBody
    @no.fjordkraft.security.springmvc.annotation.Function(UserFunctionEnum.Api_Access)
    String getValue(@PathVariable("key") String key) {
        return configService.getString(key);
    }

    @no.fjordkraft.security.springmvc.annotation.Function(UserFunctionEnum.Afi_Standard)
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    void saveConfig(@RequestParam("key") String key, @RequestParam("value") String value) {
        configService.saveConfig(key, value);
    }

    @no.fjordkraft.security.springmvc.annotation.Function(UserFunctionEnum.Admin_Jobber)
    @RequestMapping(value = "{key}", method = RequestMethod.PUT)
    @ResponseBody
    void updateConfig(@PathVariable("key") String key, @RequestParam("value") String value) {
        configService.updateConfig(key, value);
    }

    @RequestMapping(value = "{key}", method = RequestMethod.DELETE)
    @ResponseBody
    void deleteConfig(@PathVariable("key") String key) {
        configService.deleteConfig(key);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    Map<String, Object> getConfig() {
        Map<String,Object> map = new HashMap<String, Object>();
        List<Config> configList = configService.findAll();
        Long count = configService.getConfigCount();
        map.put(IMConstants.TOTAL, count);
        map.put(IMConstants.CONFIG, configList);
        return map;
    }
}
