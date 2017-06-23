package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.SegmentControlFileResult;
import no.fjordkraft.im.model.SegmentFile;
import no.fjordkraft.im.repository.SegmentFileRepository;
import no.fjordkraft.im.services.SegmentFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by miles on 6/21/2017.
 */
@Service
public class SegmentFileServiceImpl implements SegmentFileService{

    @Autowired
    SegmentFileRepository segmentFileRepository;

    @Autowired
    SegmentControlFileServiceImpl segmentControlFileService;
    
    @Override
    public void updateSegmentFile(SegmentFile segmentFile) {
        SegmentFile currentSegmentFile = segmentFileRepository.findOne(segmentFile.getId());
        currentSegmentFile.setFileContent(segmentFile.getFileContent());
        segmentFileRepository.saveAndFlush(currentSegmentFile);
    }

    @Override
    public String getFileContent(Long id) {
        SegmentFile segmentFile = segmentFileRepository.findOne(id);
        return segmentFile.getFileContent();
    }

    @Override
    public String getImageContent(String accountNo, String brand) {
        String fileContent = null;
        SegmentControlFileResult segmentControlFileResult;
        Integer campaignID;
        segmentControlFileResult = segmentControlFileService.getSegmentControlByAccountNo(accountNo);
        if(null != segmentControlFileResult) {
            campaignID = segmentControlFileResult.getIdCampaign();
            fileContent = getFileContent(Long.valueOf(campaignID));
        } else {
            segmentControlFileResult = segmentControlFileService.getSegmentControlByBrand(brand);
            if(null != segmentControlFileResult) {
                campaignID = segmentControlFileResult.getIdCampaign();
                fileContent = getFileContent(Long.valueOf(campaignID));
            }
        }
        return fileContent;
    }

    @Override
    public String getPDFContent(String accountNo, String brand) {
        String fileContent = null;
        SegmentControlFileResult segmentControlFileResult;
        Integer attachID;
        segmentControlFileResult = segmentControlFileService.getSegmentControlByAccountNo(accountNo);
        if(null != segmentControlFileResult) {
            attachID = segmentControlFileResult.getIdAttach();
            fileContent = getFileContent(Long.valueOf(attachID));
        } else {
            segmentControlFileResult = segmentControlFileService.getSegmentControlByBrand(brand);
            if(null != segmentControlFileResult) {
                attachID = segmentControlFileResult.getIdAttach();
                fileContent = getFileContent(Long.valueOf(attachID));
            }
        }
        return fileContent;
    }

}
