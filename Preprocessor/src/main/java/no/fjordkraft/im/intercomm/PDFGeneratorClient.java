package no.fjordkraft.im.intercomm;


import feign.Request;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name="im-pdfgenerator",configuration = PDFGeneratorClient.ClientConfiguration.class)
public interface PDFGeneratorClient {

    @RequestMapping(method = RequestMethod.POST, value = "/pdf/{statementId}")
    void processStatement(@PathVariable("statementId") Long statementId);

    @RequestMapping(method = RequestMethod.POST, value = "/pdf")
    void processStatement(List<Long> statementList);

    @RequestMapping(method = RequestMethod.POST, value = "/pdf/Statement/generateFile")
    byte[] processSingleStatement(@RequestParam("processFilePath")String processFilePath,@RequestParam("brand")String brand,@RequestParam("layoutID")String layoutID);


    @Configuration
    public class ClientConfiguration {

        @Bean
        public Request.Options options() {
            Request.Options options = new Request.Options(
                    5000, 60000
            );
            return options;
        }
    }
    
}
