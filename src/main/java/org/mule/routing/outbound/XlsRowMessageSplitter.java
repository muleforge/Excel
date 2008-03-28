/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.routing.outbound;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mule.impl.MuleMessage;
import org.mule.routing.outbound.AbstractMessageSplitter;
import org.mule.umo.UMOMessage;
import org.mule.umo.endpoint.UMOEndpoint;

public class XlsRowMessageSplitter extends AbstractMessageSplitter {
	private XlsSplitter splitter;
	private static final ThreadLocal propertiesContext = new ThreadLocal();

	@Override
	protected void initialise(UMOMessage message) {
		super.initialise(message);
		// Except message payload input stream and file
		if (message.getPayload() instanceof InputStream || message.getPayload() instanceof File) {
			splitter = new SimpleXlsSplitter((InputStream)message.getPayload());
		} else {
			throw new IllegalArgumentException("The payload for this router must be of type java.io.InputStream or java.io.File");
		}
		// Cache the properties here because for some message types getting the
		// properties can be expensive
		Map props = new HashMap();
		for (Iterator iterator = message.getPropertyNames().iterator(); iterator.hasNext();) {
			String propertyKey = (String) iterator.next();
			props.put(propertyKey, message.getProperty(propertyKey));
		}
		propertiesContext.set(props);


	}

	@Override
	protected UMOMessage getMessagePart(UMOMessage message, UMOEndpoint endpoint) {
		Map row = splitter.getNextRow();
		if(row != null){
			return new MuleMessage(row, (Map) propertiesContext.get());
		}else{
			return null;
		}
	}
	
	
}
