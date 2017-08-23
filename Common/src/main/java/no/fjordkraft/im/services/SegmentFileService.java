package no.fjordkraft.im.services;

import no.fjordkraft.im.model.SegmentFile;

import java.io.IOException;

/**
 * Created by miles on 6/21/2017.
 */
public interface SegmentFileService {

    void updateSegmentFile(SegmentFile segmentFile);
    String getFileContent(Long id);
    String getImageContent(String accountNo, String brand);
    String getPDFContent(String accountNo, String brand);
    String getCampaignForPreview(String path) throws IOException;
}
