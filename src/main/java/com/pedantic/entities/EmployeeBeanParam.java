package com.pedantic.entities;

import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import java.math.BigDecimal;

public class EmployeeBeanParam {


    @FormParam("name")
    private String name;
    @FormParam("salary")
    private BigDecimal salary;
    @FormParam("ssn")
    private String ssn;

    @HeaderParam("myHeader")
    private String myHeader;

    @CookieParam("employeeId")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getMyHeader() {
        return myHeader;
    }

    public void setMyHeader(String myHeader) {
        this.myHeader = myHeader;
    }
}
