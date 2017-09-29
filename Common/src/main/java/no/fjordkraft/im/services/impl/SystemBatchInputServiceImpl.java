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
import java.util.List;

/**
 * Created by bhavi on 5/22/2017.
 */
@Service
public class SystemBatchInputServiceImpl implements SystemBatchInputService {

    @Autowired
    SystemBatchInputRepository systemBatchInputRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SystemBatchInput updateStatusOfIMSystemBatchInput(SystemBatchInput systemBatchInput , String status) {
        systemBatchInput.setStatus(status);
        systemBatchInput.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return systemBatchInputRepository.save(systemBatchInput);
    }

    /*@Transactional
    public void saveSingleIMSysteBatchInput(TransferFile transferFile) {
        SystemBatchInput imSystemBatchInput = new SystemBatchInput();
        //imSystemBatchInput.setTfId(transferFile.getId());
        imSystemBatchInput.setBrand(transferFile.getBrand());
        //imSystemBatchInput.setFilename(transferFile.getFileName());


        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        imSystemBatchInput.setCreateTime(timestamp);
        imSystemBatchInput.setUpdateTime(timestamp);
        imSystemBatchInput.setStatus(SystemBatchInputStatusEnum.PENDING.getStatus());
        SystemBatchInputPayload systemBatchInputPayload = new SystemBatchInputPayload();
        //systemBatchInputPayload.setPayload(transferFile.getFileContent());
        systemBatchInputPayload.setSystemBatchInput(imSystemBatchInput);
        imSystemBatchInput.setSystemBatchInputPayload(systemBatchInputPayload);
        //imSystemBatchInput.setPayload(transferFile.getFileContent());
        systemBatchInputRepository.save(imSystemBatchInput);
    }*/

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SystemBatchInput saveIMSysteBatchInput(TransferFile transferFile) {
        SystemBatchInput imSystemBatchInput = new SystemBatchInput();
        //imSystemBatchInput.setTfId(transferFile.getId());
        //imSystemBatchInput.setBrand(transferFile.getBrand());
        //imSystemBatchInput.setFilename(transferFile.getFileName());
        imSystemBatchInput.setTransferFile(transferFile);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        imSystemBatchInput.setCreateTime(timestamp);
        imSystemBatchInput.setUpdateTime(timestamp);
        imSystemBatchInput.setStatus(SystemBatchInputStatusEnum.PENDING.getStatus());
        /*SystemBatchInputPayload systemBatchInputPayload = new SystemBatchInputPayload();
        //systemBatchInputPayload.setPayload(transferFile.getFileContent());
        systemBatchInputPayload.setSystemBatchInput(imSystemBatchInput);
        imSystemBatchInput.setSystemBatchInputPayload(systemBatchInputPayload);*/
        //imSystemBatchInput.setPayload(transferFile.getFileContent());
        return systemBatchInputRepository.saveAndFlush(imSystemBatchInput);
    }

    @Transactional()
    public void updateSystemBatchInput(SystemBatchInput systemBatchInput) {
        systemBatchInputRepository.saveAndFlush(systemBatchInput);
    }

    @Override
    public Long getSBIIdByTransferFileName(String transferFileName) {
        return systemBatchInputRepository.getSBIIdByFilename(transferFileName);
    }

    public Integer getNumOfRecordsById(Long id) {
        return systemBatchInputRepository.getNumOfRecordsById(id);
    }

    public List<SystemBatchInput> getAllByStatus(String status){
        return systemBatchInputRepository.findAllByStatus(status);
    }

}
