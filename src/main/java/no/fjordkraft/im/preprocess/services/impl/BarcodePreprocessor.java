package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.model.BrandConfig;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.repository.BrandConfigRepository;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by miles on 5/18/2017.
 */

@Service
@PreprocessorInfo(order=5)
public class BarcodePreprocessor extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(BarcodePreprocessor.class);

    @Autowired
    BrandConfigRepository barcodeConfigRepository;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {

        no.fjordkraft.im.model.Statement statement = request.getEntity();
        BrandConfig brandConfig = barcodeConfigRepository.getBarcodeConfigByBrand(statement.getSystemBatchInput().getTransferFile().getBrand());
        if(IMConstants.TRUE == brandConfig.getUseEABarcode()) {
            String barcode = IMConstants.BARCODE_PREFIX + brandConfig.getAgreementNumber() + brandConfig.getServiceLevel()
                    + brandConfig.getPrefixKID() + request.getStatement().getAccountNumber();
            logger.debug("Barcode for statementId " + statement.getId() + " is " + barcode);
            request.getStatement().setBarcode(Long.parseLong(barcode));
            request.getStatement().setKontonummer(brandConfig.getKontonummer());
        }
    }
}
