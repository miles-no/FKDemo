package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.model.BarcodeConfig;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.repository.BarcodeConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by miles on 5/18/2017.
 */

@Service
@PreprocessorInfo(order=5)
public class BarcodePreprocessor extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(BarcodePreprocessor.class);

    final static String BARCODE_PREFIX = "2";

    @Autowired
    BarcodeConfigRepository barcodeConfigRepository;

    /*@Autowired
    StatementRepository statementRepository;*/

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {

        //String statementOcr = String.valueOf(request.getStatement().getStatementOcrNumber());
        no.fjordkraft.im.model.Statement statement = request.getEntity();//statementRepository.readStatementByStatementOCR(statementOcr);
        BarcodeConfig barcodeConfig = barcodeConfigRepository.getBarcodeConfigBrand(statement.getSystemBatchInput().getBrand());
        String barcode = BARCODE_PREFIX + barcodeConfig.getAgreementNumber() + barcodeConfig.getServiceLevel()
                +barcodeConfig.getPrefixKID() + request.getStatement().getAccountNumber();
        logger.debug("Barcode for statementId "+ statement.getId() + " is "+ barcode);
        request.getStatement().setBarcode(Long.parseLong(barcode));
    }
}
