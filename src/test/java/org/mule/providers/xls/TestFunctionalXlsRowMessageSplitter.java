package org.mule.providers.xls;

import java.io.InputStream;

import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;
import org.mule.util.IOUtils;

public class TestFunctionalXlsRowMessageSplitter extends FunctionalTestCase {
	private InputStream testData;
	private MuleClient client;
	
	protected String getConfigResources() {
		return "excel-splitter-config.xml";
	}
	
	protected void doSetUp() throws Exception {
		testData= IOUtils.getResourceAsStream("employees.xls", getClass());
		client = new MuleClient();
	}

	public void testXlsRowSplitter1() throws Exception{
		client.send("vm://testXlsSplitter1", testData, null);
	}
	
	public void testXlsRowSplitter2() throws Exception{
		client.send("vm://testXlsSplitter2", testData, null);
	}
}
