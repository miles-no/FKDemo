package no.fjordkraft.im.controller;

import no.fjordkraft.im.model.Config;
import no.fjordkraft.im.repository.ConfigRepository;
import no.fjordkraft.im.services.impl.ConfigServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by miles on 6/5/2017.
 */
@Controller
@RequestMapping("/config")
public class IMConfigController {

    @Autowired
    ConfigServiceImpl configService;

    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    @ResponseBody
    String getValue(@PathVariable("key") String key) {
        return configService.getString(key);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    void saveConfig(@RequestParam("key") String key, @RequestParam("value") String value) {
        configService.saveConfig(key, value);
    }

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
    List<Config> getConfig() {
        return configService.findAll();
    }
}
