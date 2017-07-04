package no.fjordkraft.im.controller;

import com.sun.org.apache.xpath.internal.operations.Mult;
import no.fjordkraft.im.domain.*;
import no.fjordkraft.im.model.*;
import no.fjordkraft.im.services.impl.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.birt.core.exception.BirtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    PDFGeneratorImpl pdfGenerator;

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
    void saveLayoutConfig(@RequestBody RestRuleAttribute restRuleAttribute) {
        RuleAttributes ruleAttributes = new RuleAttributes();
        ruleAttributes.setName(restRuleAttribute.getName());
        ruleAttributes.setType(restRuleAttribute.getType());
        ruleAttributes.setFieldMapping(restRuleAttribute.getFieldMapping());
        ruleAttributesService.saveLayoutConfig(ruleAttributes);
    }

    @RequestMapping(value = "attribute/{id}", method = RequestMethod.PUT)
    @ResponseBody
    void updateLayoutConfig(@PathVariable("id") Long id,
                            @RequestBody RestRuleAttribute restRuleAttribute) {
        RuleAttributes ruleAttributes = new RuleAttributes();
        ruleAttributes.setId(id);
        ruleAttributes.setName(restRuleAttribute.getName());
        ruleAttributes.setType(restRuleAttribute.getType());
        ruleAttributes.setFieldMapping(restRuleAttribute.getFieldMapping());
        ruleAttributesService.updateLayoutConfig(ruleAttributes);
    }

    @RequestMapping(value="attribute/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void deleteLayoutConfig(@PathVariable("id") Long id) {
        ruleAttributesService.deleteLayoutConfig(id);
    }

    @RequestMapping(value="template/all", method = RequestMethod.GET)
    @ResponseBody
    List<RestLayout> getAllLayoutTemplate() {
        return layoutService.getAllLayout();
    }

    @RequestMapping(value="template/brand/{brand}", method = RequestMethod.GET)
    @ResponseBody
    List<RestLayout> getLayoutForBrand(@PathVariable("brand") String brand) {
        return layoutService.getLayoutByBrand(brand);
    }

    @RequestMapping(value = "template", method = RequestMethod.POST)
    @ResponseBody
    Layout saveLayoutTemplate(@RequestPart("name") String name,
                            @RequestPart("description") String description,
                            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        String template = null;
        if(null != file) {
            template = FileUtils.readFileToString(convert(file), StandardCharsets.ISO_8859_1);
        }
        RestLayoutTemplate restLayoutTemplate = new RestLayoutTemplate();
        restLayoutTemplate.setName(name);
        restLayoutTemplate.setDescription(description);
        restLayoutTemplate.setRptDesign(template);
        return layoutService.saveLayout(restLayoutTemplate);
    }

    @RequestMapping(value = "template/{id}", method = RequestMethod.PUT)
    @ResponseBody
    Layout updateLayoutTemplate(@PathVariable("id") Long id,
                                @RequestPart("name") String name,
                                @RequestPart("description") String description,
                              @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        String template = null;
        if(null != file) {
            template = FileUtils.readFileToString(convert(file), StandardCharsets.ISO_8859_1);
        }
        RestLayoutTemplate restLayoutTemplate = new RestLayoutTemplate();
        restLayoutTemplate.setName(name);
        restLayoutTemplate.setDescription(description);
        restLayoutTemplate.setRptDesign(template);
        return layoutService.updateLayout(id, restLayoutTemplate);
    }

    public File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    @RequestMapping(value="activate/{layoutId}/{version}", method = RequestMethod.PUT)
    @ResponseBody
    void activateTemplate(@PathVariable("layoutId") Long layoutId,
                          @PathVariable("version") Integer version) {
        layoutContentService.activateLayoutTemplate(layoutId, version);
    }

    @RequestMapping(value="rule", method = RequestMethod.GET)
    @ResponseBody
    List<LayoutRule> getAllRules() {
        return layoutRuleService.getAllLayoutRule();
    }

    @RequestMapping(value="rule", method = RequestMethod.POST)
    @ResponseBody
    void saveLayoutRule(@RequestBody LayoutRule layoutRule) {
        layoutRuleService.saveLayoutRule(layoutRule);
    }

    @RequestMapping(value="rule/{id}", method = RequestMethod.PUT)
    @ResponseBody
    void updateLayoutRule(@PathVariable("id") Long id,
                          @RequestBody LayoutRule layoutRule) {
        layoutRule.setId(id);
        layoutRuleService.updateLayoutRule(layoutRule);
    }

    @RequestMapping(value = "rptdesign", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getRptDesignFile(@RequestParam("id") Long id) throws IOException {
        LayoutContent layoutContent = layoutContentService.getLayoutContentById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentLength(layoutContent.getFileContent().length());
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=sample.rptdesign");
        return new ResponseEntity<>(layoutContent.getFileContent().getBytes(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "preview", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getDesignPreview(@RequestParam("layoutId") Long layoutId,
                                                   @RequestParam("version") Integer version) throws IOException, BirtException {
        byte[] pdf = pdfGenerator.generatePreview(layoutId, version);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentLength(pdf.length);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=sample.pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public List<NameValuePair> getLayoutList() {
        return layoutService.getLayoutList();
    }

    @RequestMapping(value = "rules", method = RequestMethod.GET)
    @ResponseBody
    List<NameValuePair> getRuleAttributes() {
        return ruleAttributesService.getAllRuleAttributes();
    }
}
