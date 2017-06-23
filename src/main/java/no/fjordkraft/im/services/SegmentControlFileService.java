package no.fjordkraft.im.services;

import no.fjordkraft.im.model.SegmentControlFileResult;

/**
 * Created by miles on 6/21/2017.
 */
public interface SegmentControlFileService {

    SegmentControlFileResult getSegmentControlByAccountNo(String accountNo);
    SegmentControlFileResult getSegmentControlByBrand(String brand);
}
