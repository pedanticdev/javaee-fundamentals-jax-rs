package com.pedantic.resource;

import com.pedantic.config.CDIConfig;
import com.pedantic.entities.Employee;
import com.pedantic.services.PersistenceService;
import com.pedantic.services.QueryService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegrationTest {

    @Inject
    private JaxrsClient jaxrsClient;
    @Inject
    private PersistenceService persistenceService;
    @Inject
    private QueryService queryService;

    private Client client;
    private WebTarget webTarget;

    @ArquillianResource
    private URL base;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "jaxrs.war")
                .addPackage(CDIConfig.class.getPackage())
                .addPackage(Employee.class.getPackage())
                .addPackage(JaxrsClient.class.getPackage())
                .addPackage(QueryService.class.getPackage())
                .addAsResource("persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void init() throws MalformedURLException {
        client = ClientBuilder.newBuilder().connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).build();

        webTarget = client.target(URI.create(new URL(base, "api/v1/employees").toExternalForm()));


    }

    @After
    public void cleanUp() {

        if (client != null) {

            client.close();
        }

    }


    @Test
//    @InSequence(0)
    public void test1() {

        Employee employee = new Employee();

        employee.setName("Donald Trump");
        employee.setSsn("123495ufhd");
        employee.setSalary(new BigDecimal("350000"));

//Test persistence service
        persistenceService.saveEmployee(employee);
        List<Employee> employees = queryService.getEmployees();

        assertNotNull(employee.getId());
        assertNotNull(employees);
        assertTrue(employees.size() > 0);


    }

    @Test
//    @RunAsClient
//    @InSequence(1)
    public void test2() {

        Employee employee = new Employee();

        employee.setName("Lebron James");
        employee.setSsn("123495ufhd");
        employee.setSalary(new BigDecimal("350000"));


        //Test REST service
        Response postResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(employee));

        assertNotNull(postResponse);

        JsonArray jsonArray = webTarget.request(MediaType.APPLICATION_JSON).get(JsonArray.class);

        assertNotNull(jsonArray);
        assertEquals(2, jsonArray.size());

    }

    @Test
    public void test3() {

        JsonArray jsonArray = jaxrsClient.getBreaches("bla@bla.com");
        assertNotNull(jsonArray);
        assertTrue(jsonArray.size() > 0);
    }
}
