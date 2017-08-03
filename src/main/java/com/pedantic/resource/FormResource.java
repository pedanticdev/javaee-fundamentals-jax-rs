
package com.pedantic.resource;

import com.pedantic.entities.EmployeeBeanParam;
import java.math.BigDecimal;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author pedantic
 */
@Path("form")
public class FormResource {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void createEmployeeBeanParam(@FormParam("name") String name,
            @FormParam("salary") BigDecimal salary,
            @FormParam("ssn") String ssn,
            @HeaderParam("myHeader") String myHeader,
            @CookieParam("employeeId") Long id) {

        System.out.println(name);
        System.out.println(salary);
        System.out.println(ssn);
        System.out.println(myHeader);
        System.out.println(id);
    }

    @POST
    @Path("bean")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void testBeanParam(@BeanParam EmployeeBeanParam beanParam) {
        System.out.println(beanParam.getName());
        System.out.println(beanParam.getSsn());
        System.out.println(beanParam.getSalary());
        System.out.println(beanParam.getMyHeader());
    }

    @POST
    @Path("map")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void testMap(MultivaluedMap<String, String> form) {
        System.out.println(form.get("name"));
        System.out.println(form.get("ssn"));
        System.out.println(form.get("salary"));

    }
}
