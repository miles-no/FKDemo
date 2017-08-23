package no.fjordkraft.im.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by bhavi on 4/28/2017.
 */
@Entity
@Table(name="Employee")
public class Employee {

    @Column(name="EMPID")
    @Id
    private long empId;
    @Column(name="EMPNAME")
    private String empName;
    @Column(name="EMPADDRESS")
    private String empAddress;
    @Column(name="EMPAGE")
    private long empAge;
    @Column(name="SALARY")
    private long empSalary;

    public long getEmpId() {
        return empId;
    }

    public void setEmpId(long empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpAddress() {
        return empAddress;
    }

    public void setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
    }

    public long getEmpSalary() {
        return empSalary;
    }

    public void setEmpSalary(long empSalary) {
        this.empSalary = empSalary;
    }

    public long getEmpAge() {
        return empAge;
    }

    public void setEmpAge(long empAge) {
        this.empAge = empAge;
    }
}
