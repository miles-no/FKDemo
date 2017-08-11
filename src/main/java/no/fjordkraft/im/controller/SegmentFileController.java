package no.fjordkraft.im.controller;

import no.fjordkraft.im.model.SegmentFile;
import no.fjordkraft.im.services.SegmentFileService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by miles on 6/21/2017.
 */
@RestController
@RequestMapping("/segment/file")
public class SegmentFileController {

    @Autowired
    SegmentFileService segmentFileService;

    @RequestMapping(value = "",method = RequestMethod.PUT)
    @ResponseBody
    public void updateSegmentFile(@RequestParam("id") Long id,
                                  @RequestPart("fileContent") MultipartFile fileContent) throws IOException {

        byte[] bytes = fileContent.getBytes();
        String payload = Base64.encodeBase64String(bytes);

        SegmentFile segmentFile = new SegmentFile();
        segmentFile.setId(id);
        segmentFile.setFileContent(payload);
        segmentFileService.updateSegmentFile(segmentFile);
    }
}
