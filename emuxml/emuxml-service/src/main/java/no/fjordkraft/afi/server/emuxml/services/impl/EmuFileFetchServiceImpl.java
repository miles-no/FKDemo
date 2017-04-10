package no.fjordkraft.afi.server.emuxml.services.impl;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFile;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFilesRequest;
import no.fjordkraft.afi.server.emuxml.jpa.emu.BaseEmuParser;
import no.fjordkraft.afi.server.emuxml.jpa.repository.EmuFileRepository;
import no.fjordkraft.afi.server.emuxml.jpa.repository.EmuFileSpecRepository;
import no.fjordkraft.afi.server.emuxml.services.EmuFileFetchService;
import no.fjordkraft.afi.server.emuxml.services.EmuFileFileService;
import no.fjordkraft.afi.server.iscuclient.services.InvoiceReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("EmuFileFetchService")
public class EmuFileFetchServiceImpl extends BaseEmuParser implements EmuFileFetchService {

    @Resource
    private EmuFileRepository emuFileRepository;

    @Resource
    private EmuFileSpecRepository emuFileSpecRepository;

    @Autowired
    private EmuFileFileService emuFileFileService;

    @Autowired
    private InvoiceReconciliationService invoiceReconciliationService;

    @Transactional(readOnly = true)
    @Override
    public EmuFile fetch(String filename) {
        return emuFileRepository.findByFilename(filename);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmuFile> filterByRequest(EmuFilesRequest request) {
        Stopwatch sw = Stopwatch.createStarted();
        verifyRequest(request);
        List<EmuFile> ret = emuFileSpecRepository.findByEmuFilesRequest(request);
        log.info(String.format("filterByRequest: %s %d  (%s)",
                request.toString(),
                ret.size(),
                sw.stop().toString()
        ));
        if (Boolean.TRUE.equals(request.getAllFiles())) {
            mergeFiles(request, ret, emuFileFileService.getFilesFromSource(request));
        }
        if (Boolean.TRUE.equals(request.getInvoiceReconciliation())) {
            fetchInvoiceReconciliation(ret);
        }
        return sortFiles(ret);
    }

    private void verifyRequest(EmuFilesRequest request) {
        if (request == null) throw new RuntimeException("missing request");
        if (request.getFromDate() == null) throw new RuntimeException("missing fromDate");
        if (request.getToDate() == null) throw new RuntimeException("missing toDate");
    }

    private List<EmuFile> mergeFiles(EmuFilesRequest request, List<EmuFile> a, List<EmuFile> b) {
        Map<String, EmuFile> aMap = a.stream()
                .collect(Collectors.toMap(EmuFile::getFilename, tf -> tf));
        a.addAll(b.stream()
                .filter(tf -> tf.getProductionDate() == null ||
                        (request.getFromDate() == null || request.getFromDate().before(tf.getProductionDate())) &&
                                (request.getToDate() == null || request.getToDate().after(tf.getProductionDate()))
                )
                .filter(tf -> !aMap.containsKey(tf.getFilename()))
                .collect(Collectors.toList()));
        return a;
    }

    private List<EmuFile> sortFiles(List<EmuFile> ret) {
        Comparator<EmuFile> byFilename = (tf1, tf2) -> tf1.getFilename().compareTo(tf2.getFilename());
        return ret.stream()
                .sorted(byFilename)
                .collect(Collectors.toList());
    }

    private void fetchInvoiceReconciliation(List<EmuFile> ret) {
        ret.stream()
                .forEach(ef -> ef.setInvoiceReconciliation(invoiceReconciliationService.fetchByFilename(ef.getFilename())));
    }

}
