package no.fjordkraft.im.test;

import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.if320.models.TransactionGroup;
import no.fjordkraft.im.model.BrandConfig;
import no.fjordkraft.im.model.GridConfig;
import no.fjordkraft.im.model.LayoutRule;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.services.PreprocessorService;
import no.fjordkraft.im.preprocess.services.impl.*;
import no.fjordkraft.im.repository.TransactionGroupRepository;
import no.fjordkraft.im.services.*;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by miles on 8/7/2017.
 */

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.DEFAULT)
public class PreProcessorTest {

    LayoutSelectionPreprocessor layoutSelectionPreprocessor = new LayoutSelectionPreprocessor();
    PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> wrapperRequest;
    PaymentTypePreprocessor paymentTypePreprocessor = new PaymentTypePreprocessor();
    BarcodePreprocessor barcodePreprocessor = new BarcodePreprocessor();
    PreprocessorService preprocessorService = new PreprocessorServiceImpl();
    TransactionGroupPreprocessor transactionGroupPreprocessor = new TransactionGroupPreprocessor();
    PieChartPreprocessor pieChartPreprocessor = new PieChartPreprocessor();
    CustomTransactionGroupPreprocessor customTransactionPreprocessor = new CustomTransactionGroupPreprocessor();
    ConsumptionsPreprocessor consumptionsPreprocessor =  new ConsumptionsPreprocessor();
    GenericPreprocessor genericPreprocessor = new GenericPreprocessor();
    PDFAttachmentExtractor pdfAttachmentExtractor = new PDFAttachmentExtractor();
    PDFAttachmentRemover pdfAttachmentRemover = new PDFAttachmentRemover();

    @Mock Statement statement;
    @Mock BrandService brandService = mock(BrandService.class);
    @Mock LayoutRuleService layoutRuleService = mock(LayoutRuleService.class);
    @Mock GridConfigService gridConfigService = mock(GridConfigService.class);
    @Mock AuditLogService auditLogService = mock(AuditLogService.class);
    @Mock TransactionGroupRepository transactionGroupRepository = mock(TransactionGroupRepository.class);
    @Mock InvoiceService invoiceService = mock(InvoiceService.class);

    BrandConfig brandConfig = new BrandConfig("TEST", '1', "0700", "1", "111", "1503.80.91058");
    List<LayoutRule> layoutRules = new ArrayList<>();
    GridConfig gridConfig = new GridConfig();
    TransactionGroup transactionGroup = new TransactionGroup();
    Unmarshaller unMarshaller;

    @Before
    public void setUp() throws Exception {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("no.fjordkraft.im.if320.models");
        unMarshaller = (Unmarshaller)marshaller;
        LayoutRule layoutRule = new LayoutRule();
        layoutRule.setId(1l);
        layoutRule.setBrand("TEST");
        layoutRule.setLayoutId(1l);
        layoutRules.add(layoutRule);

        gridConfig.setId(1l);
        gridConfig.setGridName("Test Grid");
        gridConfig.setPhone("0504");
        gridConfig.setEmail("test@miles.in");

        when(brandService.getBrandConfigByName("TEST")).thenReturn(brandConfig);
        when(layoutRuleService.getLayoutRuleByBrand("TEST")).thenReturn(layoutRules);
        when(gridConfigService.getGridConfigByBrand("TEST")).thenReturn(gridConfig);
        when(transactionGroupRepository.queryTransactionGroupByName(anyString())).thenReturn(null);
        when(invoiceService.saveInvoicePdf(anyObject())).thenReturn(null);

        String xml = IOUtils.toString(
                this.getClass().getResourceAsStream("/sampleFile/20170410_FKAS_39290734_statement_3.xml"),
                StandardCharsets.UTF_8
        );
        xml = xml.replaceAll("&lt;!\\[CDATA\\[", "");
        xml = xml.replaceAll("\\]\\]&gt;", "");
        xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" + xml;

        preprocessorService.setUnMarshaller(unMarshaller);
        statement = preprocessorService.unmarshallStatement(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        wrapperRequest = new PreprocessRequestWrapper<>(statement);
    }

    @Test
    public void genericPreprocessorTest() throws IOException {
        genericPreprocessor.setUnMarshaller(unMarshaller);
        genericPreprocessor.unmarshallAttachments(wrapperRequest.getStatement());
        genericPreprocessor.decodeAndUnmarshalEHFAttachment(wrapperRequest.getStatement());
        System.out.println("Done");
    }

    @Test
    public void pdfAttachmentExtractorTest() {
        pdfAttachmentExtractor.setInvoiceService(invoiceService);
        pdfAttachmentExtractor.preprocess(wrapperRequest);
        System.out.println("Done");
    }

    @Test
    public void pdfAttachmentRemoverTest() {
        pdfAttachmentRemover.preprocess(wrapperRequest);
        System.out.println("Done");
    }

    @Test
    public void consumptionsPreprocessorTest() {
        consumptionsPreprocessor.preprocess(wrapperRequest);
        System.out.println("Done");
    }

    @Test
    public void barcodePreprocessorTest() {
        barcodePreprocessor.setBarcodeConfigRepository(brandService);
        barcodePreprocessor.preprocess(wrapperRequest);
        String barcode = IMConstants.BARCODE_PREFIX + brandConfig.getAgreementNumber() + brandConfig.getServiceLevel()
                + brandConfig.getPrefixKID() + wrapperRequest.getStatement().getAccountNumber();
        Assert.assertEquals(barcode, String.valueOf(wrapperRequest.getStatement().getBarcode()));
    }

    @Test
    public void transactionGroupAndPieChartPreprocessorTest() {
        transactionGroupPreprocessor.setGridConfigService(gridConfigService);
        transactionGroupPreprocessor.setAuditLogService(auditLogService);
        transactionGroupPreprocessor.preprocess(wrapperRequest);
        transactionGroup = wrapperRequest.getStatement().getTransactionGroup();

        customTransactionPreprocessor.setTransactionGroupRepository(transactionGroupRepository);
        customTransactionPreprocessor.preprocess(wrapperRequest);

        pieChartPreprocessor.preprocess(wrapperRequest);
        System.out.println(IMConstants.NETT + ": " + wrapperRequest.getStatement().getPieChart().getDistribution().get(0).getAmount());
        System.out.println(IMConstants.STROM + ": " + wrapperRequest.getStatement().getPieChart().getDistribution().get(1).getAmount());
        System.out.println("Done");
    }

    @Test
    public void layoutSelectionPreprocessorTest() throws ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        layoutSelectionPreprocessor.setLayoutRuleServiceImpl(layoutRuleService);
        layoutSelectionPreprocessor.preprocess(wrapperRequest);
        Assert.assertEquals(layoutRules.get(0).getLayoutId(), wrapperRequest.getEntity().getLayoutID());
    }

    @Test
    public void paymentTypePreprocessorTest() throws ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {

        wrapperRequest = new PreprocessRequestWrapper<>(IMConstants.UNDEFINED, IMConstants.UNDEFINED);
        paymentTypePreprocessor.preprocess(wrapperRequest);
        Assert.assertEquals(IMConstants.NO, wrapperRequest.getStatement().getDirectDebit());

        wrapperRequest = new PreprocessRequestWrapper<>(IMConstants.DIRECT_DEBIT, IMConstants.GRANTED);
        paymentTypePreprocessor.preprocess(wrapperRequest);
        Assert.assertEquals(IMConstants.YES, wrapperRequest.getStatement().getDirectDebit());

        wrapperRequest = new PreprocessRequestWrapper<>(IMConstants.DIRECT_DEBIT, IMConstants.CANCELLED);
        paymentTypePreprocessor.preprocess(wrapperRequest);
        Assert.assertEquals(IMConstants.NO, wrapperRequest.getStatement().getDirectDebit());

        wrapperRequest = new PreprocessRequestWrapper<>(IMConstants.DIRECT_DEBIT, IMConstants.INTERNALLY_GRANTED);
        paymentTypePreprocessor.preprocess(wrapperRequest);
        Assert.assertEquals(IMConstants.YES, wrapperRequest.getStatement().getDirectDebit());
    }

}
