package no.fjordkraft.im.test;

import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.model.TransferFile;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.util.IMConstants;

/**
 * Created by miles on 8/8/2017.
 */
public class PreprocessRequestWrapper<T,K> extends PreprocessRequest<T,K> {

    private T statement;

    PreprocessRequestWrapper() {

    }

    PreprocessRequestWrapper(String paymentType, String paymentTypeStatus) {
        Statement statement = new Statement();
        statement.setPaymentType(paymentType);
        statement.setPaymentTypeStatus(paymentTypeStatus);
        setStatement((T) statement);
    }

    PreprocessRequestWrapper(Statement statement) {
        setStatement((T) statement);
        setEntity((K) formStatementEntity());
    }

    no.fjordkraft.im.model.Statement formStatementEntity() {
        no.fjordkraft.im.model.Statement statementEntity = new no.fjordkraft.im.model.Statement();
        statementEntity.setId(1l);
        statementEntity.setStatus("PENDING");

        TransferFile transferFile = new TransferFile();
        transferFile.setBrand("TEST");

        SystemBatchInput systemBatchInput = new SystemBatchInput();
        systemBatchInput.setTransferFile(transferFile);

        statementEntity.setSystemBatchInput(systemBatchInput);

        return statementEntity;
    }
}
