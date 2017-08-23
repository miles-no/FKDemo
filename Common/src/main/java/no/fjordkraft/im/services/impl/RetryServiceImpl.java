package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.TransferFileId;
import no.fjordkraft.im.model.TransferTypeEnum;
import no.fjordkraft.im.repository.TransferFileRepository;
import no.fjordkraft.im.services.RetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by miles on 7/25/2017.
 */
@Service
public class RetryServiceImpl implements RetryService {

    @Autowired
    TransferFileRepository transferFileRepository;

    @Override
    public void updateTransferfileStatus(String transfertype, String brand, String filename, String status) {
        TransferFileId transferFileId = new TransferFileId();
        transferFileId.setTransferType(TransferTypeEnum.valueOf(transfertype));
        transferFileId.setBrand(brand);
        transferFileId.setFilename(filename);

        //TransferFile transferFile = transferFileRepository.findOne(transferFileId);
    }
}
