package no.fjordkraft.im.preprocess.services.impl;

//import no.fjordkraft.im.util.SetInvoiceASOnline;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.if320.models.ToAddress;
import no.fjordkraft.im.model.BrandConfig;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.BrandService;
import no.fjordkraft.im.services.impl.AuditLogServiceImpl;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by miles on 5/18/2017.
 */

@Service
@PreprocessorInfo(order=25)
public class BarcodePreprocessor extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(BarcodePreprocessor.class);

    @Autowired
    BrandService brandService;

    @Autowired
    AuditLogServiceImpl auditLogService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {

        no.fjordkraft.im.model.Statement statement = request.getEntity();
        String brand = statement.getSystemBatchInput().getBrand();
        BrandConfig brandConfig = brandService.getBrandConfigByName(brand);

        if(null != brandConfig) {
            if (IMConstants.TRUE == brandConfig.getUseEABarcode()) {
                String barcode = IMConstants.BARCODE_PREFIX + brandConfig.getAgreementNumber() + brandConfig.getServiceLevel() +
                       "0" + brandConfig.getPrefixKID() + request.getStatement().getAccountNumber();
                logger.debug("Barcode for statementId " + statement.getId() + " is " + barcode);
                request.getStatement().setBarcode(barcode);
            }
            request.getStatement().setKontonummer(brandConfig.getKontonummer());
            ToAddress toAddress = new ToAddress();
            toAddress.setDescription(brandConfig.getDescription());
            toAddress.setPostcode(brandConfig.getPostcode());
            toAddress.setCity(brandConfig.getCity());
            toAddress.setNationalId(brandConfig.getNationalId());
            toAddress.setRegion(brandConfig.getRegion());
            request.getStatement().setToAddress(toAddress);
        } else {
            //if(SetInvoiceASOnline.get()==null || !SetInvoiceASOnline.get())
            if(!request.getStatement().isOnline())
            {
                String errorMessage = "Brand not found";
                auditLogService.saveAuditLog(statement.getId(), StatementStatusEnum.PRE_PROCESSING.getStatus(), errorMessage, IMConstants.WARNING,statement.getLegalPartClass());
            }
        }
    }

    public void setBarcodeConfigRepository(BrandService brandService) {
        this.brandService = brandService;
    }
}
