/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.providers.xls;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.mule.providers.xls.test.model.Department;
import org.mule.providers.xls.test.model.Employee;
import org.mule.tck.AbstractTransformerTestCase;
import org.mule.transformers.xls.BeanToXlsTransformer;
import org.mule.umo.transformer.UMOTransformer;

public class TestBeanToXlsTransformer extends AbstractTransformerTestCase {
	private String template;
	private String resultFileName;

	protected void doSetUp() throws Exception {
		template = "src/test/resources/templates/department.xls";
		resultFileName="result.xls";
	}

	public Object getResultData() {
		return "Dummy";
	}

	public UMOTransformer getRoundTripTransformer() throws Exception {
		return null;
	}

	public Object getTestData() {
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

	public UMOTransformer getTransformer() throws Exception {
		BeanToXlsTransformer t = new BeanToXlsTransformer();
		t.setTemplate(template);
		t.initialise();
		return t;
	}

	public boolean compareResults(Object expected, Object result) {
		// Write outputstream in a file
		assertNotNull(result);
		if (result instanceof ByteArrayOutputStream) {
			try {
				FileOutputStream fileOut = new FileOutputStream(resultFileName);
				fileOut.write(((ByteArrayOutputStream)result).toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
}
