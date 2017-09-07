package no.fjordkraft.im.intercomm;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient("im-pdfgenerator")
public interface PDFGeneratorClient {

    @RequestMapping(method = RequestMethod.POST, value = "/pdf/{statementId}")
    void processStatement(@PathVariable("statementId") Long statementId);
    
}
