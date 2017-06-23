package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.SegmentControlFileResult;
import no.fjordkraft.im.repository.SegmentControlFileRepository;
import no.fjordkraft.im.services.SegmentControlFileService;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by miles on 6/21/2017.
 */
@Service
public class SegmentControlFileServiceImpl implements SegmentControlFileService {

    @Autowired
    SegmentControlFileRepository segmentControlFileRepository;

    @Override
    public SegmentControlFileResult getSegmentControlByAccountNo(String accountNo) {
        Format formatter = new SimpleDateFormat("dd-MMM-yy");
        String currentDate = formatter.format(new Date());
        List<SegmentControlFileResult> segmentControlFileResults;
        segmentControlFileResults = segmentControlFileRepository.getSegmentControlFileByAccountNo(accountNo, currentDate.toString());
        if(null != segmentControlFileResults && IMConstants.ZERO != segmentControlFileResults.size()) {
            return segmentControlFileResults.get(0);
        }
        return null;
    }

    @Override
    public SegmentControlFileResult getSegmentControlByBrand(String brand) {
        Format formatter = new SimpleDateFormat("dd-MMM-yy");
        String currentDate = formatter.format(new Date());
        List<SegmentControlFileResult> segmentControlFileResults;
        segmentControlFileResults = segmentControlFileRepository.getSegmentControlFileByBrand(brand, currentDate.toString());
        if(null != segmentControlFileResults && IMConstants.ZERO != segmentControlFileResults.size()) {
            return segmentControlFileResults.get(0);
        }
        return null;
    }
}
