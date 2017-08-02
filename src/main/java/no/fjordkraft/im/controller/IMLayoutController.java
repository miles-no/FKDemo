package no.fjordkraft.im.controller;

import com.sun.org.apache.xpath.internal.operations.Mult;
import no.fjordkraft.im.domain.*;
import no.fjordkraft.im.model.*;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.services.impl.*;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.birt.core.exception.BirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    StatementServiceImpl statementService;


    @RequestMapping(value = "attribute", method = RequestMethod.GET)
    @ResponseBody
    List<RestRuleAttribute> getAllLayoutConfig() {
        return ruleAttributesService.getAllLayoutConfig();
    }

    @RequestMapping(value = "attribute/{name}", method = RequestMethod.GET)
    @ResponseBody
    RestRuleAttribute getRuleAttributeByName(@PathVariable("name") String name) {
        return ruleAttributesService.getRuleAttributeByName(name);
    }

    @RequestMapping(value = "attribute", method = RequestMethod.POST)
    @ResponseBody
    void saveLayoutConfig(@RequestBody RuleAttributes ruleAttributes) {
        ruleAttributesService.saveLayoutConfig(ruleAttributes);
    }

    @RequestMapping(value = "attribute/{id}", method = RequestMethod.PUT)
    @ResponseBody
    void updateLayoutConfig(@PathVariable("id") Long id,
                            @RequestBody RuleAttributes ruleAttributes) {
        ruleAttributes.setId(id);
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
        byte rptFile[] = layoutContent.getFileContent().getBytes();
        logger.debug("File bytes is "+ rptFile.length+ " file length of string "+ layoutContent.getFileContent().length());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentLength(rptFile.length);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=sample.rptdesign");


        return new ResponseEntity<>(rptFile, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "preview", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getDesignPreview(@RequestParam("layoutId") Long layoutId,
                                                   @RequestParam("version") Integer version) throws IOException, BirtException {
        byte[] pdf = pdfGenerator.generatePreview(layoutId, version);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
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

    @RequestMapping(value = "statement/xml/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getStatementXmlFile(@PathVariable("id") Long id) throws IOException {

        String payload = statementService.getStatementById(id);
        if(null != payload) {
            byte statement[] = payload.getBytes();
            logger.debug("File bytes is " + statement.length + " file length of string " + payload.length());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.setContentLength(statement.length);
            headers.set(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=statement_" + id + ".xml");

            return new ResponseEntity<>(statement, headers, HttpStatus.OK);
        }
        return null;
    }
}
