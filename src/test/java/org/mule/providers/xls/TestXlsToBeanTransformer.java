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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mule.api.transformer.Transformer;
import org.mule.providers.xls.test.model.Employee;
import org.mule.transformer.AbstractTransformerTestCase;
import org.mule.transformers.xls.XlsToBeanTransformer;
import org.mule.util.IOUtils;

public class TestXlsToBeanTransformer extends AbstractTransformerTestCase {
    private String mappingFile;
    private InputStream testData;

	
	protected void doSetUp() throws Exception{
    	mappingFile = "src/test/resources/xlsMapping.xml";
    	testData = IOUtils.getResourceAsStream("departmentdata.xls", getClass());
    }

	@Override
	public Object getResultData() {
		Map result = new HashMap();
		List<Employee> employees = new ArrayList<Employee>();
		employees.add(new Employee("Olga",26,1400,20));
		employees.add(new Employee("Helen",30,2100,10));
		employees.add(new Employee("Keith",24,1800,15));
		employees.add(new Employee("Cat",34,1900,15));
		result.put("employees", employees);
		return result;
	}

	@Override
	public Transformer getRoundTripTransformer() throws Exception {
		return null;
	}

	@Override
	public Object getTestData() {
		return testData;
	}

	@Override
	public Transformer getTransformer() throws Exception {
		XlsToBeanTransformer t = new XlsToBeanTransformer();
		Map mappingBeans = new HashMap();
		mappingBeans.put("employees", "java.util.ArrayList");
		t.setMappingBeans(mappingBeans);
		t.setMappingFile(mappingFile);
		t.initialise();
		return t;
	}
	
    @Override
	public boolean compareResults(Object expected, Object result) {
    	if(expected instanceof Map && result instanceof Map){
    		Map mE = (Map)expected;
    		Map mR = (Map)result;
    		assertEquals(mE.size(), mR.size());
    		
    		//Load list
    		List<Employee> lE = (List)mE.get("employees");
    		List<Employee> lR = (List)mR.get("employees");
    		assertNotNull(lE);
    		assertNotNull(lR);
    		assertEquals(lE.size(), lR.size());
    	    Arrays.equals(lE.toArray(), lR.toArray());
    		return true;
    	}else{
    		return false;
    	}
 	}
	

}
