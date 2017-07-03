package no.fjordkraft.im.controller;

import no.fjordkraft.im.domain.RestLayout;
import no.fjordkraft.im.model.RuleAttributes;
import no.fjordkraft.im.model.Layout;
import no.fjordkraft.im.model.LayoutRule;
import no.fjordkraft.im.model.LayoutRuleMap;
import no.fjordkraft.im.services.impl.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * Created by miles on 6/13/2017.
 */
@RestController
@RequestMapping("/layout")
public class IMLayoutController {

    @Autowired
    RuleAttributesServiceImpl ruleAttributesService;

    @Autowired
    LayoutServiceImpl layoutService;

    @Autowired
    LayoutContentServiceImpl layoutContentService;

    @Autowired
    LayoutRuleServiceImpl layoutRuleService;

    @Autowired
    LayoutRuleMapServiceImpl layoutRuleMapService;

    @RequestMapping(value = "attribute", method = RequestMethod.GET)
    @ResponseBody
    List<RuleAttributes> getAllLayoutConfig() {
        return ruleAttributesService.getAllLayoutConfig();
    }

    @RequestMapping(value = "attribute/{name}", method = RequestMethod.GET)
    @ResponseBody
    RuleAttributes getRuleAttributeByName(@PathVariable("name") String name) {
        return ruleAttributesService.getRuleAttributeByName(name);
    }

    @RequestMapping(value = "attribute", method = RequestMethod.POST)
    @ResponseBody
    void saveLayoutConfig(@RequestParam("name") String name,
                          @RequestParam("type") String type,
                          @RequestParam("fieldMapping") String fieldMapping) {
        RuleAttributes ruleAttributes = new RuleAttributes();
        ruleAttributes.setName(name);
        ruleAttributes.setType(type);
        ruleAttributes.setFieldMapping(fieldMapping);
        ruleAttributesService.saveLayoutConfig(ruleAttributes);
    }

    @RequestMapping(value = "attribute/{id}", method = RequestMethod.PUT)
    @ResponseBody
    void updateLayoutConfig(@PathVariable("id") Long id,
                            @RequestParam("name") String name,
                            @RequestParam("type") String type,
                            @RequestParam("fieldMapping") String fieldMapping) {
        RuleAttributes ruleAttributes = new RuleAttributes();
        ruleAttributes.setId(id);
        ruleAttributes.setName(name);
        ruleAttributes.setType(type);
        ruleAttributes.setFieldMapping(fieldMapping);
        ruleAttributesService.updateLayoutConfig(ruleAttributes);
    }

    @RequestMapping(value="attribute/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void deleteLayoutConfig(@PathVariable("id") Long id) {
        ruleAttributesService.deleteLayoutConfig(id);
    }

    @RequestMapping(value="template/all", method = RequestMethod.GET)
    @ResponseBody
    List<Layout> getAllLayoutDesign() {
        return layoutService.getAllLayoutDesign();
    }

    @RequestMapping(value="template/{id}", method = RequestMethod.GET)
    @ResponseBody
    Layout getLayoutDesignById(@PathVariable("id") Long id) {
        return layoutService.getLayoutDesignById(id);
    }

    @RequestMapping(value="template", method = RequestMethod.POST)
    @ResponseBody
    void saveLayoutDesign(@RequestParam("name") String name,
                          @RequestParam("description") String description) {
        Layout layout = new Layout();
        layout.setName(name);
        layout.setDescription(description);
        layoutService.saveLayoutDesign(layout);
    }

    @RequestMapping(value="template/{id}", method = RequestMethod.PUT)
    @ResponseBody
    void updateLayoutDesign(@PathVariable("id") Long id,
                            @RequestParam("name") String name,
                            @RequestParam("description") String description) {
        Layout layout = new Layout();
        layout.setId(id);
        layout.setName(name);
        layout.setDescription(description);
        layoutService.updateLayoutDesign(layout);
    }

    public File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    @RequestMapping(value="template/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void deleteLayoutDesign(@PathVariable("id") Long id) {
        layoutService.deleteLayoutDesign(id);
    }

    @RequestMapping(value="activate/{layoutId}/{version}", method = RequestMethod.PUT)
    @ResponseBody
    void activateTemplate(@PathVariable("layoutId") Long layoutId,
                          @PathVariable("version") Integer version) {
        layoutContentService.activateLayoutTemplate(layoutId, version);
    }

    @RequestMapping(value="content/{layoutId}", method = RequestMethod.GET)
    @ResponseBody
    byte[] getContentByIdLayoutId(@PathVariable("layoutId") Long layoutId) {
        String layoutTemplate = layoutService.getRptDesignFile(layoutId);
        return layoutTemplate.getBytes();
    }

    @RequestMapping(value="content", method = RequestMethod.POST)
    @ResponseBody
    void saveLayoutContent(@RequestParam("layoutId") Long layoutId,
                           @RequestPart("file") MultipartFile file) throws IOException {
        String template = FileUtils.readFileToString(convert(file), StandardCharsets.ISO_8859_1);
        layoutContentService.saveLayoutContent(layoutId, template);
    }

    @RequestMapping(value="content/{id}", method = RequestMethod.PUT)
    @ResponseBody
    void updateLayoutContent(@PathVariable("id") Long id,
                             @RequestParam("file") MultipartFile file) throws IOException {
        String template = FileUtils.readFileToString(convert(file), StandardCharsets.ISO_8859_1);
        layoutContentService.updateLayoutContent(id, template);
    }

    @RequestMapping(value="{brand}", method = RequestMethod.GET)
    @ResponseBody
    List<RestLayout> getLayoutForBrand(@PathVariable("brand") String brand) {
        return layoutService.getLayoutByBrand(brand);
    }

    @RequestMapping(value="rule", method = RequestMethod.GET)
    @ResponseBody
    List<LayoutRule> getAllRules() {
        List<LayoutRule> layoutRules = layoutRuleService.getAllLayoutRule();
        Collections.sort(layoutRules);
        return layoutRules;
    }

    @RequestMapping(value="rule/{brand}", method = RequestMethod.GET)
    @ResponseBody
    List<LayoutRule> getRuleByBrand(@PathVariable("brand") String brand) {
        List<LayoutRule> layoutRules = layoutRuleService.getLayoutRuleByBrand(brand);
        Collections.sort(layoutRules);
        return layoutRules;
    }

    @RequestMapping(value="rule", method = RequestMethod.POST)
    @ResponseBody
    void saveLayoutRule(@RequestParam("brand") String brand,
                        @RequestParam("layoutId") Long layoutId) {
        LayoutRule layoutRule = new LayoutRule();
        layoutRule.setBrand(brand);
        layoutRule.setLayoutId(layoutId);
        layoutRuleService.saveLayoutRule(layoutRule);
    }

    @RequestMapping(value="rule/{id}", method = RequestMethod.PUT)
    @ResponseBody
    void updateLayoutRule(@PathVariable("id") Long id,
                          @RequestParam("brand") String brand,
                          @RequestParam("layoutId") Long layoutId) {
        LayoutRule layoutRule = new LayoutRule();
        layoutRule.setId(id);
        layoutRule.setBrand(brand);
        layoutRule.setLayoutId(layoutId);
        layoutRuleService.updateLayoutRule(layoutRule);
    }

    @RequestMapping(value="rule/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void deleteLayoutRule(@PathVariable("id") Long id) {
        layoutRuleService.deleteLayoutRule(id);
    }

    @RequestMapping(value="rulemap", method = RequestMethod.GET)
    @ResponseBody
    List<LayoutRuleMap> getAllRuleMap() {
        return layoutRuleMapService.getAllLayoutRuleMap();
    }

    @RequestMapping(value="rulemap", method = RequestMethod.POST)
    @ResponseBody
    void saveRuleMap(@RequestParam("ruleId") Long ruleId,
                     @RequestParam("name") String name,
                     @RequestParam("operation") String operation,
                     @RequestParam("value") String value) {
        LayoutRuleMap layoutRuleMap = new LayoutRuleMap();
        layoutRuleMap.setRuleId(ruleId);
        layoutRuleMap.setName(name);
        layoutRuleMap.setOperation(operation);
        layoutRuleMap.setValue(value);
        layoutRuleMapService.saveLayoutRuleMap(layoutRuleMap);
    }

    @RequestMapping(value="rulemap/{id}", method = RequestMethod.PUT)
    @ResponseBody
    void updateRuleMap(@PathVariable("id") Long id,
                     @RequestParam("ruleId") Long ruleId,
                     @RequestParam("name") String name,
                     @RequestParam("operation") String operation,
                     @RequestParam("value") String value) {
        LayoutRuleMap layoutRuleMap = new LayoutRuleMap();
        layoutRuleMap.setId(id);
        layoutRuleMap.setRuleId(ruleId);
        layoutRuleMap.setName(name);
        layoutRuleMap.setOperation(operation);
        layoutRuleMap.setValue(value);
        layoutRuleMapService.updateLayoutRuleMap(layoutRuleMap);
    }


    @RequestMapping(value="rulemap/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void deleteRuleMap(@PathVariable("id") Long id) {
        layoutRuleMapService.deleteLayoutRuleMap(id);
    }
}
