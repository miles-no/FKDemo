package no.fjordkraft.im.controller;

/**
 * Created by bhavik on 4/28/2017.
 */
import no.fjordkraft.im.model.Employee;
import no.fjordkraft.im.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;

@RestController
public class HelloController {

    @Autowired
    DataSource dataSource;

    @Autowired
    EmployeeRepository employeeRepository;

    @RequestMapping("/")
    public String index() {
        Employee emp = new Employee();
        emp.setEmpId(4);
        emp.setEmpAge(30);
        emp.setEmpName("Aniket");
        emp.setEmpSalary(300000);
        emp.setEmpAddress("mumbai");
        employeeRepository.save(emp);
        return "Greetings from Spring Boot!";
    }

}