package no.fjordkraft.im.controller;

import com.sun.org.apache.xpath.internal.operations.Mult;
import no.fjordkraft.im.domain.*;
import no.fjordkraft.im.model.*;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.services.impl.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.birt.core.exception.BirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * Created by miles on 6/13/2017.
 */
@RestController
@RequestMapping("/layout")
public class IMLayoutController {

    private static final Logger logger = LoggerFactory.getLogger(IMLayoutController.class);

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

    @Autowired
    ConfigService configService;

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
        String encoding = configService.getString("save.layout.encoding");
        encoding = encoding == null ? "UTF-8":encoding;
        logger.debug("saveLayoutTemplate encoding "+ encoding);
        if(null != file) {
            template = FileUtils.readFileToString(convert(file), Charset.forName(encoding));
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
        String encoding = configService.getString("save.layout.encoding");
        encoding = encoding == null ? "UTF-8":encoding;
        logger.debug("saveLayoutTemplate encoding "+ encoding);
        if(null != file) {
            template = FileUtils.readFileToString(convert(file), Charset.forName(encoding));
        }
        RestLayoutTemplate restLayoutTemplate = new RestLayoutTemplate();
        restLayoutTemplate.setName(name);
        restLayoutTemplate.setDescription(description);
        restLayoutTemplate.setRptDesign(template);
        return layoutService.updateLayout(id, restLayoutTemplate);
    }

    @RequestMapping(value = "template/version/{id}", method = RequestMethod.PUT)
    @ResponseBody
    Layout updateLayoutVersion(@PathVariable("id") Long id,
                               @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        String template = null;
        String encoding = configService.getString("save.layout.encoding");
        encoding = encoding == null ? "UTF-8":encoding;
        logger.debug("saveLayoutTemplate encoding "+ encoding);
        if(null != file) {
            template = FileUtils.readFileToString(convert(file), Charset.forName(encoding));
        }
        return layoutService.updateLayoutVersion(id, template);
    }

    @RequestMapping(value = "template/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void deleteLayoutTemplate(@PathVariable("id") Long id) {
        layoutService.deleteLayout(id);
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

    @RequestMapping(value="deActivate/{layoutId}/{version}", method = RequestMethod.PUT)
    @ResponseBody
    void deActivateTemplate(@PathVariable("layoutId") Long layoutId,
                          @PathVariable("version") Integer version) {
        layoutContentService.deActivateLayoutTemplate(layoutId, version);
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
        LayoutContent layoutContent = layoutContentService.getLayoutContentByLayoutId(id);
        //File file = new File("/opt/app/sampleFile/sample.rptdesign");
        File file = new File("D:\\data\\sample.rptdesign");
        FileUtils.writeStringToFile(file, layoutContent.getFileContent());
            logger.debug("sample.rptdesign is saved.");
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
