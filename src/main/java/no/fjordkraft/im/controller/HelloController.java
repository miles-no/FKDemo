package no.fjordkraft.im.controller;

/**
 * Created by bhavik on 4/28/2017.
 */
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.model.Employee;
import no.fjordkraft.im.model.TransferFile;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.services.Preprocessor;
import no.fjordkraft.im.preprocess.services.PreprocessorEngine;
import no.fjordkraft.im.preprocess.services.PreprocessorService;
import no.fjordkraft.im.repository.EmployeeRepository;
import no.fjordkraft.im.repository.TransferFileRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class HelloController {

    @Autowired
    DataSource dataSource;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    Unmarshaller unMarshaller;

    @Autowired
    PreprocessorEngine preprocessorEngine;

    @Autowired
    PreprocessorService preprocessorService;

    @Autowired
    TransferFileRepository transferFileRepository;

    static int index=10;

    static String baseDirectory = "E:\\\\FuelKraft\\\\invoice_manager\\\\if320-samples";

    @RequestMapping("/")
    public String index() {
        try {
            StreamSource source = new StreamSource(new File("E:\\\\Tutorial\\\\workspace\\\\birtworkspace\\\\Parser\\\\StatementFile1.xml"));
            Statement stmt = (Statement)unMarshaller.unmarshal(source);
            System.out.println(stmt);
        } catch(Exception e){
            e.printStackTrace();
        }

        Statement stmt = preprocessorService.unmarshallStatement("E:\\\\Tutorial\\\\workspace\\\\birtworkspace\\\\Parser\\\\StatementFile1.xml");
        PreprocessRequest<Statement, Object> request = new PreprocessRequest<Statement, Object>();
        request.setStatement(stmt);
        preprocessorEngine.execute(request);
        /*Employee emp = new Employee();
        emp.setEmpId(4);
        emp.setEmpAge(30);
        emp.setEmpName("Aniket");
        emp.setEmpSalary(300000);
        emp.setEmpAddress("mumbai");
        employeeRepository.save(emp);*/

        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "savetransferfile",method = RequestMethod.GET)
    public void saveTransferFile(@RequestParam("filepath") String filePath) throws IOException {

        File f = new File(baseDirectory);
        String[] files = f.list();
        for(String fileName : files ) {
            TransferFile transferFile = new TransferFile();
            File xmlFile = new File(f,fileName);
            String xml = FileUtils.readFileToString(xmlFile, StandardCharsets.ISO_8859_1);

            transferFile.setId(((Integer) index++).longValue());
            transferFile.setFileName(xmlFile.getName());
            transferFile.setBrand("FKAS");
            transferFile.setFileContent(xml);
            transferFile.setTransferState("PENDING");

            transferFileRepository.save(transferFile);
        }
    }

}