package no.fjordkraft.afi.server.emuxml.services.impl;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoice;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoiceRequest;
import no.fjordkraft.afi.server.emuxml.jpa.emu.BaseEmuParser;
import no.fjordkraft.afi.server.emuxml.jpa.repository.EmuFileRepository;
import no.fjordkraft.afi.server.emuxml.jpa.repository.EmuInvoiceSpecRepository;
import no.fjordkraft.afi.server.emuxml.services.EmuInvoiceFetchService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service("EmuInvoiceFetchService")
public class EmuInvoiceFetchServiceImpl extends BaseEmuParser implements EmuInvoiceFetchService {

    @Resource
    private EmuFileRepository emuFileRepository;

    @Resource
    private EmuInvoiceSpecRepository emuInvoiceSpecRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<EmuInvoice> filterByRequest(EmuInvoiceRequest request) {
        Stopwatch sw = Stopwatch.createStarted();
        verifyRequest(request);
        Page<EmuInvoice> ret = emuInvoiceSpecRepository.findByEmuInvoiceRequest(request);
        if (request.isFetchInvoiceOrderLines()) {
            ret.getContent().stream()
                    .forEach(ei -> ei.setInvoiceOrderLines(emuInvoiceSpecRepository.findEmuInvoiceOrderLines(ei.getInvoiceNo())));
        }
        log.info(String.format("filterByRequest: %s %d  (%s)",
                request.toString(),
                ret.getContent().size(),
                sw.stop().toString()
        ));

        return ret;
    }

    private void verifyRequest(EmuInvoiceRequest request) {
        if (request == null) throw new RuntimeException("missing request");
        if (request.getAccountNo() == null) throw new RuntimeException("missing accountNo");
//        if (request.getFromDate() == null) throw new RuntimeException("missing fromDate");
//        if (request.getToDate() == null) throw new RuntimeException("missing toDate");
    }

}
