package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.model.TransferFile;
import no.fjordkraft.im.repository.SystemBatchInputRepository;
import no.fjordkraft.im.services.SystemBatchInputService;
import no.fjordkraft.im.statusEnum.SystemBatchInputStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * Created by bhavi on 5/22/2017.
 */
@Service
public class SystemBatchInputServiceImpl implements SystemBatchInputService {

    @Autowired
    SystemBatchInputRepository systemBatchInputRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateStatusOfIMSystemBatchInput(SystemBatchInput systemBatchInput , String status) {
        systemBatchInput.setStatus(status);
        systemBatchInput.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        systemBatchInputRepository.save(systemBatchInput);
    }

    @Transactional
    public void saveSingleIMSysteBatchInput(TransferFile transferFile) {
        SystemBatchInput imSystemBatchInput = new SystemBatchInput();
        imSystemBatchInput.setTfId(transferFile.getId());
        imSystemBatchInput.setBrand(transferFile.getBrand());
        imSystemBatchInput.setFilename(transferFile.getFileName());
        imSystemBatchInput.setPayload(transferFile.getFileContent());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        imSystemBatchInput.setCreateTime(timestamp);
        imSystemBatchInput.setUpdateTime(timestamp);
        imSystemBatchInput.setStatus(SystemBatchInputStatusEnum.PENDING.getStatus());
        systemBatchInputRepository.save(imSystemBatchInput);
    }
}
