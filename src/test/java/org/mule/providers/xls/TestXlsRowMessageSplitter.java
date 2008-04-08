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
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.mule.impl.MuleMessage;
import org.mule.impl.endpoint.MuleEndpointURI;
import org.mule.routing.outbound.XlsRowMessageSplitter;
import org.mule.tck.AbstractMuleTestCase;
import org.mule.tck.MuleTestUtils;
import org.mule.umo.UMOMessage;
import org.mule.umo.UMOSession;
import org.mule.umo.endpoint.UMOEndpoint;
import org.mule.util.IOUtils;

import com.mockobjects.constraint.Constraint;
import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;

public class TestXlsRowMessageSplitter extends AbstractMuleTestCase {
	private UMOEndpoint endpoint1;
	private XlsRowMessageSplitter xlsSplitter;

	// @Override
	protected void doSetUp() throws Exception {
		// setup endpoints
		endpoint1 = getTestEndpoint("Test1Endpoint", UMOEndpoint.ENDPOINT_TYPE_SENDER);
		endpoint1.setEndpointURI(new MuleEndpointURI("test://endpointUri.1"));

		// setup splitter
		xlsSplitter = new XlsRowMessageSplitter();
		xlsSplitter.addEndpoint(endpoint1);
	}

	private InputStream getTestData() throws Exception {
		return IOUtils.getResourceAsStream("employees.xls", getClass());
	}
	

	public void testSplittXls() throws Exception{
        Mock session = MuleTestUtils.getMockSession();
        UMOMessage message = new MuleMessage(getTestData());
        assertTrue(xlsSplitter.isMatch(message));
        session.expect("dispatchEvent", C.args(new ConstraintRow1(), C.eq(endpoint1)));
        session.expect("dispatchEvent", C.args(new ConstraintRow2(), C.eq(endpoint1)));
        session.expect("dispatchEvent", C.args(new ConstraintRow3(), C.eq(endpoint1)));
        session.expect("dispatchEvent", C.args(new ConstraintRow4(), C.eq(endpoint1)));
        session.expect("dispatchEvent", C.args(new ConstraintRow5(), C.eq(endpoint1)));
        xlsSplitter.route(message, (UMOSession)session.proxy(), false);
        session.verify();
        
        message = new MuleMessage(getTestData());
        session.expectAndReturn("sendEvent", C.args(new ConstraintRow1(), C.eq(endpoint1)), message);
        session.expectAndReturn("sendEvent", C.args(new ConstraintRow2(), C.eq(endpoint1)), message);
        session.expectAndReturn("sendEvent", C.args(new ConstraintRow3(), C.eq(endpoint1)), message);
        session.expectAndReturn("sendEvent", C.args(new ConstraintRow4(), C.eq(endpoint1)), message);
        session.expectAndReturn("sendEvent", C.args(new ConstraintRow5(), C.eq(endpoint1)), message);
	}
	
	public void testSplittXlsWithGivenSheet() throws Exception{
		//Set sheet
		xlsSplitter.setSheetName("Sheet1");
        Mock session = MuleTestUtils.getMockSession();
        UMOMessage message = new MuleMessage(getTestData());
        assertTrue(xlsSplitter.isMatch(message));
        session.expect("dispatchEvent", C.args(new ConstraintRow1(), C.eq(endpoint1)));
        session.expect("dispatchEvent", C.args(new ConstraintRow2(), C.eq(endpoint1)));
        session.expect("dispatchEvent", C.args(new ConstraintRow3(), C.eq(endpoint1)));
        session.expect("dispatchEvent", C.args(new ConstraintRow4(), C.eq(endpoint1)));
        session.expect("dispatchEvent", C.args(new ConstraintRow5(), C.eq(endpoint1)));
        xlsSplitter.route(message, (UMOSession)session.proxy(), false);
        session.verify();
        
        message = new MuleMessage(getTestData());
        session.expectAndReturn("sendEvent", C.args(new ConstraintRow1(), C.eq(endpoint1)), message);
        session.expectAndReturn("sendEvent", C.args(new ConstraintRow2(), C.eq(endpoint1)), message);
        session.expectAndReturn("sendEvent", C.args(new ConstraintRow3(), C.eq(endpoint1)), message);
        session.expectAndReturn("sendEvent", C.args(new ConstraintRow4(), C.eq(endpoint1)), message);
        session.expectAndReturn("sendEvent", C.args(new ConstraintRow5(), C.eq(endpoint1)), message);
	}

	
	private class ConstraintRow1 implements Constraint {
		public boolean eval(Object o) {
			final UMOMessage message = (UMOMessage) o;
			final Object payload = message.getPayload();
			assertNotNull(payload);
			assertTrue("Wrong class type for node.", payload instanceof Map);
			Map<Object, HSSFCell> m = (Map) payload;
			assertEquals("Name", m.get("0").toString());
			assertEquals("Age", m.get("1").toString());
			assertEquals("Payment", m.get("2").toString());
			assertEquals("Bonus", m.get("3").toString());
			return true;
		}
	}
	private class ConstraintRow2 implements Constraint {
		public boolean eval(Object o) {
			final UMOMessage message = (UMOMessage) o;
			final Object payload = message.getPayload();
			assertNotNull(payload);
			assertTrue("Wrong class type for node.", payload instanceof Map);
			Map<Object, HSSFCell> m = (Map) payload;
			assertEquals("Olga", m.get("0").toString());
			assertEquals(26.0, m.get("1").getNumericCellValue());
			assertEquals(1400.0, m.get("2").getNumericCellValue());
			assertEquals(0.2, m.get("3").getNumericCellValue());
			return true;
		}
	}
	private class ConstraintRow3 implements Constraint {
		public boolean eval(Object o) {
			final UMOMessage message = (UMOMessage) o;
			final Object payload = message.getPayload();
			assertNotNull(payload);
			assertTrue("Wrong class type for node.", payload instanceof Map);
			Map<Object, HSSFCell> m = (Map) payload;
			assertEquals("Helen", m.get("0").toString());
			assertEquals(30.0, m.get("1").getNumericCellValue());
			assertEquals(2100.0, m.get("2").getNumericCellValue());
			assertEquals(0.1, m.get("3").getNumericCellValue());
			return true;
		}
	}
	private class ConstraintRow4 implements Constraint {
		public boolean eval(Object o) {
			final UMOMessage message = (UMOMessage) o;
			final Object payload = message.getPayload();
			assertNotNull(payload);
			assertTrue("Wrong class type for node.", payload instanceof Map);
			Map<Object, HSSFCell> m = (Map) payload;
			assertEquals("Keith", m.get("0").toString());
			assertEquals(24.0, m.get("1").getNumericCellValue());
			assertEquals(1800.0, m.get("2").getNumericCellValue());
			assertEquals(0.15, m.get("3").getNumericCellValue());
			return true;
		}
	}
	private class ConstraintRow5 implements Constraint {
		public boolean eval(Object o) {
			final UMOMessage message = (UMOMessage) o;
			final Object payload = message.getPayload();
			assertNotNull(payload);
			assertTrue("Wrong class type for node.", payload instanceof Map);
			Map<Object, HSSFCell> m = (Map) payload;
			assertEquals("Cat", m.get("0").toString());
			assertEquals(34.0, m.get("1").getNumericCellValue());
			assertEquals(1900.0, m.get("2").getNumericCellValue());
			assertEquals(0.15, m.get("3").getNumericCellValue());
			return true;
		}
	}

}
