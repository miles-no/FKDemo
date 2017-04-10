package no.fjordkraft.afi.server.emuxml.services.impl;

import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFile;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFileStatusEnum;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuXmlHandleRequest;
import no.fjordkraft.afi.server.emuxml.jpa.emu.BaseEmuParser;
import no.fjordkraft.afi.server.emuxml.jpa.repository.EmuFileRepository;
import no.fjordkraft.afi.server.emuxml.services.EmuFileFetchService;
import no.fjordkraft.afi.server.emuxml.services.EmuFileUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service("EmuFileUpdateService")
public class EmuFileUpdateServiceImpl extends BaseEmuParser implements EmuFileUpdateService {

    @Resource
    private EmuFileRepository emuFileRepository;

    @Autowired
    private EmuFileFetchService emuFileFetchService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public EmuFile create(EmuXmlHandleRequest request) {
        if (!request.isStoreEmuXmlDb()) {
            return null;
        }
        EmuFile emuFile = emuFileFetchService.fetch(request.getEmuXmlFilename());
        if (emuFile == null) {
            emuFile = new EmuFile();
            emuFile.setFilename(request.getEmuXmlFilename());
        }
        emuFile.setNumberOfInvoices(request.getNumTransactions());
        emuFile.setSumOfGiroAmount(null);
        emuFile.setSumOfPrintedAmount(null);
        emuFile.setScanned(null);
        emuFile.setStatus(EmuFileStatusEnum.scanning);
        return emuFileRepository.saveAndFlush(emuFile);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public EmuFile update(EmuFile emuFile) {
        return emuFileRepository.saveAndFlush(emuFile);
    }

}
