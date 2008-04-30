package org.mule.providers.xls;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mule.extras.client.MuleClient;
import org.mule.providers.xls.test.model.Department;
import org.mule.providers.xls.test.model.Employee;
import org.mule.tck.FunctionalTestCase;
import org.mule.umo.UMOMessage;
import org.mule.util.IOUtils;

public class TestFunctionalXlsTransformers extends FunctionalTestCase {
	private MuleClient client;
	
	protected String getConfigResources() {
		return "excel-transformers-config.xml";
	}
	
	protected void doPreFunctionalSetUp() throws Exception {
		super.doPreFunctionalSetUp();
		client = new MuleClient();
	}

	public void testTransformBeanToXls() throws Exception{
		UMOMessage msg = client.send("vm://testBeanToXls",getTestData() , null);
		assertNotNull(msg.getPayload());
		logger.debug(msg.getPayload());
	}
	
	public void testTransformXlsToBean() throws Exception{
    	InputStream testData = IOUtils.getResourceAsStream("departmentdata.xls", getClass());
		UMOMessage msg = client.send("vm://testXlsToBean",testData , null);
		assertNotNull(msg.getPayload());
		logger.debug(msg.getPayload());
	}
	
	private Map getTestData(){
		Department department = new Department("IT");
		Calendar calendar = Calendar.getInstance();
		calendar.set(1970, 12, 2);
		Date d1 = calendar.getTime();
		calendar.set(1980, 2, 15);
		Date d2 = calendar.getTime();
		calendar.set(1976, 7, 20);
		Date d3 = calendar.getTime();
		calendar.set(1968, 5, 6);
		Date d4 = calendar.getTime();
		calendar.set(1978, 8, 17);
		Date d5 = calendar.getTime();
		Employee chief = new Employee("Markus", 35, 3000, 0.30, d1);
		department.setChief(chief);
		Employee elsa = new Employee("Elsa", 28, 1500, 0.15, d2);
		department.addEmployee(elsa);
		Employee oleg = new Employee("Oleg", 32, 2300, 0.25, d3);
		department.addEmployee(oleg);
		Employee neil = new Employee("Neil", 34, 2500, 0.00, d4);
		department.addEmployee(neil);
		Employee maria = new Employee("Maria", 34, 1700, 0.15, d5);
		department.addEmployee(maria);
		Employee john = new Employee("John", 35, 2800, 0.20, d2);
		department.addEmployee(john);
		maria.setSuperior(oleg);
		oleg.setSuperior(john);
		neil.setSuperior(john);
		Map beans = new HashMap();
		beans.put("department", department);
		return beans;
	}
}
